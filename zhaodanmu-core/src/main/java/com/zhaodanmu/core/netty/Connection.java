package com.zhaodanmu.core.netty;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class Connection {

    public static final int HEARTBEAT_TIMEOUT = 15;
    private Channel channel;
    public volatile long lastReadTime;
    public volatile ConnectionState state = ConnectionState.NEW;
    //room id,primary key
    private String rid;

    public Connection(String rid) {
        this.rid = rid;
    }

    public void init(Channel channel) {
        this.channel = channel;
        lastReadTime = System.currentTimeMillis();
        state = ConnectionState.ACTIVE;
    }


    public boolean isConnected() {
        return state.equals(ConnectionState.CONNECTED);
    }

    public ChannelFuture close() {
        Log.sysLogger.info("closing conn: {}",this);
        state = ConnectionState.CLOSED;
        if(channel.isActive()) {
            return channel.close();
        }
       return null;
    }

    /**
     * 发送心跳包
     */
    public abstract void ping();


    public ChannelFuture send(Packet packet) {
        if(!isConnected()) {
            //TODO 【需要做限制吗？】
            //throw new NettyClientRuntimeException("can't send any data before connection connected");
        }
        if(channel.isActive()) {
            ChannelFuture future = channel.writeAndFlush(packet);
            if (channel.isWritable()) {
                return future;
            }
            //阻塞调用线程还是抛异常？
            //return channel.newPromise().setFailure(new RuntimeException("send data too busy"));
            if (!future.channel().eventLoop().inEventLoop()) {
                future.awaitUninterruptibly(100);
            }
            return future;
        } else {
            throw new NettyClientRuntimeException("can't send any data: channel is inactive now");
        }
    }




    public ConnectionState getState() {
        return state;
    }
    public Channel getChannel() {
        return channel;
    }
    public String getRid() {
        return rid;
    }


    @Override
    public String toString() {
        return "Connection{" +
                "channel=" + channel +
                ", lastReadTime=" + new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date(lastReadTime)) +
                ", state=" + state +
                ", rid='" + rid + '\'' +
                '}';
    }

}
