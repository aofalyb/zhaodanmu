package com.zhaodanmu.core.util;

import com.zhaodanmu.common.utils.Log;

public class NettySurportUtils {

    /**
     * nio or epoll?
     * @return
     */
    public static boolean useNettyEpoll() {
        try {
            Class.forName("io.netty.channel.epoll.Native");
            Log.sysLogger.warn("###### netty epoll is available,use [epoll] now ######");
            return true;
        } catch (Throwable error) {
            Log.sysLogger.warn("#### can not load netty epoll,switch [nio] model #####");
        }
        return false;
    }
}
