package com.zhaodanmu.douyu.server;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.netty.NettyRuntimeException;
import com.zhaodanmu.core.netty.NettyServer;
import com.zhaodanmu.douyu.server.netty.DouyuHttpRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    public void sync() {
        final CountDownLatch lock = new CountDownLatch(1);
        start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                Log.sysLogger.info("http server start success,host&port: [{}]",CC.httpHost + ":" + CC.httpPort);
                lock.countDown();
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.sysLogger.error("http server start failed,host&port: [{}]",CC.httpHost + ":" + CC.httpPort,cause);
                lock.countDown();
            }
        });

        try {
            if(!lock.await(3000,TimeUnit.MILLISECONDS)) {
                Log.sysLogger.error("http server start time out ,host&port: [{}]",CC.httpHost + ":" + CC.httpPort);
                throw new NettyRuntimeException("http server start time out");
            }
        } catch (InterruptedException e) {
        }
    }
}
