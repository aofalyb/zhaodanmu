package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.netty.NettyServer;
import com.zhaodanmu.douyu.server.netty.DouyuHttpRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 暴露http服务
 */
public class DouyuHttpServer extends NettyServer {

    public DouyuHttpServer(int port, String host) {
        super(port, host);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return new DouyuHttpRespHandler();
    }

    @Override
    protected ChannelHandler getDecoder() {
        return new HttpRequestDecoder();
    }

    @Override
    protected ChannelHandler getEncoder() {
        return new HttpResponseEncoder();
    }

    @Override
    protected void initOptions(ServerBootstrap b) {
        super.initOptions(b);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
