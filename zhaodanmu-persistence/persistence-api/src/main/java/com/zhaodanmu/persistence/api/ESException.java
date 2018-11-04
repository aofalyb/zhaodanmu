package com.zhaodanmu.persistence.api;

import com.zhaodanmu.common.MyRuntimeException;

public class ESException extends MyRuntimeException {

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
