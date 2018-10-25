package com.zhaodanmu.douyu.server.message.handler;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.persistence.elasticsearch.model.ChatMessageModel;
import com.zhaodanmu.persistence.elasticsearch.model.GiveGiftsModel;

import java.util.Map;

public class DouyuGiveGiftsMsgHandler implements IMessageHandler<DouyuMessage> {
    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        Map<String, String> attributes = message.getAttributes();

        GiveGiftsModel giveGifts = null;
        try {
            giveGifts = JSON.parseObject(JSON.toJSONString(attributes), GiveGiftsModel.class);
            Log.defLogger.debug(giveGifts);
        } catch (Exception e) {
            Log.sysLogger.error("json serialize: {} ",attributes,e);
            return false;
        }
        //写入持久化

        return false;
    }
}
