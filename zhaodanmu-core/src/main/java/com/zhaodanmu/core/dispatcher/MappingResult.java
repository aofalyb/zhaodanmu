package com.zhaodanmu.core.dispatcher;

import com.zhaodanmu.core.common.Result;

/**
 * Created by Administrator on 2018/10/31.
 */
public class MappingResult implements Result {

    private Object data;

    private boolean success = true;

    private Throwable throwable;

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Throwable failure() {
        return throwable;
    }

    public MappingResult(boolean success ,Object data, Throwable throwable) {
        this.data = data;
        this.success = success;
        this.throwable = throwable;
    }

    public MappingResult(Object data) {
        this.data = data;
        this.success = true;
    }

    public MappingResult(Throwable throwable) {
        this.throwable = throwable;
        this.success = false;
    }
}
