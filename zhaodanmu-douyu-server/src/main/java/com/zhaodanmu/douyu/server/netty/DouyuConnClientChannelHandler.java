package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.netty.*;
import com.zhaodanmu.core.util.ClientHolder;
import com.zhaodanmu.douyu.server.DouyuCrawlerClient;
import com.zhaodanmu.douyu.server.message.DouyuLoginReqMessage;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.core.dispatcher.MessageHandlerDispatcher;
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

    public DouyuConnClientChannelHandler(String rid,ConnectionManager connectionManager,MessageHandlerDispatcher messageHandlerDispatcher) {
        this.rid = rid;
        this.connectionManager = connectionManager;
        this.messageHandlerDispatcher = messageHandlerDispatcher;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DouyuPacket packet = (DouyuPacket) msg;
        DouyuMessage douyuMessage = new DouyuMessage(packet,connection);
        messageHandlerDispatcher.dispatch(connection,douyuMessage);
        connection.refreshLastRead();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        connection = new DouyuConnection(rid);
        connection.init(ctx.channel());
        Log.sysLogger.info("channel:{} is active now, WELCOME.",ctx.channel());

        connectionManager.init();
        connectionManager.put(connection);
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
