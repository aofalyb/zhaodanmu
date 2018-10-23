package com.zhaodanmu.core.netty;


import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ConnectionTest {


    @Test
    public void hashedWheelTimer() {

        HashedWheelTimer wheelTimer = new HashedWheelTimer(1,TimeUnit.SECONDS,3);
        wheelTimer.start();
        addNewTask(wheelTimer);
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addNewTask( HashedWheelTimer wheelTimer) {
        wheelTimer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("---");
                addNewTask(wheelTimer);
            }
        },3,TimeUnit.SECONDS);
    }
}