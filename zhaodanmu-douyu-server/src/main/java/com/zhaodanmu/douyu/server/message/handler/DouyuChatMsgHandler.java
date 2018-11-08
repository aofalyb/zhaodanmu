package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.redis.RedisManager;
import com.zhaodanmu.core.redis.RedisServer;
import com.zhaodanmu.douyu.server.cache.JVMLruCache;
import com.zhaodanmu.douyu.server.cache.LruCache;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.model.ChatMessageModel;
import com.zhaodanmu.persistence.elasticsearch.model.SimpinleUserModel;

import java.util.Calendar;
import java.util.Map;

public class DouyuChatMsgHandler implements IMessageHandler<DouyuMessage> {

    private static JVMLruCache eventCache = JVMLruCache.getInstance();

    private static RedisManager redisManager = RedisServer.getManager();

    private static final String U_CHAT_RANK = "u_chat_rank";
    private static final String R_CHAT_RANK = "r_chat_rank";

    private PersistenceService persistenceService;
    public DouyuChatMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }


    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getData();

        ChatMessageModel chatMessage = JSON.parseObject(JSON.toJSONString(attributes), ChatMessageModel.class);
        persistenceService.bufferedInsert(chatMessage);

        SimpinleUserModel simpinleUserModel = JSON.parseObject(JSON.toJSONString(attributes), SimpinleUserModel.class);
        //lru cache
        final String lruCacheKey = "uid:" + simpinleUserModel.getUid();
        SimpinleUserModel cachedUser = (SimpinleUserModel) eventCache.get(lruCacheKey);
        if(cachedUser == null || !cachedUser.equals(simpinleUserModel)) {
            persistenceService.bufferedInsert(simpinleUserModel);
            eventCache.cache(new LruCache() {
                @Override
                public Object getKey() {
                    return lruCacheKey;
                }

                @Override
                public Object getCacheData() {
                    return simpinleUserModel;
                }
            });
        } else if(cachedUser != null) {
            Log.sysLogger.debug("lru cache hit key: {}",lruCacheKey);
        }

        if(redisManager.exsit(U_CHAT_RANK)) {
            //redis 个人弹幕数排行
            redisManager.zIncr(U_CHAT_RANK, 1.0, String.valueOf(simpinleUserModel.getUid()));
        } else {
            //redis 个人弹幕数排行
            redisManager.zIncr(U_CHAT_RANK, 1.0, String.valueOf(simpinleUserModel.getUid()));
            //设置过期时间
            redisManager.expireAt(U_CHAT_RANK,getExpireUnixTime());
        }

        if(redisManager.exsit(R_CHAT_RANK)) {
            //redis 房间弹幕数率
            redisManager.zIncr(R_CHAT_RANK ,1.0, String.valueOf(simpinleUserModel.getRid()));
        } else {
            //redis 房间弹幕数率
            redisManager.zIncr(R_CHAT_RANK ,1.0, String.valueOf(simpinleUserModel.getRid()));
            //设置过期时间
            redisManager.expireAt(R_CHAT_RANK,getExpireUnixTime());
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
