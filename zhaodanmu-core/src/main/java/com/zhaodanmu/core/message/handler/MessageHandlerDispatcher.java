package com.zhaodanmu.core.message.handler;


import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息处理器分发
 */
public class MessageHandlerDispatcher {

    private static final String OR = "\\|";
    private Map<String,IMessageHandler> handlerMap;

    private AtomicInteger totalDispatch = new AtomicInteger(0);
    private long dispatchStartTime = -1L;
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
        Message decodeMessage = message.decodeBody();
        echoTPS();
        IMessageHandler handler = getHandler(decodeMessage.getMessageType());
        if(handler == null) {
            handler = getDefaultHandler();
        }
        //TODO 这里需要细化到具体的message类型

        return handler.handle(connection,decodeMessage);
    }

    /**
     * TPS
     */
    private void echoTPS() {
        int totalDispatchInt = totalDispatch.incrementAndGet();
        if(dispatchStartTime == -1L) {
            dispatchStartTime = System.currentTimeMillis();
        }
        long nowTime = System.currentTimeMillis();
        long secondPassed = (nowTime - dispatchStartTime) / 1000;
        if(secondPassed >= 1) {
            Log.defLogger.debug("conn-rid: {},tps: {}",connection.getRid(),totalDispatchInt / secondPassed);
        }
    }






}
