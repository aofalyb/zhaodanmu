package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.message.handler.IMessageHandler;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.netty.ConnectionState;
import com.zhaodanmu.douyu.server.DouyuCrawlerClient;
import com.zhaodanmu.douyu.server.message.DouyuJoinGroupReqMessage;
import com.zhaodanmu.douyu.server.message.DouyuMessage;
import com.zhaodanmu.core.util.ClientHolder;

import java.util.Objects;

public class DouyuLoginMsgHandler implements IMessageHandler<DouyuMessage> {


    @Override
    public boolean handle(Connection connection, DouyuMessage message) {

        if(Objects.equals(message.getMessageType(),"loginres")) {
            //login success then join group
            joinGroup(connection);
        }
        return false;
    }



    private void joinGroup(Connection connection) {
        new DouyuJoinGroupReqMessage(connection)
                .send()
                .addListener((future -> {
                    if(future.isSuccess()) {
                        connection.state = ConnectionState.CONNECTED;
                        Log.sysLogger.info("login douyu.properties-chat room: {} success.",connection.getRid());
                        DouyuCrawlerClient nettyClient = (DouyuCrawlerClient) ClientHolder.get(connection.getRid());
                        nettyClient.release();
                    } else {
                     //do nothing, wait lock time out
                    }
                }));

    }

}
