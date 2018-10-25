package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.elasticsearch.model.ChatMessageModel;

import java.util.Map;

public class DouyuChatMsgHandler implements IMessageHandler<DouyuMessage> {
    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getAttributes();

        ChatMessageModel chatMessage = null;
        try {
            chatMessage = JSON.parseObject(JSON.toJSONString(attributes), ChatMessageModel.class);
            Log.defLogger.debug(chatMessage);
        } catch (Exception e) {
            Log.sysLogger.error("json serialize: {} ",attributes,e);
            return false;
        }
        //写入持久化


        return false;
    }
}
