package com.zhaodanmu.douyu.server.util;



import com.zhaodanmu.common.utils.NamedThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Administrator on 2018/11/9.
 */
public class ThreadUtils {
    private ThreadUtils (){}

    private static ScheduledThreadPoolExecutor timerUpdateRoomThread = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("timer-room-update"));

    private static NamedThreadFactory namedThreadFactory = new NamedThreadFactory("util-");

    public static ScheduledThreadPoolExecutor getScheduledThread() {
        return timerUpdateRoomThread;
    }

    public static Thread newThread(String name,Runnable runnable) {
        return namedThreadFactory.newThread(name,runnable);
    }
}
