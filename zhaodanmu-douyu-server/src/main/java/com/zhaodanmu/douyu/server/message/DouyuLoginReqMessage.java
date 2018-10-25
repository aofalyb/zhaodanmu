package com.zhaodanmu.douyu.server.message;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.protocol.Packet;
import com.zhaodanmu.douyu.server.protocol.DouyuPacket;
import com.zhaodanmu.douyu.server.util.DouyuPacketBuilder;

/**
 * 由客户端主动发起的登录请求消息
 */
public class DouyuLoginReqMessage extends DouyuMessage {

    public static final int LOGIN_TIME_OUT = 5 * 1000;
    public DouyuLoginReqMessage(Connection connection) {
        super(new DouyuPacket(("type@=loginreq/roomid@=" + connection.getRid() + "/").getBytes()),connection);
    }

    public DouyuLoginReqMessage(DouyuPacket packet,Connection connection) {
        super(packet,connection);
    }

    @Override
    public void encodeBody() {
        //packet = DouyuPacketBuilder.build(DouyuPacket.PACKET_TYPE_LOGIN, connection.getRid());
    }

}
