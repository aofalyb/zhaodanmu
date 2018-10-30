package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.common.utils.PropertiesUtil;
import com.zhaodanmu.persistence.elasticsearch.EsClient;

public class Main {

    public static void main(String[] args) {

        System.setProperty("io.netty.noUnsafe","false");
        String[] roomsArray = CC.rooms;
        EsClient.getInstance().init(CC.esHost,CC.esPort);
        for (int i = 0; i < roomsArray.length; i++) {
            DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient(roomsArray[i]);
            douyuCrawlerClient.doStart();
        }

        DouyuHttpServer douyuServer = new DouyuHttpServer(CC.httpPort,CC.httpHost);
        douyuServer.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                Log.sysLogger.info("http server start success,host&port: [{}]",CC.httpHost + ":" + CC.httpPort);
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.sysLogger.error("http server start failed,host&port: [{}]",CC.httpHost + ":" + CC.httpPort,cause);
            }
        });

        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
