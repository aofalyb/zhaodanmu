package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.common.utils.PropertiesUtil;
import com.zhaodanmu.core.redis.RedisServer;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.elasticsearch.EsClient;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {

        System.setProperty("io.netty.noUnsafe","false");
        String[] roomsArray = CC.rooms;
        PersistenceService esClient = new EsClient();
        //esClient.init(CC.esHost,CC.esPort);

        RedisServer redisServer = new RedisServer(CC.redisHost,CC.redisPort);
        redisServer.sync();

        for (int i = 0; i < roomsArray.length; i++) {
            DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient(roomsArray[i],esClient);
            douyuCrawlerClient.sync();
        }
        DouyuHttpServer douyuServer = new DouyuHttpServer(CC.httpPort,CC.httpHost,esClient);
        douyuServer.sync();
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("========== SERVER START SUCCESS ======");
        Log.sysLogger.info("======================================");
        Log.sysLogger.info("======================================");
    }
}
