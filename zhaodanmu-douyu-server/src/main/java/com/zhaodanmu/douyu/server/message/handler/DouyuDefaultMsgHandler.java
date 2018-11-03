package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;

import java.util.Map;

/**
 * 默认的消息处理器，当有消息未注册处理器时，使用默认处理器处理
 */
public class DouyuDefaultMsgHandler implements IMessageHandler<DouyuMessage> {

    public DouyuDefaultMsgHandler(PersistenceService persistenceService) {

    }

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getData();
        Log.defLogger.debug("un-handled message: {}",attributes);
        return false;
    }

}
