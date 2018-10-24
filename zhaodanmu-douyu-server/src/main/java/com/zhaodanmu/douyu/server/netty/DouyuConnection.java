package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.message.DouyuHeartbeatMessage;

public class DouyuConnection extends Connection {

    public DouyuConnection(String rid) {
        super(rid);
    }

    @Override
    public void ping() {

        new DouyuHeartbeatMessage(this)
                .send()
                .addListener((future -> {
                    if(!future.isSuccess()) {
                        Log.defLogger.error("ping rid: {}",this.getRid(),future.cause());
                    }
                    Log.defLogger.debug("ping rid: {}",this.getRid());
                }));
    }
}
