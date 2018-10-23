package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.netty.ConnectionState;
import com.zhaodanmu.core.netty.NettyClientException;
import com.zhaodanmu.douyu.server.message.DouyuLoginMessage;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.douyu.server.message.handler.DouyuDefaultMessageHandler;
import com.zhaodanmu.douyu.server.message.handler.DouyuLoginMessageHandler;
import com.zhaodanmu.douyu.server.message.handler.MessageHandlerDispatcher;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * 430489 276685 606118 99999 520 88188 5221750 911 71017 688
 */
@ChannelHandler.Sharable
public class DouyuConnClientChannelHandler extends ChannelInboundHandlerAdapter {

    private Connection connection;
    public String rid;

    private MessageHandlerDispatcher messageHandlerDispatcher;


    public DouyuConnClientChannelHandler(String rid) {
        this.rid = rid;
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

        if(connection == null) {
            connection = new DouyuConnection(ctx.channel(), rid);
        }
        //init handler
        messageHandlerDispatcher = new MessageHandlerDispatcher(connection);
        messageHandlerDispatcher.register("loginres|loginreq",new DouyuLoginMessageHandler());
        messageHandlerDispatcher.register("def",new DouyuDefaultMessageHandler());


        doLogin(connection);

    }

    //登录弹幕服务器
    private void doLogin(Connection connection) {
        new DouyuLoginMessage(null,connection)
                .send()
                .addListener((future -> {
                    if(future.isSuccess()) {
                        //connection.refreshState(Connection.ConnectionState.LOGIN_PRE);
                        Log.defLogger.info("trying login douyu-chat room:{}.",connection.getRid());
                    } else {
                        throw new NettyClientException("send login req fail,exp:{}.",future.cause());
                    }
                }));
    }





    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connection.state = ConnectionState.DISCONNECTED;
        Log.defLogger.error("disconnect from douyu-room:{}. try reconnect...", rid);
        Log.defLogger.error("ctx : {} ",ctx);
        //TODO 【断线重连】
//        NettyClient nettyClient = ClientManager.get(rid);
//        nettyClient.reLogin(connection);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.defLogger.error(ctx,cause);
        //TODO 【断线重连】
    }
}
