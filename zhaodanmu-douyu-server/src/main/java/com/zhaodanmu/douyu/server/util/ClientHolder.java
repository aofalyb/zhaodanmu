package com.zhaodanmu.douyu.server.util;

import com.zhaodanmu.core.netty.NettyClient;

public class ClientHolder {
    private static NettyClient nettyClient;
    public static void hold(NettyClient hold) {
        nettyClient = hold;
    }

    public static NettyClient get() {
        return nettyClient;
    }
}
