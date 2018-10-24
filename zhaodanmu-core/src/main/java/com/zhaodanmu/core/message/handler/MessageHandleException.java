package com.zhaodanmu.core.message.handler;

public class MessageHandleException extends RuntimeException {


    public MessageHandleException() {
        super();
    }

    public MessageHandleException(String message) {
        super(message);
    }

    public MessageHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageHandleException(Throwable cause) {
        super(cause);
    }

    protected MessageHandleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
