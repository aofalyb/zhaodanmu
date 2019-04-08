package com.zhaodanmu.douyu.server.message.handler;

import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;

public class NewBlackMsgHandler implements IMessageHandler<DouyuMessage> {

    private PersistenceService persistenceService;

    public NewBlackMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        return true;
    }
}
