package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.EsClient;
import com.zhaodanmu.persistence.elasticsearch.model.DouyuESModel;

import java.util.Map;

public class DouyuDeserveMsgHandler implements IMessageHandler<DouyuMessage> {
    private PersistenceService persistenceService;
    public DouyuDeserveMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getAttributes();

        DouyuESModel deserve = null;
        try {
            deserve = JSON.parseObject(JSON.toJSONString(attributes), DouyuESModel.class);
            Log.defLogger.debug(deserve);
        } catch (Exception e) {
            Log.sysLogger.error("json serialize: {} ",attributes,e);
            return false;
        }
        //写入持久化
        persistenceService.bufferedInsert(deserve);
        return false;
    }
}
