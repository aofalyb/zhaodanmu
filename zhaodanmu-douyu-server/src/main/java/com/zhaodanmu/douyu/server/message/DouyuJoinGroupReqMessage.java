package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.netty.Connection;

/**
 * 心跳消息
 */
public class DouyuJoinGroupReqMessage extends DouyuMessage {


    private final String jionGroupReq;

    public DouyuJoinGroupReqMessage(Connection connection) {
        super(connection);
        jionGroupReq = "type@=joingroup/rid@=" + connection.getRid() + "/gid@=-9999/";
    }


    @Override
    public void encode() {
        //packet = DouyuPacketBuilder.build(DouyuPacket.PACKET_TYPE_JOINGROUP,connection.getRid());
    }

}
