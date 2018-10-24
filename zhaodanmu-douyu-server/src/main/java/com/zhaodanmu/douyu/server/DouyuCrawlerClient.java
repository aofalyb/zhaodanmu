package com.zhaodanmu.douyu.server;


import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.netty.DouyuConnClientChannelHandler;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketDecoder;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketEncoder;
import com.zhaodanmu.douyu.server.util.ClientHolder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DouyuCrawlerClient extends NettyClient {

    private String rid;
    private Thread reConnectThread;
    private Connection connection;
    private Lock lock = new ReentrantLock();
    private Condition loginSuccessCondition = lock.newCondition();
    private Listener defaultListener = new Listener() {
        @Override
        public void onSuccess(Object... args) {

        }

        @Override
        public void onFailure(Throwable cause) {

        }
    };

    public DouyuCrawlerClient(String rid) {
        this.rid = rid;
        ClientHolder.hold(this);
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
    protected boolean doStart(Listener listener)  {
        super.doStart(listener);
        //异步转同步
        boolean connect = false;
        try {
            lock.lock();
            ChannelFuture future = connect("openbarrage.douyutv.com", 8601);
            boolean timeOut = loginSuccessCondition.await(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
            if(!timeOut) {
                Log.defLogger.error("connect rid:{} timeout.",rid);
            }
            return timeOut;
        } catch (Exception e) {
            Log.defLogger.error("exception caught when rid:{} do start.",rid,e);
        } finally {
            lock.unlock();
        }
        return connect;
    }


    public boolean start() {
        return doStart(this.defaultListener);
    }

    public void release() {
        lock.lock();
        try {
            loginSuccessCondition.signal();
        } finally {
            lock.unlock();
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
//                try {
//                    //超时检查
//                    if(!loginFuture.await(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS)) {
//                        throw new NettyClientException("login time out ,rid="+rid);
//                    }
//
//                } catch (Exception e) {
//                   Log.defLogger.error("exception caught when doing re-connect",e);
//                } finally {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//
//                    }
//                }
            }
            Log.defLogger.error("reconnect douyu rid={} success, retry times = {}.",rid,times);

        });
        reConnectThread.setName("douyu-reconnect");
        reConnectThread.start();

    }

    @Override
    public String toString() {
        return "DouyuCrawlerClient{" +
                "rid='" + rid + '\'' +
                ", connection=" + connection +
                '}';
    }
}
