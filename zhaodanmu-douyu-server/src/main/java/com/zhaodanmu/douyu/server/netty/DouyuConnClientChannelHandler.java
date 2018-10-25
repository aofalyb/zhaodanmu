package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.core.util.ClientHolder;
import com.zhaodanmu.douyu.server.DouyuCrawlerClient;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.douyu.server.message.handler.*;
import com.zhaodanmu.core.message.handler.MessageHandlerDispatcher;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
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

    public DouyuConnClientChannelHandler(String rid,ConnectionManager connectionManager) {
        this.rid = rid;
        this.connectionManager = connectionManager;
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
        Log.sysLogger.info("channel:{} is active now, WELCOME.",ctx.channel());

        connectionManager.init();
        connectionManager.put(connection);

        //doStart handler
        messageHandlerDispatcher = new MessageHandlerDispatcher(connection);
        //处理登录相关消息
        messageHandlerDispatcher.register("loginres|loginreq",new DouyuLoginMsgHandler());
        //弹幕聊天消息(chatmsg)
        messageHandlerDispatcher.register("chatmsg",new DouyuChatMsgHandler());
        // 赠送礼物消息(dgb)
        messageHandlerDispatcher.register("dgb",new DouyuGiveGiftsMsgHandler());
        //TODO 抢到道具消息(gpbc)
        //用户进房消息(uenter)
        messageHandlerDispatcher.register("uenter",new DouyuUEnterMsgHandler());
        // 赠送酬劳消息(bc_buy_deserve)
        messageHandlerDispatcher.register("bc_buy_deserve",new DouyuDeserveMsgHandler());

        messageHandlerDispatcher.register("def",new DouyuDefaultMsgHandler());
        tryLogin(connection);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionManager.removeAndClose(connection);
        Log.sysLogger.info("channel:{} is inactive now, try reconnect.",ctx.channel());
        DouyuCrawlerClient nettyClient = (DouyuCrawlerClient) ClientHolder.get(connection.getRid());
        nettyClient.reConnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.sysLogger.error("exception caught! connection: {}",connection,cause);

    }



    //登录弹幕服务器
    private void tryLogin(Connection connection) {
        new DouyuLoginReqMessage(connection)
                .send()
                .addListener((future -> {
                    if(future.isSuccess()) {
                        //connection.refreshState(Connection.ConnectionState.LOGIN_PRE);
                        Log.sysLogger.info("trying login chat room:{}.",connection.getRid());
                    } else {
                        throw new NettyClientException("send login req fail,exp:{}.",future.cause());
                    }
                }));
    }
}
