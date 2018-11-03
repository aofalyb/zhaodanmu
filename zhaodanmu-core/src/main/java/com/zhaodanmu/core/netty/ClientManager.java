package com.zhaodanmu.core.netty;


public interface ClientManager {

    void init();

    void put(NettyClient nettyClient);

    void destroy();

    NettyClient get(String id);

}
