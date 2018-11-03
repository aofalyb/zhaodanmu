package com.zhaodanmu.core.message;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.protocol.Packet;
import io.netty.channel.ChannelFuture;


public interface Message<T> {

    ChannelFuture send();

    Message decodeBody();

    void encodeBody();

    String getMessageType();

    Packet getPacket();

    Connection getConnection();

    T getData();
}
