

package com.zhaodanmu.douyu.server.netty.codec;

import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public final class DouyuPacketEncoder extends MessageToByteEncoder<DouyuPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, DouyuPacket packet, ByteBuf out) throws Exception {
        packet.encode(out);
    }
}
