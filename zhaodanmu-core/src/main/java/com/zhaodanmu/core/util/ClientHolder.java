package com.zhaodanmu.core.util;

import com.zhaodanmu.core.netty.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHolder {
    private static Map<String,NettyClient> nettyClientHolder = new ConcurrentHashMap<>();

    public static void hold(String id,NettyClient hold) {
        nettyClientHolder.put(id,hold);
    }

    public static NettyClient get(String id) {
        return nettyClientHolder.get(id);
    }

    public static NettyClient remove(String id) {
        return nettyClientHolder.remove(id);
    }
}
