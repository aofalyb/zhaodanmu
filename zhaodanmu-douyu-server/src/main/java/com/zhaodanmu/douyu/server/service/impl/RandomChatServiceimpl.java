package com.zhaodanmu.douyu.server.service.impl;

import com.zhaodanmu.douyu.server.cache.JVMLruCache;
import com.zhaodanmu.douyu.server.cache.LruCache;
import com.zhaodanmu.douyu.server.service.RandomChatService;
import com.zhaodanmu.persistence.api.PersistenceService;

import java.util.List;

/**
 * Created by Administrator on 2018/11/8.
 */
public class RandomChatServiceimpl implements RandomChatService {

    private PersistenceService persistenceService;

    private JVMLruCache userCahce = JVMLruCache.getInstance();

    public RandomChatServiceimpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public List getRandom(int randomSize) {
        return null;
    }
}
