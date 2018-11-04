package com.zhaodanmu.douyu.server.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhaodanmu.common.utils.Log;

import java.util.concurrent.TimeUnit;

/**
 * 素质广场~
 * Created by Administrator on 2018/11/4.
 */
public class ActiveUserEventCache {

    private static Cache<Long, UserEventCache> cacher;

    private static ActiveUserEventCache eventCache = new ActiveUserEventCache();

    public static ActiveUserEventCache getInstance() {
        return eventCache;
    }

    private ActiveUserEventCache() {
        cacher = CacheBuilder.newBuilder()
                .expireAfterAccess(3 * 60, TimeUnit.SECONDS)
                .initialCapacity(50 * 10000)
                .maximumSize(100 * 10000)
                .build();
    }

    public void cache(long uid,UserEventCache userEventCache) {
        cacher.put(uid,userEventCache);
        Log.sysLogger.debug("lru cache: {}",userEventCache);
    }


    public UserEventCache get(long uid) {
        return cacher.getIfPresent(uid);
    }






}
