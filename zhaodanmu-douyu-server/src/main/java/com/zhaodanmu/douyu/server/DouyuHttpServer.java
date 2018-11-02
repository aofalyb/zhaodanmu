package com.zhaodanmu.douyu.server;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.dispatcher.ControllerDispatcher;
import com.zhaodanmu.core.netty.NettyRuntimeException;
import com.zhaodanmu.core.netty.NettyServer;
import com.zhaodanmu.douyu.server.netty.DouyuHttpRespHandler;
import com.zhaodanmu.douyu.server.netty.controller.SearchController;
import com.zhaodanmu.douyu.server.netty.controller.UserController;
import com.zhaodanmu.persistence.api.PersistenceService;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 暴露http服务
 */
public class DouyuHttpServer extends NettyServer {

    private ControllerDispatcher controllerDispatcher;

    private PersistenceService persistenceService;

    public DouyuHttpServer(int port, String host, PersistenceService persistenceService) {
        super(port, host);
        this.persistenceService = persistenceService;
        controllerDispatcher = new ControllerDispatcher();

        controllerDispatcher.register(new SearchController(persistenceService));
        controllerDispatcher.register(new UserController(persistenceService));
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return new DouyuHttpRespHandler(controllerDispatcher);
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
    protected int getWorkThreadNum() {
        return 5;
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
