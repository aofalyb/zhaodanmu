package com.zhaodanmu.core.common;

public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);

}