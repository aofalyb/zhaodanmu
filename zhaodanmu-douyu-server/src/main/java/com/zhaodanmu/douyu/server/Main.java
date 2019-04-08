package com.zhaodanmu.douyu.server;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.EsClient;

public class Main {

    public static void main(String[] args) {

        System.setProperty("io.netty.noUnsafe","false");
//
        PersistenceService esClient = new EsClient();
//        esClient.init(CC.esHost,CC.esPort);
//
//        RedisServer.connect(CC.redisHost,CC.redisPort);

        PropGiftConfig.init();

        DouyuCrawlerServer douyuCrawlerServer = new DouyuCrawlerServer(esClient);
        douyuCrawlerServer.sync();

        DouyuHttpServer douyuServer = new DouyuHttpServer(esClient);
        douyuServer.sync();
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
