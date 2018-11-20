package com.zhaodanmu.douyu.server.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/11/20.
 */
public class SimpleCache {

    private static Map<Object,Object> cache = new ConcurrentHashMap<>();


    public static void store(Object key,Object value) {
        cache.put(key,value);
    }

    public static Object get(Object key) {
        return cache.get(key);
    }
}
