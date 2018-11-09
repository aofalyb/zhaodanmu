package com.zhaodanmu.douyu.server.util;

import org.apache.lucene.util.NamedThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Administrator on 2018/11/9.
 */
public class ThreadUtils {
    private ThreadUtils (){}

    private static ScheduledThreadPoolExecutor timerUpdateRoomThread = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("timer-room-update"));


    public static ScheduledThreadPoolExecutor getScheduledThread() {
        return timerUpdateRoomThread;
    }
}
