package com.zhaodanmu.douyu.server.message;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;

/**
 * 心跳消息
 */
public class DouyuHeartbeatMessage extends DouyuMessage {


    private static final String HEART_BEAT_CMD = "type@=mrkl/";

    public DouyuHeartbeatMessage(Connection connection) {
        super(new DouyuPacket(HEART_BEAT_CMD.getBytes()),connection);
    }
    public DouyuHeartbeatMessage(DouyuPacket packet, Connection connection) {
        super(packet,connection);
    }


}
