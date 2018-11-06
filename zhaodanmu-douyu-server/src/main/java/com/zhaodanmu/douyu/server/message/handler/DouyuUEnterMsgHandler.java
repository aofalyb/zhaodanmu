package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.cache.JVMLruCache;
import com.zhaodanmu.douyu.server.cache.LruCache;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.model.SimpinleUserModel;

import java.util.Map;

public class DouyuUEnterMsgHandler implements IMessageHandler<DouyuMessage> {

    private static JVMLruCache eventCache = JVMLruCache.getInstance();

    private PersistenceService persistenceService;
    public DouyuUEnterMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getData();
        SimpinleUserModel simpinleUserModel = JSON.parseObject(JSON.toJSONString(attributes), SimpinleUserModel.class);
        //lru cache
        final String lruCacheKey = "uid:" + simpinleUserModel.getUid();
        SimpinleUserModel cachedUser = (SimpinleUserModel) eventCache.get(lruCacheKey);
        if(cachedUser == null || !simpinleUserModel.equals(cachedUser)) {
            persistenceService.bufferedInsert(simpinleUserModel);
            eventCache.cache(new LruCache() {
                @Override
                public Object getKey() {
                    return "uid:" + simpinleUserModel.getUid();
                }

                @Override
                public Object getCacheData() {
                    return simpinleUserModel;
                }
            });
        }

        return true;
    }
}
