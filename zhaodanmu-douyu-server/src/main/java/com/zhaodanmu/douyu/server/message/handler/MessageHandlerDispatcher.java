package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理器分发
 */
public class MessageHandlerDispatcher {

    private static final String OR = "\\|";
    private Map<String,IMessageHandler> handlerMap;

    private Connection connection;


    public MessageHandlerDispatcher(Connection connection) {
        handlerMap = new HashMap<>();
        this.connection = connection;
    }

    public void register(String key, IMessageHandler messageHandler) {
        if(key.contains("|")) {
            String[] keys = key.split(OR);
            for (String singleKey: keys) {
                handlerMap.put(singleKey,messageHandler);
            }
        } else {
            handlerMap.put(key,messageHandler);
        }
    }


    public IMessageHandler getHandler(String key) {
        return handlerMap.get(key);
    }

    public IMessageHandler getDefaultHandler() {
        return getHandler("def");
    }

    public boolean dispatch(Message message) {
        Message decodeMessage = message.decode();

        IMessageHandler handler = getHandler(decodeMessage.getMessageType());
        if(handler == null) {
            handler = getDefaultHandler();
        }
        //TODO 这里需要细化到具体的message类型



        return handler.handle(connection,decodeMessage);
    }







}
