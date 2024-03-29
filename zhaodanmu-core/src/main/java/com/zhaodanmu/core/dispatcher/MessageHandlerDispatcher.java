package com.zhaodanmu.core.dispatcher;


import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息处理器分发
 */
public class MessageHandlerDispatcher<T extends Message> {

    private static final String OR = "\\|";
    private Map<String,IMessageHandler> handlerMap = new HashMap<>();

    private static AtomicInteger totalDispatch = new AtomicInteger(0);
    private long dispatchStartTime = -1L;

    public void register(String key, IMessageHandler messageHandler) {
        Log.sysLogger.info("register message handler: [{}] -> {}",key,messageHandler.getClass().getName());
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

    public boolean dispatch(Connection connection,T message) {
        Message decodeMessage = message.decodeBody();
        //data DataAnalysis

        //echoTPS();
        IMessageHandler handler = getHandler(decodeMessage.getMessageType());
        if(handler == null) {
            handler = getDefaultHandler();
        }
        long _s = System.currentTimeMillis();
        boolean success = handler.handle(connection, decodeMessage);
        long _e = System.currentTimeMillis();

        Log.sysLogger.debug("handle cost time: {} ms,thread:{}",(_e - _s),Thread.currentThread().getName());

        return success;
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
        //5s打印一次
        if(secondPassed >= 1 && secondPassed % 5 == 0) {
            Log.sysLogger.debug("tps info: {duration: {} s,total: {},tps: {}}",secondPassed,totalDispatchInt,totalDispatchInt / secondPassed);
        }
    }






}
