package com.zhaodanmu.douyu.server.message;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import com.zhaodanmu.douyu.server.util.DouyuPacketBuilder;

/**
 * 由客户端主动发起的登录请求消息
 */
public class DouyuLoginReqMessage extends DouyuMessage {

    public static final int LOGIN_TIME_OUT = 5 * 1000;
    private final String loginReq;
    public DouyuLoginReqMessage(Connection connection) {
        super(connection);
        loginReq = "type@=loginreq/roomid@=" + connection.getRid() + "/";
        packet = new DouyuPacket(loginReq.getBytes());
    }

    @Override
    public void encode() {
        //packet = DouyuPacketBuilder.build(DouyuPacket.PACKET_TYPE_LOGIN, connection.getRid());
    }

}
