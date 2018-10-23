package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.Message;

public interface IMessageHandler<T extends Message> {

    boolean handle(Connection connection, T message);
}
