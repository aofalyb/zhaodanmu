package com.zhaodanmu.douyu.server.util;

import com.zhaodanmu.core.netty.NettyClient;
import com.zhaodanmu.douyu.server.DouyuCrawlerClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHolder {
    private static Map<String, DouyuCrawlerClient> nettyClientHolder = new ConcurrentHashMap<>();

    public static void hold(String id,DouyuCrawlerClient hold) {
        nettyClientHolder.put(id,hold);
    }

    public static DouyuCrawlerClient get(String id) {
        return nettyClientHolder.get(id);
    }

    public static NettyClient remove(String id) {
        return nettyClientHolder.remove(id);
    }


    public static Collection<DouyuCrawlerClient> getAll() {

        return nettyClientHolder.values();
    }
}
