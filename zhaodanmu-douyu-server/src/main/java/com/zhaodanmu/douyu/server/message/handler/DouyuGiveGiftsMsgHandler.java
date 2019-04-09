package com.zhaodanmu.douyu.server.message.handler;

import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;


public class DouyuGiveGiftsMsgHandler implements IMessageHandler<DouyuMessage> {

    private static final String U_GIFT_RANK = "u_gift_rank";

    private PersistenceService persistenceService;
    public DouyuGiveGiftsMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        return true;
    }
}
