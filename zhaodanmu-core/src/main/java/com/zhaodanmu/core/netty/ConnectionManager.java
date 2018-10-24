package com.zhaodanmu.core.netty;

import io.netty.channel.Channel;

public interface ConnectionManager {

    void init();

    void put(Connection connection);

    void removeAndClose(Connection connection);

    void destroy();

    Connection get(String id);

}
