package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;

/**
 * 默认的消息处理器，当有消息未注册处理器时，使用默认处理器处理
 */
public class DouyuDefaultMsgHandler implements IMessageHandler<DouyuMessage> {
    PersistenceService persistenceService;
    public DouyuDefaultMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
//        persistenceService.bufferedInsert(message.getData());
        return false;
    }
}
