package com.zhaodanmu.persistence.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EsClient {

    private EsClient() {}

    private volatile static EsClient esClient;

    private static TransportClient client;

    private volatile boolean start = false;
    private static Object lock = new Object();

    private static BlockingQueue<BulkResponse> waitCheckQueue = new LinkedBlockingQueue<>();

    static {

        //用一个线程轮训检查是否有错误的写入
        CheckBulkResponseFailThread checkWriteFailThread = new CheckBulkResponseFailThread("check-es-write-fail");
        checkWriteFailThread.setDaemon(true);
        checkWriteFailThread.start();
    }



    public static  EsClient getInstance() {
        if(esClient == null) {
            synchronized (lock) {
                if(esClient == null) {
                    esClient = new EsClient();
                }
            }
        }
        return esClient;
    }

    public static void shutdown() {
        if(client != null) {
            client.close();
        }

    }

    final String TYPE_NAME = "barrage";

    final String INDEX_NAME = "douyu-barrage_v3";
    public void init() throws Exception{
        long _s = System.currentTimeMillis();
        if(client == null) {
            Settings settings = Settings.builder().build();
            client  = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        }



        IndicesExistsResponse existsResponse = client.admin().indices().prepareExists(INDEX_NAME).execute().actionGet();

        //Log.defLogger.error("existsResponse="+existsResponse.isExists());
        if(!existsResponse.isExists()) {
            //创建

            CreateIndexResponse indexResponse = client.admin().indices().prepareCreate(INDEX_NAME).execute().actionGet();
            //Log.defLogger.error("indexResponse"+indexResponse.isAcknowledged());

        }

        //索引字段

        TypesExistsResponse typesResponse = client.admin().indices().prepareTypesExists(INDEX_NAME).setTypes(TYPE_NAME).execute().actionGet();
        //Log.defLogger.error("TypesExistsResponse="+typesResponse.isExists());
        if(!typesResponse.isExists()) {

            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
                    .startObject("uid").field("type", "keyword").endObject()
                    .startObject("nn").field("type", "keyword").endObject()
                    .startObject("text").field("type", "keyword").endObject()
                    .endObject().endObject();

            PutMappingRequest mappingRequest = Requests.putMappingRequest(INDEX_NAME).type(TYPE_NAME).source(mapping);

            //Log.defLogger.error("mapping="+client.admin().indices().putMapping(mappingRequest).actionGet().isAcknowledged());

        }
        start = true;

        long _e = System.currentTimeMillis();
        System.out.println(_e - _s);
    }

    private volatile static int _BATCH_LEN = 100;
    private volatile int CURSOR = 0;
    private volatile BulkRequestBuilder bulkRequestBuilder;

    /**
     * 这一段代码多线程环境下会报错，所以加了同步锁（原因抽时间再查）。
     *
     */
    public synchronized void insert(String uid,String nn,String text) throws Exception {

        long _s = System.currentTimeMillis();

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(INDEX_NAME, TYPE_NAME)
                .setSource(XContentFactory.jsonBuilder().startObject()
                        .field("uid", uid)
                        .field("nn", nn)
                        .field("text", text)
                        .endObject());

        if(bulkRequestBuilder == null) {
            bulkRequestBuilder = client.prepareBulk();
        }

        //批量写入，不知道es有没有带buffer类，没有的话，考虑自己实现一个
        //这里用游标实现了一个非常简单的buffer
        if(CURSOR < _BATCH_LEN) {
            bulkRequestBuilder.add(indexRequestBuilder.request());
            ++CURSOR;
            return;
        }

        BulkResponse bulkItemResponses = bulkRequestBuilder.get();
        waitCheckQueue.put(bulkItemResponses);
        bulkRequestBuilder = null;
        CURSOR = 0;


        long _e = System.currentTimeMillis();
        System.out.println("insert cost time="+(_e - _s)+"ms, result="+bulkItemResponses.hasFailures()+".");
    }


    public boolean isStart() {
        return start;
    }

    private static class CheckBulkResponseFailThread extends Thread {

        public CheckBulkResponseFailThread(String name) {
            super(name);
        }

        @Override
        public void run() {

            BulkResponse bulkResponse = null;
            try {
                while ((bulkResponse = waitCheckQueue.take()) != null) {
                        if(bulkResponse.hasFailures()) {
                            Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
                            while (iterator.hasNext()) {
                                BulkItemResponse bulkItemResponse = iterator.next();
                                //Log.defLogger.error("#["+System.currentTimeMillis()+"]"+"["+bulkItemResponse+"]");
                            }

                        }
                }
            } catch (InterruptedException e) {
                //Log.defLogger.error("waitCheckQueue take error.",e);
            }

        }
    }



}



