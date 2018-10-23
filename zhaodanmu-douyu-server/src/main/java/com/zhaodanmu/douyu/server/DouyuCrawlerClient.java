package com.zhaodanmu.douyu.server;


import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.netty.ConnectionState;
import com.zhaodanmu.core.netty.NettyClient;
import com.zhaodanmu.core.netty.NettyClientException;
import com.zhaodanmu.douyu.server.message.DouyuLoginMessage;
import com.zhaodanmu.douyu.server.netty.DouyuConnClientChannelHandler;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketDecoder;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketEncoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class DouyuCrawlerClient extends NettyClient {

    private String rid;

    private static CountDownLatch loginFuture = new CountDownLatch(1);

    private Thread reConnectThread;

    public DouyuCrawlerClient(String rid) {
        this.rid = rid;
        loginFuture = new CountDownLatch(1);
    }

    //接收login success的回调(重连也会调用)
    public static void notifyLoginSuccess() {
        loginFuture.countDown();
    }


    @Override
    protected ChannelHandler getDecoder() {
        return new DouyuPacketDecoder();
    }

    @Override
    protected ChannelHandler getEncoder() {
        return new DouyuPacketEncoder();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return new DouyuConnClientChannelHandler(rid);
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        super.doStart(listener);

        try {
            ChannelFuture connect = connect("openbarrage.douyutv.com", 8601);
            connect.get(DouyuLoginMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
            if(!connect.isSuccess()) {
                throw new NettyClientException("connect fail ,rid="+rid);
            }

            //超时检查
            if(!loginFuture.await(DouyuLoginMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS)) {
                throw new NettyClientException("login time out ,rid="+rid);
            }

        } catch (Throwable throwable) {
            if(!(throwable instanceof NettyClientException)) {
                throw new NettyClientException("login error ,rid="+rid,throwable);
            }
            throw throwable;
        }
    }




    public void reLogin(Connection connection) {
        if(reConnectThread != null) return;

        reConnectThread = new Thread(() -> {
            int times = 0;
            while (connection.getState() != ConnectionState.CONNECTED) {
                Log.defLogger.error("reconnect rid={} , times = {}.",rid,++times);
                try {
                    ChannelFuture connect = connect("openbarrage.douyutv.com", 8601);
                    connect.get(DouyuLoginMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
                    if(!connect.isSuccess()) {
                        throw new NettyClientException("connect fail ,rid="+rid);
                    }

                    //超时检查
                    if(!loginFuture.await(DouyuLoginMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS)) {
                        throw new NettyClientException("login time out ,rid="+rid);
                    }

                } catch (Exception e) {
                   Log.defLogger.error("exception caught when doing re-login",e);
                } finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
            Log.defLogger.error("reconnect rid={} success, retry times = {}.",rid,times);

        });
        reConnectThread.setName("reconnect-thread");
        reConnectThread.start();

    }
}
