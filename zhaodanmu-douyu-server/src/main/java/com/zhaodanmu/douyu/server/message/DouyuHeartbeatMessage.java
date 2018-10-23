package com.zhaodanmu.douyu.server.message;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;

/**
 * 心跳消息
 */
public class DouyuHeartbeatMessage extends DouyuMessage {


    private static final String HEART_BEAT_CMD = "type@=mrkl/";

    public DouyuHeartbeatMessage(DouyuPacket packet, Connection connection) {
        super(packet,connection);
    }

    @Override
    public void encode() {
        packet = new DouyuPacket(HEART_BEAT_CMD.getBytes());
    }
}
