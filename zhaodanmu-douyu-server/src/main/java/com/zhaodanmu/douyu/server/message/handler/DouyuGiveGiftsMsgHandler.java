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

public class DouyuGiveGiftsMsgHandler implements IMessageHandler<DouyuMessage> {

    private PersistenceService persistenceService;
    public DouyuGiveGiftsMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getAttributes();

        DouyuESModel giveGifts = null;
        try {
            giveGifts = JSON.parseObject(JSON.toJSONString(attributes), DouyuESModel.class);
            Log.defLogger.debug(giveGifts);
        } catch (Exception e) {
            Log.sysLogger.error("json serialize: {} ",attributes,e);
            return false;
        }
        //写入持久化
        persistenceService.bufferedInsert(giveGifts);
        return false;
    }
}
