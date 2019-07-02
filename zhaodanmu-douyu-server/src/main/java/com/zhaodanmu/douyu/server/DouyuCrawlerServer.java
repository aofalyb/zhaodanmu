package com.zhaodanmu.douyu.server;

import com.zhaodanmu.persistence.api.PersistenceService;


/**
 * Created by Administrator on 2018/11/2.
 */
public class DouyuCrawlerServer {

    private PersistenceService persistenceService;


    public DouyuCrawlerServer(PersistenceService persistenceService) {
       this.persistenceService = persistenceService;
    }

    public void sync() {
        String[] roomsArray = CC.rooms;
        for (int i = 0; i < roomsArray.length; i++) {
            DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient(roomsArray[i],persistenceService);
            douyuCrawlerClient.sync();
        }

    }

}
