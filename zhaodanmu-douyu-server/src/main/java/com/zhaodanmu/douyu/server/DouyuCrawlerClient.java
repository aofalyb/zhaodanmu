package com.zhaodanmu.douyu.server;


import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.dispatcher.MessageHandlerDispatcher;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.message.handler.*;
import com.zhaodanmu.douyu.server.netty.DouyuConnClientChannelHandler;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketDecoder;
import com.zhaodanmu.douyu.server.netty.codec.DouyuPacketEncoder;
import com.zhaodanmu.douyu.server.util.ClientHolder;
import com.zhaodanmu.persistence.api.PersistenceService;
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

    private RoomDetail roomDetail;

    private static PersistenceService persistenceService;
    //断线重连次数
    private AtomicInteger retryTimes = new AtomicInteger(0);
    //重试标志位
    private AtomicBoolean retrying = new AtomicBoolean(false);

    private static ThreadPoolExecutor reConnectThread = new ThreadPoolExecutor(1, 1,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<>());

    private MessageHandlerDispatcher messageHandlerDispatcher;

    private Connection connection;



    private Listener defaultListener = new Listener() {
        @Override
        public void onSuccess(Object... args) {

        }

        @Override
        public void onFailure(Throwable cause) {

        }
    };

    public DouyuCrawlerClient(String rid,PersistenceService persistenceService) {
        this.rid = rid;
        this.persistenceService = persistenceService;
        ClientHolder.hold(rid,this);
        this.initPersistence();
    }

    private void initPersistence() {
        //doStart handler
        messageHandlerDispatcher = new MessageHandlerDispatcher();
        //处理登录相关消息
        messageHandlerDispatcher.register("loginres|loginreq",new DouyuLoginMsgHandler(persistenceService));
        //弹幕聊天消息(chatmsg)
        messageHandlerDispatcher.register("chatmsg",new DouyuChatMsgHandler(persistenceService));
        // 赠送礼物消息(dgb)
        messageHandlerDispatcher.register("dgb",new DouyuGiveGiftsMsgHandler(persistenceService));
        //用户进房消息(uenter)
        messageHandlerDispatcher.register("uenter",new DouyuUEnterMsgHandler(persistenceService));
        // 赠送酬劳消息(bc_buy_deserve)
        //messageHandlerDispatcher.register("bc_buy_deserve",new DouyuDeserveMsgHandler(persistenceService));
        //被禁言消息（newblackres）
        messageHandlerDispatcher.register("newblackres",new NewBlackMsgHandler(persistenceService));

        messageHandlerDispatcher.register("def",new DouyuDefaultMsgHandler(persistenceService));
    }


    public void setRoomDetail(RoomDetail roomDetail) {
        this.roomDetail = roomDetail;
    }

    public RoomDetail getRoomDetail() {
        return roomDetail;
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
        return new DouyuConnClientChannelHandler(rid,messageHandlerDispatcher);
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

    /**
     * 同步阻塞启动客户端
     */
    public void sync() {
        if(!doStart(this.defaultListener)) {
            reConnect();
        }
    }

    public void release(Connection connection) {
        lock.lock();
        try {
            this.connection = connection;
            loginSuccessCondition.signal();
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
            Log.sysLogger.warn("trying reconnect conn-rid={}.",rid);
            while (!connect()) {
                retryTimes.incrementAndGet();
                Log.sysLogger.warn("trying reconnect conn-rid={}, retry times={}.",rid,retryTimes.get());
                Log.sysLogger.warn("reconnect conn-rid={} time out,wait a second...",rid);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
            Log.sysLogger.info("reconnect conn-rid={} success, retry times = {}.",rid,retryTimes.get());
            retrying.set(false);
            retryTimes.set(0);
        });
    }


    @Override
    public String toString() {
        return "DouyuCrawlerClient{" +
                "rid='" + rid + "'}";
    }
}
