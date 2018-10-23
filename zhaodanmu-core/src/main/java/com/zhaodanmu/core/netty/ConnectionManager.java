package com.zhaodanmu.core.netty;

public interface ConnectionManager {

    void init();

    void put(Connection connection);

    void destroy();

}
