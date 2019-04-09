package com.zhaodanmu.douyu.server;

import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.ElasticPersistenceService;
import com.zhaodanmu.persistence.elasticsearch.ElasticsearchClient;

public class Main {

    public static void main(String[] args) throws Exception {

        System.setProperty("io.netty.noUnsafe","false");
//
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient("127.0.0.1",9301);
        elasticsearchClient.start();
        PersistenceService persistenceService = new ElasticPersistenceService(elasticsearchClient);
//
//        RedisServer.connect(CC.redisHost,CC.redisPort);

//        PropGiftConfig.init();

        DouyuCrawlerServer douyuCrawlerServer = new DouyuCrawlerServer(persistenceService);
        douyuCrawlerServer.sync();

        DouyuHttpServer douyuServer = new DouyuHttpServer(persistenceService);
        douyuServer.sync();
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
