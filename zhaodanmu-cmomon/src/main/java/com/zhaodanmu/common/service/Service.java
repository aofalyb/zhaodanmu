package com.zhaodanmu.common.service;


import com.zhaodanmu.common.Listener;

import java.util.concurrent.CompletableFuture;

public interface Service {

    void start(Listener listener);

    void stop(Listener listener);

    CompletableFuture<Boolean> start();

    CompletableFuture<Boolean> stop();

    boolean syncStart();

    boolean syncStop();

    void init();

    boolean isRunning();

}
