package com.zhaodanmu.douyu.server.netty;

import com.zhaodanmu.core.netty.Connection;
import io.netty.channel.Channel;

public class DouyuConnection extends Connection {

    public DouyuConnection(String rid) {
        super(rid);
    }

    @Override
    public void ping() {

    }
}
