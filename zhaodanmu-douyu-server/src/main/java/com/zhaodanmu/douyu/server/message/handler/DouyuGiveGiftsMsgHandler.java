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
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.douyu.server.util.ClientHolder;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.model.GiveGiftModel;

import java.util.Calendar;
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
        //persistenceService.bufferedInsert(giveGifts);

        String giftId = String.valueOf(giveGifts.getGfid());
        String rid = String.valueOf(giveGifts.getRid());
        //礼物贡献值
        int devote = 0;
        PropGiftInfo propGift = PropGiftConfig.getPropGift(giftId);
        if(propGift == null) {
            DouyuCrawlerClient douyuCrawlerClient = ClientHolder.get(rid);
            RoomDetail.GiftEntity newGift = douyuCrawlerClient.getRoomDetail().getGiftInfo(giveGifts.getGfid());
            if(newGift == null) {
                Log.sysLogger.error("un known gift id: {}",giftId);
                return true;
            }
            devote = newGift.getGx();
        } else {
            devote = propGift.getDevote();
        }

//        if(redisManager.exsit(U_GIFT_RANK)) {
//            //redis 房间弹幕数率
//            redisManager.zIncr(U_GIFT_RANK ,devote, String.valueOf(giveGifts.getUid()));
//        } else {
//            //redis 房间弹幕数率
//            redisManager.zIncr(U_GIFT_RANK ,devote, String.valueOf(giveGifts.getUid()));
//            //设置过期时间
//            redisManager.expireAt(U_GIFT_RANK,getExpireUnixTime());
//        }
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
