package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.douyu.server.model.UEnterModel;
import com.zhaodanmu.persistence.elasticsearch.EsClient;
import com.zhaodanmu.persistence.elasticsearch.model.DouyuESModel;

import java.util.Map;

public class DouyuUEnterMsgHandler implements IMessageHandler<DouyuMessage> {
    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getAttributes();

        DouyuESModel uEnterModel = null;
        try {
            uEnterModel = JSON.parseObject(JSON.toJSONString(attributes), DouyuESModel.class);
            Log.defLogger.debug(uEnterModel);
        } catch (Exception e) {
            Log.sysLogger.error("json serialize: {} ",attributes,e);
            return false;
        }
        //写入持久化
        EsClient.getInstance().asyncInsert(uEnterModel);

        return false;
    }
}
