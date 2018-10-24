package com.zhaodanmu.douyu.server;

import com.zhaodanmu.core.common.Listener;
import com.zhaodanmu.core.event.EventBus;
import com.zhaodanmu.douyu.server.util.ClientHolder;

public class Main {

    public static void main(String[] args) {
        DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient("99999");
        boolean start = douyuCrawlerClient.start();
        if(!start) {
            System.exit(-1);
        }
    }
}
