package com.zhaodanmu.core.event;

import com.zhaodanmu.core.netty.Connection;

public class ConnectEvent implements Event {
    private Connection connection;

    public ConnectEvent(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "conn {" + connection.toString() + "}";
    }
}
