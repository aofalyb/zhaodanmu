package com.zhaodanmu.douyu.server;

public class Main {

    public static void main(String[] args) {
        DouyuCrawlerClient douyuCrawlerClient = new DouyuCrawlerClient("99999");
        boolean start = douyuCrawlerClient.doStart();
        if(!(start)) {
            System.exit(-1);
        }
    }
}
