package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.protocol.Packet;
import io.netty.channel.ChannelFuture;


public abstract class BaseMessage<T extends Packet>  implements Message {

    protected T packet;

    protected Connection connection;

    public BaseMessage(T packet, Connection connection) {
        this.packet = packet;
        this.connection = connection;
    }


    @Override
    public ChannelFuture send() {
        encode();

        return connection.send(packet);
    }
}
