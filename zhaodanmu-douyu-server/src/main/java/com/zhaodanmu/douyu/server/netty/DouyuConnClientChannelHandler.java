package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.event.ConnectEvent;
import com.zhaodanmu.core.event.EventBus;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.douyu.server.message.handler.DouyuDefaultMessageHandler;
import com.zhaodanmu.douyu.server.message.handler.DouyuLoginMessageHandler;
import com.zhaodanmu.core.message.handler.MessageHandlerDispatcher;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * 430489 276685 606118 99999 520 88188 5221750 911 71017 688
 */
public class DouyuConnClientChannelHandler extends ChannelInboundHandlerAdapter {

    private Connection connection;
    public String rid;
    private MessageHandlerDispatcher messageHandlerDispatcher;

    private ConnectionManager connectionManager;

    public DouyuConnClientChannelHandler(String rid) {
        this.rid = rid;
        connectionManager = new ClientConnectionManager();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        DouyuPacket packet = (DouyuPacket) msg;
        DouyuMessage douyuMessage = new DouyuMessage(packet,connection);
        messageHandlerDispatcher.dispatch(douyuMessage);
        connection.lastReadTime = System.currentTimeMillis();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        connection = new DouyuConnection(rid);
        connection.init(ctx.channel());
        connectionManager.init();
        connectionManager.put(connection);

        //init handler
        messageHandlerDispatcher = new MessageHandlerDispatcher(connection);
        messageHandlerDispatcher.register("loginres|loginreq",new DouyuLoginMessageHandler());
        messageHandlerDispatcher.register("def",new DouyuDefaultMessageHandler());
        tryLogin(connection);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionManager.removeAndClose(ctx.channel());
        Log.defLogger.error("channel is inactive now, try reconnect.");
        Log.defLogger.error("connection:{} ",connection);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.defLogger.error("exception caught! connection: {}",connection,cause);

    }


    //登录弹幕服务器
    private void tryLogin(Connection connection) {
        new DouyuLoginReqMessage(connection)
                .send()
                .addListener((future -> {
                    if(future.isSuccess()) {
                        //connection.refreshState(Connection.ConnectionState.LOGIN_PRE);
                        Log.defLogger.info("trying login chat room:{}.",connection.getRid());
                    } else {
                        throw new NettyClientException("send login req fail,exp:{}.",future.cause());
                    }
                }));
    }
}
