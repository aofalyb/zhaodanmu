package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.redis.RedisManager;
import com.zhaodanmu.core.redis.RedisServer;
import com.zhaodanmu.douyu.server.DouyuCrawlerClient;
import com.zhaodanmu.douyu.server.PropGiftConfig;
import com.zhaodanmu.douyu.server.PropGiftInfo;
import com.zhaodanmu.douyu.server.RoomDetail;
import com.zhaodanmu.douyu.server.cache.SimpleCache;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.douyu.server.util.ClientHolder;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.model.GiveGiftModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DouyuGiveGiftsMsgHandler implements IMessageHandler<DouyuMessage> {

    private static final String U_GIFT_RANK = "u_gift_rank";

    private RedisManager redisManager = RedisServer.getManager();
    private PersistenceService persistenceService;
    public DouyuGiveGiftsMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getData();

        GiveGiftModel giveGifts = JSON.parseObject(JSON.toJSONString(attributes), GiveGiftModel.class);
        //写入持久化
        persistenceService.bufferedInsert(giveGifts);

        String giftId = String.valueOf(giveGifts.getGfid());
        String rid = String.valueOf(giveGifts.getRid());
        //礼物贡献值
        int devote = 0;
        PropGiftInfo propGift = PropGiftConfig.getPropGift(giftId);
        Map giftCache = new HashMap();
        if(propGift == null) {
            DouyuCrawlerClient douyuCrawlerClient = ClientHolder.get(rid);
            RoomDetail.GiftEntity newGift = douyuCrawlerClient.getRoomDetail().getGiftInfo(giveGifts.getGfid());
            if(newGift == null) {
                Log.sysLogger.error("un known gift id: {},rid: {}",giftId,rid);
                return true;
            }
            devote = newGift.getGx();
            giftCache.put("devote",devote);
            giftCache.put("name",newGift.getName());
            giftCache.put("himg",newGift.getHimg());
            SimpleCache.store("gift:" + giftId,giftCache);
        } else {
            devote = propGift.getDevote();
            giftCache.put("devote",devote);
            giftCache.put("name",propGift.getName());
            giftCache.put("himg",propGift.getHimg());
            SimpleCache.store("gift:" + giftId,giftCache);
        }



        if(redisManager.exsit(U_GIFT_RANK)) {
            //个人礼物排行
            redisManager.zIncr(U_GIFT_RANK ,devote, String.valueOf(giveGifts.getUid()));
        } else {
            //房间礼物排行
            redisManager.zIncr(U_GIFT_RANK ,devote, String.valueOf(giveGifts.getUid()));
            //设置过期时间
            redisManager.expireAt(U_GIFT_RANK,getExpireUnixTime());
        }
        return true;
    }


    private long getExpireUnixTime() {
        //毫秒
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
}
