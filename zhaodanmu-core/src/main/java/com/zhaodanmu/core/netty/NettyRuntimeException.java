package com.zhaodanmu.core.netty;

public class NettyRuntimeException extends RuntimeException {
    public NettyRuntimeException() {
    }

    public NettyRuntimeException(String message) {
        super(message);
    }

    public NettyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyRuntimeException(Throwable cause) {
        super(cause);
    }

    public NettyRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
