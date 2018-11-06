package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.model.NewBlackresModel;

import java.util.Map;

public class NewBlackMsgHandler implements IMessageHandler<DouyuMessage> {

    private PersistenceService persistenceService;

    public NewBlackMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getData();

        NewBlackresModel newBlackresModel = JSON.parseObject(JSON.toJSONString(attributes), NewBlackresModel.class);
        //写入持久化
        persistenceService.bufferedInsert(newBlackresModel);
        return true;
    }
}
