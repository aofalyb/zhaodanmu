package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.elasticsearch.EsClient;
import com.zhaodanmu.persistence.elasticsearch.model.DouyuESModel;
import com.zhaodanmu.persistence.elasticsearch.model.ULiveRoomModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DouyuChatMsgHandler implements IMessageHandler<DouyuMessage> {

    private Map<String,String> userInsertCache = new ConcurrentHashMap<>();


    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getAttributes();

        DouyuESModel chatMessage = null;
        ULiveRoomModel uLiveRoomModel = null;
        try {
            chatMessage = JSON.parseObject(JSON.toJSONString(attributes), DouyuESModel.class);
            uLiveRoomModel = JSON.parseObject(JSON.toJSONString(attributes), ULiveRoomModel.class);
        } catch (Exception e) {
            Log.sysLogger.error("json serialize: {} ",attributes,e);
            return false;
        }
        //es持久化
        EsClient.getInstance().bufferedInsert(chatMessage);
        String pk = userInsertCache.get(uLiveRoomModel.getPK());
        if(pk == null) {
            EsClient.getInstance().bufferedInsert(uLiveRoomModel);
            userInsertCache.put(uLiveRoomModel.getPK(),uLiveRoomModel.getPK());
        }
        return false;
    }
}
