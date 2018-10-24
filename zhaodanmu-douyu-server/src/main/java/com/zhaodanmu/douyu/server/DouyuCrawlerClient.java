package com.zhaodanmu.douyu.server;


import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.netty.DouyuConnClientChannelHandler;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketDecoder;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketEncoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class DouyuCrawlerClient extends NettyClient {

    private String rid;

    private CountDownLatch loginFuture;
    private Thread reConnectThread;

    private Connection connection;
    public DouyuCrawlerClient(String rid) {
        this.rid = rid;
        loginFuture = new CountDownLatch(1);

    }

    //接收login success的回调(重连也会调用)
//    public static void notifyLoginSuccess() {
//        loginFuture.countDown();
//    }


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
            connect.get(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
            if(!connect.isSuccess()) {
                throw new NettyClientException("connect fail ,rid="+rid);
            }

            //超时检查
            if(!loginFuture.await(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS)) {
                throw new NettyClientException("login time out ,rid="+rid);
            }

        } catch (Throwable throwable) {
            if(!(throwable instanceof NettyClientException)) {
                throw new NettyClientException("login error ,rid="+rid,throwable);
            }
            throw throwable;
        }
    }


    @Override
    protected void doStop(Listener listener) throws Throwable {
        //先关闭connection


        super.doStop(listener);
    }


    public void reConnect(Connection connection) {
        if(reConnectThread != null) return;

        reConnectThread = new Thread(() -> {
            int times = 0;
            while (connection.getState() != ConnectionState.CONNECTED) {
                Log.defLogger.error("reconnect rid={} , times = {}.",rid,++times);

                ChannelFuture connect = connect("openbarrage.douyutv.com", 8601);
                try {
                    connect.get(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if(!connect.isSuccess()) {
                    throw new NettyClientRuntimeException("connect fail ,rid="+rid);
                }
                try {
                    //超时检查
                    if(!loginFuture.await(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS)) {
                        throw new NettyClientException("login time out ,rid="+rid);
                    }

                } catch (Exception e) {
                   Log.defLogger.error("exception caught when doing re-connect",e);
                } finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
            Log.defLogger.error("reconnect douyu rid={} success, retry times = {}.",rid,times);

        });
        reConnectThread.setName("douyu-reconnect");
        reConnectThread.start();

    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
