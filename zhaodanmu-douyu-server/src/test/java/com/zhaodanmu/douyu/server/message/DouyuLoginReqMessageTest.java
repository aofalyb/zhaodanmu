package com.zhaodanmu.douyu.server.message;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.core.netty.NettyClientException;
import com.zhaodanmu.douyu.server.netty.DouyuConnection;
import org.junit.Test;

public class DouyuLoginReqMessageTest {


    @Test
    public void encode() {
        Connection connection = new DouyuConnection("8888");
        new DouyuLoginReqMessage(connection)
                .send()
                .addListener((future -> {
                    if(future.isSuccess()) {
                        //connection.refreshState(Connection.ConnectionState.LOGIN_PRE);
                        Log.sysLogger.info("trying login chat room:{}.",connection.getRid());
                    } else {
                        throw new NettyClientException("send login req fail,exp:{}.",future.cause());
                    }
                }));
    }
}