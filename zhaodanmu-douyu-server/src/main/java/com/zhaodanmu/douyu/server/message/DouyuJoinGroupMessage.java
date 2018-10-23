package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import com.zhaodanmu.douyu.server.util.DouyuPacketBuilder;

/**
 * 心跳消息
 */
public class DouyuJoinGroupMessage extends DouyuMessage {


    public DouyuJoinGroupMessage(DouyuPacket packet, Connection connection) {
        super(packet, connection);
    }


    @Override
    public void encode() {
        packet = DouyuPacketBuilder.build(DouyuPacket.PACKET_TYPE_JOINGROUP,connection.getRid());
    }

}
