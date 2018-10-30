package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.common.utils.PropertiesUtil;
import com.zhaodanmu.persistence.elasticsearch.EsClient;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {

        System.setProperty("io.netty.noUnsafe","false");
        String[] roomsArray = CC.rooms;
        //EsClient.getInstance().init(CC.esHost,CC.esPort);

        for (int i = 0; i < roomsArray.length; i++) {
            DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient(roomsArray[i]);
            douyuCrawlerClient.sync();
        }

        DouyuHttpServer douyuServer = new DouyuHttpServer(CC.httpPort,CC.httpHost);
        douyuServer.sync();

        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
