package com.zhaodanmu.core.message;

import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.protocol.Packet;
import io.netty.channel.ChannelFuture;


public abstract class BaseMessage implements Message {

    protected Packet packet;
    protected Connection connection;


    public BaseMessage(Packet packet, Connection connection) {
        this.packet = packet;
        this.connection = connection;
    }

    @Override
    public Packet getPacket() {
        return packet;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public ChannelFuture send() {
        encodeBody();
        return connection.send(packet);
    }
}
