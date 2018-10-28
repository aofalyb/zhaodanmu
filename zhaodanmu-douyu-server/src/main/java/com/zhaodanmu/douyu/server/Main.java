package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.util.PropertiesUtil;
import com.zhaodanmu.persistence.elasticsearch.EsClient;

public class Main {

    public static void main(String[] args) {


        PropertiesUtil.load("douyu.properties");
        String roomsArray = PropertiesUtil.getValue("rooms").trim();
        String[] rooms = roomsArray.split(",");

        EsClient.getInstance().init(PropertiesUtil.getValue("es.host"),Integer.parseInt(PropertiesUtil.getValue("es.port")));

        for (int i = 0; i < rooms.length; i++) {
            DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient(rooms[i]);
            douyuCrawlerClient.doStart();
        }

        DouyuHttpServer douyuServer = new DouyuHttpServer(8088,"127.0.0.1");
        douyuServer.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {

            }

            @Override
            public void onFailure(Throwable cause) {
                cause.printStackTrace();
            }
        });

        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
