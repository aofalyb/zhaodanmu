package com.zhaodanmu.core.netty;


public interface ConnectionManager {

    void init();

    void put(Connection connection);

    void removeAndClose(Connection connection);

    void destroy();

    Connection get(String id);

}
