package com.zhaodanmu.douyu.server;


import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.common.NamedPoolThreadFactory;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.netty.DouyuConnClientChannelHandler;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketDecoder;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketEncoder;
import com.zhaodanmu.core.util.ClientHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DouyuCrawlerClient extends NettyClient {

    private String rid;
    private Lock lock = new ReentrantLock();
    private Condition loginSuccessCondition = lock.newCondition();
    private ConnectionManager connectionManager;
    //断线重连次数
    private AtomicInteger retryTimes = new AtomicInteger(0);
    //重试标志位
    private AtomicBoolean retrying = new AtomicBoolean(false);

    private static ThreadPoolExecutor reConnectThread = new ThreadPoolExecutor(1, 1,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());;

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
        ClientHolder.hold(rid,this);
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
        return new DouyuConnClientChannelHandler(rid,connectionManager = new ClientConnectionManager());
    }

    @Override
    protected boolean doStart(Listener listener)  {
        super.doStart(listener);
        //异步转同步
        return connect();
    }

    private boolean connect() {
        try {
            lock.lock();
            ChannelFuture future = connect("openbarrage.douyutv.com", 8601);
            boolean timeOut = loginSuccessCondition.await(DouyuLoginReqMessage.LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
            if(!timeOut) {
                Log.sysLogger.error("connect rid:{} timeout.",rid);
                future.channel().close();
            }
            return timeOut;
        } catch (Exception e) {
            Log.sysLogger.error("exception caught when rid:{} do doStart.",rid,e);
        } finally {
            lock.unlock();
        }
        return false;
    }

    public void doStart() {
        if(!doStart(this.defaultListener)) {
            reConnect();
        }
    }

    public void release() {
        lock.lock();
        try {
            loginSuccessCondition.signal();
            retrying.set(false);
            retryTimes.set(0);
        } finally {
            lock.unlock();
        }
    }


    @Override
    protected void doStop(Listener listener) throws Throwable {
        super.doStop(listener);
    }

    /**
     * 断线重连
     */
    public void reConnect() {
        if(!retrying.compareAndSet(false,true)) {
            return;
        }
        reConnectThread.execute(() -> {
            Log.sysLogger.info("trying reconnect conn-rid={}.",rid);
            while (!connect()) {
                retryTimes.incrementAndGet();
                Log.sysLogger.info("trying reconnect conn-rid={}, retry times={}.",rid,retryTimes.get());
                Log.sysLogger.info("reconnect conn-rid={} time out,wait a second",rid);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
            Log.sysLogger.info("reconnect conn-rid={} success, retry times = {}.",rid,retryTimes.get());
        });
    }


    @Override
    public String toString() {
        return "DouyuCrawlerClient{" +
                "rid='" + rid + "'}";
    }
}
