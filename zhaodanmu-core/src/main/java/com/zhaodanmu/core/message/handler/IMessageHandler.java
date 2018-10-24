package com.zhaodanmu.core.message.handler;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.message.Message;

public interface IMessageHandler<T extends Message> {

    boolean handle(Connection connection, T message);
}
