package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.common.Log;

public class Main {

    public static void main(String[] args) {

        DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient("99999");
        douyuCrawlerClient.doStart();
        DouyuCrawlerClient douyuCrawlerClient2 = new DouyuCrawlerClient("688");
        douyuCrawlerClient2.doStart();
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
