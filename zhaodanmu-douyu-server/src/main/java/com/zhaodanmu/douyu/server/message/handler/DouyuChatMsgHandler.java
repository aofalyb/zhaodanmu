package com.zhaodanmu.douyu.server.message.handler;

import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.cache.JVMLruCache;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.api.PersistenceService;

import java.util.HashMap;

public class DouyuChatMsgHandler implements IMessageHandler<DouyuMessage> {

    private static JVMLruCache eventCache = JVMLruCache.getInstance();


    private static final String U_CHAT_RANK = "u_chat_rank";
    private static final String R_CHAT_RANK = "r_chat_rank";

    private PersistenceService persistenceService;
    public DouyuChatMsgHandler(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }


    @Override
    public boolean handle(Connection connection, DouyuMessage message) {

        //persistenceService.bufferedInsert(message.getData());


        return true;
    }

    class MapModel extends HashMap<String,String> {

    }

}
