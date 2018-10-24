package com.zhaodanmu.core.netty;

import io.netty.channel.Channel;

public interface ConnectionManager {

    void init();

    void put(Connection connection);

    void removeAndClose(Channel channel);

    void destroy();

}
