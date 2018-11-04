package com.zhaodanmu.douyu.server.message.handler;

import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;

import java.util.Map;

public class DouyuUEnterMsgHandler implements IMessageHandler<DouyuMessage> {
    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getData();

        //写入持久化
        //EsClient.getInstance().bufferedInsert(uEnterModel);

        return false;
    }
}
