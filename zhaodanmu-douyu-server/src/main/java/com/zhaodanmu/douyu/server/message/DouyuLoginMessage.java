package com.zhaodanmu.douyu.server.message;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import com.zhaodanmu.douyu.server.util.DouyuPacketBuilder;

/**
 * 心跳消息
 */
public class DouyuLoginMessage extends DouyuMessage {

    public static final int LOGIN_TIME_OUT = 5 * 1000;

    public DouyuLoginMessage(DouyuPacket packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    public void encode() {
        packet = DouyuPacketBuilder.build(DouyuPacket.PACKET_TYPE_LOGIN, connection.getRid());
    }

}
