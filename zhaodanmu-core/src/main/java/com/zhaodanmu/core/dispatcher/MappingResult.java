package com.zhaodanmu.core.dispatcher;

import com.zhaodanmu.core.common.Result;

/**
 * Created by Administrator on 2018/10/31.
 */
public class MappingResult implements Result {

    private Object data;

    private boolean success = true;

    private Throwable throwable;

    private String descZh;

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

    @Override
    public String getDesc() {
        if(descZh == null && failure() != null) {
            return failure().getMessage();
        }
        return descZh;
    }

    public MappingResult(boolean success , Object data,String descZh, Throwable throwable) {
        this.data = data;
        this.success = success;
        this.descZh = descZh;
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
