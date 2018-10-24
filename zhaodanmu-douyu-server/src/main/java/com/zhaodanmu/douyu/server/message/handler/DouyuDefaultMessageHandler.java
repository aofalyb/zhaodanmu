package com.zhaodanmu.douyu.server.message.handler;


import com.zhaodanmu.core.common.Log;
import com.zhaodanmu.core.common.NamedPoolThreadFactory;
import com.zhaodanmu.core.netty.Connection;
import com.zhaodanmu.douyu.server.elastic.EsClient;
import com.zhaodanmu.douyu.server.message.DouyuMessage;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DouyuDefaultMessageHandler implements IMessageHandler<DouyuMessage> {

    private static EsClient esClient;

    static {
        esClient = EsClient.getInstance();
    }

    private static final int THREAD_COUNT = 1;
    private static LinkedBlockingQueue insertQueue = new LinkedBlockingQueue<Runnable>();
    //用线程池的想法是：线程池是天然的缓冲区，insert本身是阻塞的，可以缓冲写入。
    private static final Executor threadPool =  new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
                                      0L,TimeUnit.MILLISECONDS,
            insertQueue,new NamedPoolThreadFactory("es-writer"));

    @Override
    public boolean handle(Connection connection, DouyuMessage message) {
        String messageType = message.getMessageType();

        Map<String, String> attributes = message.getAttributes();
        String nn = attributes.get("nn");
        String txt = attributes.get("txt");
        String uid = attributes.get("uid");

        threadPool.execute(new EsInsertTask(nn,txt,uid));

        return false;
    }


    private class EsInsertTask implements Runnable {

        String nn,txt,uid;

        public EsInsertTask(String nn, String txt, String uid) {
            this.nn = nn;
            this.txt = txt;
            this.uid = uid;
        }

        @Override
        public void run() {
            if(esClient.isStart()) {
                try {
                    esClient.insert(uid,nn,txt);
                    Log.defLogger.debug("_current es write que length: {}.",insertQueue.size());
                } catch (Exception e) {
                    Log.defLogger.error("insert into es error, nn={},txt={},uid={}",nn,txt,uid,e);
                }
            }
        }
    }
}