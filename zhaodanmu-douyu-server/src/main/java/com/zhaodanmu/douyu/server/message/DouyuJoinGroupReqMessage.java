package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;

/**
 * 心跳消息
 */
public class DouyuJoinGroupReqMessage extends DouyuMessage {

    public DouyuJoinGroupReqMessage(Connection connection) {
        super(new DouyuPacket(("type@=joingroup/rid@=" + connection.getRid() + "/gid@=-9999/").getBytes()),connection);
    }

    public DouyuJoinGroupReqMessage(DouyuPacket packet, Connection connection) {
        super(packet,connection);
    }


    @Override
    public void encodeBody() {
        //packet = DouyuPacketBuilder.build(DouyuPacket.PACKET_TYPE_JOINGROUP,connection.getRid());
    }

}
