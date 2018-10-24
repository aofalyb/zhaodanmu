package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.protocol.Packet;
import io.netty.channel.ChannelFuture;


public abstract class BaseMessage implements Message {

    protected Packet packet;

    protected Connection connection;

    public BaseMessage(Connection connection) {
        this.connection = connection;
    }

    public BaseMessage(Packet packet, Connection connection) {
        this.packet = packet;
        this.connection = connection;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    @Override
    public ChannelFuture send() {
        encode();

        return connection.send(packet);
    }
}
