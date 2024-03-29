package com.zhaodanmu.core.netty;

import com.zhaodanmu.common.thread.NamedPoolThreadFactory;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ClientConnectionManager implements ConnectionManager {

    private static HashedWheelTimer wheelTimer ;

    private static Map<String,Connection> connections = new ConcurrentHashMap<>();

    @Override
    public void init() {
        wheelTimer = new HashedWheelTimer(new NamedPoolThreadFactory("conn-heartbeat"),1,TimeUnit.SECONDS,Connection.HEARTBEAT_TIMEOUT);
        wheelTimer.start();
    }

    @Override
    public void put(Connection connection) {
        connections.put(connection.getRid(),connection);
        wheelTimer.newTimeout(new HeartBeatTask(connection),Connection.HEARTBEAT_TIMEOUT,TimeUnit.SECONDS);
    }

    @Override
    public void removeAndClose(Connection connection) {
        connections.remove(connection.getRid());
        if(connection != null) {
            connection.state = ConnectionState.CLOSED;
            connection.close();
        }
    }

    @Override
    public void destroy() {
        Collection<Connection> values = connections.values();
        values.forEach(Connection::close);
    }

    @Override
    public Connection get(String id) {
        return connections.get(id);
    }


    class HeartBeatTask implements TimerTask {
        private Connection connection;

        void ping() {
            if(connection.isConnected()) {
                connection.ping();
                wheelTimer.newTimeout(new HeartBeatTask(connection),Connection.HEARTBEAT_TIMEOUT,TimeUnit.SECONDS);
            }
        }

        public HeartBeatTask(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run(Timeout timeout) throws Exception{
            ping();
        }

    }
}
