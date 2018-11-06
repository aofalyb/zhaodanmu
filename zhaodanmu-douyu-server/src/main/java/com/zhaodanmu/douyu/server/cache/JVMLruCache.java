package com.zhaodanmu.douyu.server.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhaodanmu.common.utils.Log;

import java.util.concurrent.TimeUnit;

/**
 * 素质广场~
 * Created by Administrator on 2018/11/4.
 */
public class JVMLruCache {

    private static Cache<Object, Object> cacher;

    private static JVMLruCache eventCache = new JVMLruCache();

    public static JVMLruCache getInstance() {
        return eventCache;
    }

    private JVMLruCache() {
        cacher = CacheBuilder.newBuilder()
                .expireAfterAccess(3 * 60, TimeUnit.SECONDS)
                .initialCapacity(50 * 10000)
                .maximumSize(100 * 10000)
                .build();
    }

    public void cache(LruCache lruCache) {
        cacher.put(lruCache.getKey(), lruCache.getCacheData());
        Log.sysLogger.debug("lru cache: {}", lruCache.getCacheData());
    }


    public Object get(Object key) {
        return cacher.getIfPresent(key);
    }






}
