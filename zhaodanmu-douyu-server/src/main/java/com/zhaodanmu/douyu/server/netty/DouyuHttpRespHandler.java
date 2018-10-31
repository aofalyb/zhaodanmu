package com.zhaodanmu.douyu.server.netty;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.common.Result;
import com.zhaodanmu.core.dispatcher.ControllerDispatcher;
import com.zhaodanmu.core.dispatcher.MappingResult;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
/*
ByteBuf content = ((DefaultLastHttpContent) msg).content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        new String(bytes);*/

public class DouyuHttpRespHandler extends ChannelInboundHandlerAdapter {


    private ControllerDispatcher controllerDispatcher;

    public DouyuHttpRespHandler(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            URI uri = URI.valueOf(httpRequest.uri());
            String body = null;
            if(httpRequest.method().equals(HttpMethod.POST)) {
                ByteBuf contentByteBuf = httpRequest.content();
                byte[] bytes = new byte[contentByteBuf.readableBytes()];
                contentByteBuf.readBytes(bytes);
                ReferenceCountUtil.release(contentByteBuf);
                body = new String(bytes,"utf-8");
            }

            Log.httpLogger.debug("rev http request,method: {},uri: {},body: {},client:{}",httpRequest.method(),httpRequest.uri(),body,ctx.channel().remoteAddress());
            Result result = controllerDispatcher.dispatch(uri, body);
            Response resp = Response.from(result);
            String str = JSON.toJSONString(resp);
            ByteBuf respByte = Unpooled.buffer(str.length());
            respByte.writeBytes(str.getBytes());
            HttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK, respByte);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.httpLogger.error("http server exception caught,chanel: {}",ctx.channel(),cause);
        ctx.channel().close();
    }



    private static class Response<T> {

        private T data;
        /**
         * success default
         */
        private int code = 0;

        private String descZh = "success";


        public Response(T data) {
            this.data = data;
        }

        public Response(int code, String descZh) {
            this.code = code;
            this.descZh = descZh;
        }

        public Response(T data, String descZh) {
            this.data = data;
            this.descZh = descZh;
        }

        public static Response from(Result result) {
            if(result.success()) {
                return new Response(result.getData());
            } else {
                return new Response(-1,result.getDesc());
            }

        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescZh() {
            return descZh;
        }

        public void setDescZh(String descZh) {
            this.descZh = descZh;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "data=" + data +
                    ", code=" + code +
                    ", descZh='" + descZh + '\'' +
                    '}';
        }

    }
}
