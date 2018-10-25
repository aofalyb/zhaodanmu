package com.zhaodanmu.persistence.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.persistence.elasticsearch.model.DouyuESModel;
import com.zhaodanmu.persistence.elasticsearch.util.NamedPoolThreadFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
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
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.*;

public class EsClient {


    final static String TYPE_NAME = "danmu";

    final static String INDEX_NAME = "douyu";

    private EsClient() {}

    private volatile static EsClient esClient;

    private static TransportClient client;

    private volatile boolean start = false;
    private static Object lock = new Object();

    private static BlockingQueue<BulkResponse> waitCheckQueue = new LinkedBlockingQueue<>();

    private static final int THREAD_COUNT = 1;
    private static LinkedBlockingQueue insertQueue = new LinkedBlockingQueue<Runnable>();
    //用线程池的想法是：线程池是天然的缓冲区，insert本身是阻塞的，可以缓冲写入。
    private static final ThreadPoolExecutor threadPool =  new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
            0L, TimeUnit.MILLISECONDS,
            insertQueue,new NamedPoolThreadFactory("es-writer"));

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

    public void shutdown() {
        if(client != null && start) {
            threadPool.shutdown();
            client.close();
        }
    }


    public void init(String host,int port) {

        Settings settings = Settings
                .builder()
                .build();
        try {
            client  = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            throw new ESException(e);
        }

        IndicesExistsResponse indexExsit = client
                .admin()
                .indices()
                .prepareExists(INDEX_NAME)
                .execute()
                .actionGet();

        if(!indexExsit.isExists()) {
            //创建
            Log.sysLogger.info("es index:{} is not exists, prepare create!",INDEX_NAME);
            CreateIndexResponse indexCreate = client
                    .admin()
                    .indices()
                    .prepareCreate(INDEX_NAME)
                    .execute()
                    .actionGet();
            if(!indexCreate.isAcknowledged()) {
                Log.sysLogger.error("create es index: {} FAILED!.",INDEX_NAME);
                throw new ESException("create es index: " + INDEX_NAME + "failed");
            }
            Log.sysLogger.info("create es index: {} SUCCESS!",INDEX_NAME);

        }

        //索引字段
        TypesExistsResponse typeExist = client
                .admin()
                .indices()
                .prepareTypesExists(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .execute()
                .actionGet();

        if(!typeExist.isExists()) {
            Log.sysLogger.info("index type:{} is not exists, prepare create!",TYPE_NAME);
            PutMappingRequest putMappingRequest = Requests
                    .putMappingRequest(INDEX_NAME)
                    .type(TYPE_NAME)
                    .source(getMapping());
            PutMappingResponse typeCreate = client.admin()
                    .indices()
                    .putMapping(putMappingRequest)
                    .actionGet();
            if(!typeCreate.isAcknowledged()){
                Log.sysLogger.error("create index type:{} FAILED!",TYPE_NAME);
                throw new ESException("create index type: " + TYPE_NAME + "failed");
            }
            Log.sysLogger.info("create index type:{} SUCCESS!",TYPE_NAME);
        }
        Log.sysLogger.info("es client start success at:{}, use index:{}, use type:{}",host + ":" + port,INDEX_NAME,TYPE_NAME);
        start = true;
    }


    private XContentBuilder getMapping() {
        try {
            return XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("rid").field("type","long").endObject()
                    .startObject("uid").field("type","long").endObject()
                    .startObject("level").field("type","integer").endObject()
                    .startObject("nn").field("type","keyword").endObject()
                    .startObject("gid").field("type","long").endObject()
                    .startObject("ic").field("type","keyword").endObject()
                    .startObject("nl").field("type","integer").endObject()
                    .startObject("nc").field("type","integer").endObject()
                    .startObject("bnn").field("type","keyword").endObject()
                    .startObject("bl").field("type","integer").endObject()
                    .startObject("brid").field("type","long").endObject()
                    .startObject("type").field("type","keyword").endObject()
                    .startObject("txt").field("type","keyword").endObject()
                    .startObject("cid").field("type","keyword").endObject()
                    .startObject("col").field("type","integer").endObject()
                    .startObject("rev").field("type","integer").endObject()
                    .startObject("hl").field("type","integer").endObject()
                    .startObject("ifs").field("type","integer").endObject()
                    .startObject("cnt").field("type","integer").endObject()
                    .startObject("lev").field("type","integer").endObject()
                    .startObject("gfid").field("type","long").endObject()
                    .startObject("gfcnt").field("type","integer").endObject()
                    .startObject("hits").field("type","integer").endObject()
                    .startObject("now").field("type","date").endObject()
                    .endObject()
                    .endObject();

        } catch (IOException e) {
        }


        return null;
    }


    private volatile static int _BATCH_LEN = 100;
    private volatile int CURSOR = 0;
    private volatile BulkRequestBuilder bulkRequestBuilder;


    public synchronized void insert(DouyuESModel douyuESModel) {

        long _s = System.currentTimeMillis();

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(INDEX_NAME, TYPE_NAME)
                .setSource(JSON.toJSONString(douyuESModel), XContentType.JSON);

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
        try {
            waitCheckQueue.put(bulkItemResponses);
        } catch (InterruptedException e) {

        }
        bulkRequestBuilder = null;
        CURSOR = 0;
        long _e = System.currentTimeMillis();
        Log.defLogger.info("es insert cost time=" + (_e - _s) +"ms, hasFailures=" + bulkItemResponses.hasFailures()+",insertQueue=" + insertQueue.size());
    }


    public synchronized void asyncInsert(final DouyuESModel douyuESModel) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                insert(douyuESModel);
            }
        });
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



