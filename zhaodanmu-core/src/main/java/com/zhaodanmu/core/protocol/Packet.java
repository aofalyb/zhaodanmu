package com.zhaodanmu.core.protocol;

import io.netty.buffer.ByteBuf;

/**
 * 基于netty的协议包
 */
public interface Packet {

     void encode(ByteBuf out);

     void decode(ByteBuf byteBuf, int length);

     byte[] getBody();
}
