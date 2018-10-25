package com.zhaodanmu.persistence.elasticsearch;

public class ESException extends RuntimeException {

    public ESException() {
    }

    public ESException(String message) {
        super(message);
    }

    public ESException(String message, Throwable cause) {
        super(message, cause);
    }

    public ESException(Throwable cause) {
        super(cause);
    }

    public ESException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
