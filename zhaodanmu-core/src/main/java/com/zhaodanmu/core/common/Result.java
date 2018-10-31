package com.zhaodanmu.core.common;

/**
 * Created by Administrator on 2018/10/31.
 */
public interface Result {

    boolean success();

    Object getData();

    Throwable failure();

}
