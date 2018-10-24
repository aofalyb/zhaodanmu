package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.netty.ConnectionState;
import com.zhaodanmu.douyu.server.message.DouyuJoinGroupReqMessage;
import com.zhaodanmu.douyu.server.message.DouyuMessage;

import java.util.Objects;

public class DouyuLoginMessageHandler implements IMessageHandler<DouyuMessage> {


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
                        Log.defLogger.info("login douyu-chat room:{} success.",connection.getRid());
//                        connection.hearBeat();
//                        DouyuNettyClient.notifyLoginSuccess();
                    } else {
                     //do nothing, wait lock time out
                    }
                }));

    }

}