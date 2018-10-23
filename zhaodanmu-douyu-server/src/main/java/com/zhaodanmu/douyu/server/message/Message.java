package com.zhaodanmu.douyu.server.message;


import io.netty.channel.ChannelFuture;


public interface Message {

    ChannelFuture send();

    Message decode();

    void encode();

    String getMessageType();

}
