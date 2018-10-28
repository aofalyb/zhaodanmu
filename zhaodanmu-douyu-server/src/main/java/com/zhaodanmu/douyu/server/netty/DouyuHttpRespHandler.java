package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.core.common.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.http.HttpVersion;


public class DouyuHttpRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            Log.httpLogger.debug("rev http request,method: {},uri: {},client:{}",httpRequest.method(),httpRequest.uri(),ctx.channel().remoteAddress());
            String str = "hello server.";
            ByteBuf resp = Unpooled.buffer(str.length());
            resp.writeBytes(str.getBytes());
            HttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK, resp);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isDone()) {
                        future.channel().close();
                    }
                }
            });
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.httpLogger.error("http server exception caught,chanel: {}",ctx.channel(),cause);
        ctx.channel().close();
    }
}
