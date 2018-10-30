package com.zhaodanmu.core.netty;

import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.util.NettySurportUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;


public abstract class NettyClient {

    private static final Logger LOGGER = Log.sysLogger;

    private EventLoopGroup workerGroup;
    protected Bootstrap bootstrap;

    private void createClient(Listener listener, EventLoopGroup workerGroup, ChannelFactory<? extends Channel> channelFactory) {
        this.workerGroup = workerGroup;
        this.bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)//
                .option(ChannelOption.SO_REUSEADDR, true)//
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//
                .channelFactory(channelFactory);
        bootstrap.handler(new ChannelInitializer<Channel>() { // (4)
            @Override
            public void initChannel(Channel ch) throws Exception {
                initPipeline(ch.pipeline());
            }
        });
        initOptions(bootstrap);
        listener.onSuccess();
    }

    public ChannelFuture connect(String host, int port) {
        return bootstrap.connect(new InetSocketAddress(host, port));
    }

    public ChannelFuture connect(String host, int port, Listener listener) {
        return bootstrap.connect(new InetSocketAddress(host, port)).addListener(f -> {
            if (f.isSuccess()) {
                if (listener != null) listener.onSuccess(port);
                LOGGER.info("start netty client success, host={}, port={}", host, port);
            } else {
                if (listener != null) listener.onFailure(f.cause());
                LOGGER.error("start netty client failure, host={}, port={}", host, port, f.cause());
            }
        });
    }

    private void createNioClient(Listener listener) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(
                getWorkThreadNum(), new DefaultThreadFactory("tcp-client"), getSelectorProvider()
        );
        workerGroup.setIoRatio(getIoRate());
        createClient(listener, workerGroup, getChannelFactory());
    }

    private void createEpollClient(Listener listener) {
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(
                getWorkThreadNum(), new DefaultThreadFactory("tcp-client")
        );
        workerGroup.setIoRatio(getIoRate());
        createClient(listener, workerGroup, EpollSocketChannel::new);
    }

    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    protected abstract ChannelHandler getDecoder();

    protected abstract ChannelHandler getEncoder();

    protected int getIoRate() {
        return 2;
    }

    protected int getWorkThreadNum() {
        return 1;
    }

    public abstract ChannelHandler getChannelHandler();

    protected boolean doStart(Listener listener) {
        if (NettySurportUtils.useNettyEpoll()) {
            createEpollClient(listener);
        } else {
            createNioClient(listener);
        }
        return true;
    }

    protected void doStop(Listener listener) throws Throwable {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        LOGGER.error("netty client [{}] stopped.", this.getClass().getSimpleName());
        listener.onSuccess();
    }

    protected void initOptions(Bootstrap b) {
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
        b.option(ChannelOption.TCP_NODELAY, true);
    }

    public ChannelFactory<? extends Channel> getChannelFactory() {
        return NioSocketChannel::new;
    }

    public SelectorProvider getSelectorProvider() {
        return SelectorProvider.provider();
    }


    @Override
    public String toString() {
        return "NettyClient{" +
                ", name=" + this.getClass().getSimpleName() +
                '}';
    }
}
