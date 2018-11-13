package com.zhaodanmu.douyu.server;

import com.zhaodanmu.douyu.server.model.RecentlyEvent;
import com.zhaodanmu.douyu.server.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RecentlyMessagePool {

    private static BlockingDeque<RecentlyEvent> messageList = new LinkedBlockingDeque<>(100 * 10000);

    private static volatile boolean full = false;


    static {
        ThreadUtils.newThread("remv-recently", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(full) {


                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }


    public static void add(RecentlyEvent recentlyEvent) {
        if(!messageList.offerFirst(recentlyEvent)) {
        }
    }


    public static List<RecentlyEvent> get(String clientId,int len) {

        return null;
    }


}
