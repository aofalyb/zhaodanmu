package com.zhaodanmu.common;

public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);

}