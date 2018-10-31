package com.zhaodanmu.persistence.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.PageInfo;
import com.zhaodanmu.common.thread.NamedPoolThreadFactory;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.persistence.api.*;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;

public class EsClient implements PersistenceService {

    private static TransportClient client;

    private volatile boolean start = false;
    private static Object lock = new Object();

    private static BlockingQueue<BulkResponse> waitCheckQueue = new LinkedBlockingQueue<>();
    private static BlockingQueue<Model> bufferedModelQueue = new LinkedBlockingQueue<>();

    private static final int THREAD_COUNT = 1;
    private static LinkedBlockingQueue insertQueue = new LinkedBlockingQueue<Runnable>();
    //可能被调整
    private volatile int modelBufferSize = 500;
    private int originModelBufferSize = modelBufferSize;
    private int dangerInstertQueueSize = 388;
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

    @Override
    public void init(Object...args) {
        final String host = (String) args[0];
        final int port = (int) args[1];

        Log.sysLogger.info("connecting es client es.host: [{}]",host + ":" + port);

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
                .prepareExists(TypeNameEnmu.danmu.name())
                .execute()
                .actionGet();

        if(!indexExsit.isExists()) {
            //创建
            Log.sysLogger.info("es index:{} is not exists, prepare create!",TypeNameEnmu.danmu.name());
            CreateIndexResponse indexCreate = client
                    .admin()
                    .indices()
                    .prepareCreate(TypeNameEnmu.danmu.name())
                    .setSettings(Settings.builder().put("index.number_of_replicas",0).build())
                    .execute()
                    .actionGet();
            if(!indexCreate.isAcknowledged()) {
                Log.sysLogger.error("create es index: {} FAILED!.",TypeNameEnmu.danmu.name());
                throw new ESException("create es index: " + TypeNameEnmu.danmu.name() + "failed");
            }
            Log.sysLogger.info("create es index: {} SUCCESS!",TypeNameEnmu.danmu.name());

        }

        indexExsit = client
                .admin()
                .indices()
                .prepareExists(TypeNameEnmu.new_black.name())
                .execute()
                .actionGet();

        if(!indexExsit.isExists()) {
            //创建
            Log.sysLogger.info("es index:{} is not exists, prepare create!",TypeNameEnmu.new_black.name());
            CreateIndexResponse indexCreate = client
                    .admin()
                    .indices()
                    .prepareCreate(TypeNameEnmu.new_black.name())
                    .setSettings(Settings.builder().put("index.number_of_replicas",0).build())
                    .execute()
                    .actionGet();
            if(!indexCreate.isAcknowledged()) {
                Log.sysLogger.error("create es index: {} FAILED!.",TypeNameEnmu.new_black.name());
                throw new ESException("create es index: " + TypeNameEnmu.new_black.name() + "failed");
            }
            Log.sysLogger.info("create es index: {} SUCCESS!",TypeNameEnmu.new_black.name());

        }

        indexExsit = client
                .admin()
                .indices()
                .prepareExists(TypeNameEnmu.user.name())
                .execute()
                .actionGet();

        if(!indexExsit.isExists()) {
            //创建
            Log.sysLogger.info("es index:{} is not exists, prepare create!",TypeNameEnmu.user.name());
            CreateIndexResponse indexCreate = client
                    .admin()
                    .indices()
                    .prepareCreate(TypeNameEnmu.user.name())
                    .setSettings(Settings.builder().put("index.number_of_replicas",0).build())
                    .execute()
                    .actionGet();
            if(!indexCreate.isAcknowledged()) {
                Log.sysLogger.error("create es index: {} FAILED!.",TypeNameEnmu.user.name());
                throw new ESException("create es index: " + TypeNameEnmu.user.name() + "failed");
            }
            Log.sysLogger.info("create es index: {} SUCCESS!",TypeNameEnmu.user.name());

        }

        //danmu
        //索引字段
        TypesExistsResponse typeExist = client
                .admin()
                .indices()
                .prepareTypesExists(TypeNameEnmu.danmu.name())
                .setTypes(TypeNameEnmu.danmu.name())
                .execute()
                .actionGet();

        if(!typeExist.isExists()) {
            Log.sysLogger.info("index type:{} is not exists, prepare create!", TypeNameEnmu.danmu.name());
            PutMappingRequest putMappingRequest = Requests
                    .putMappingRequest( TypeNameEnmu.danmu.name())
                    .type(TypeNameEnmu.danmu.name())
                    .source(getMapping(TypeNameEnmu.danmu));
            PutMappingResponse typeCreate = client.admin()
                    .indices()
                    .putMapping(putMappingRequest)
                    .actionGet();
            if(!typeCreate.isAcknowledged()){
                Log.sysLogger.error("create index type:{} FAILED!", TypeNameEnmu.danmu.name());
                throw new ESException("create index type: " + TypeNameEnmu.danmu.name() + "failed");
            }
            Log.sysLogger.info("create index type:{} SUCCESS!", TypeNameEnmu.danmu.name());
        }

        typeExist = client
                .admin()
                .indices()
                .prepareTypesExists(TypeNameEnmu.new_black.name())
                .setTypes(TypeNameEnmu.new_black.name())
                .execute()
                .actionGet();

        if(!typeExist.isExists()) {
            Log.sysLogger.info("index type:{} is not exists, prepare create!", TypeNameEnmu.new_black.name());
            PutMappingRequest putMappingRequest = Requests
                    .putMappingRequest(TypeNameEnmu.new_black.name())
                    .type(TypeNameEnmu.new_black.name())
                    .source(getMapping(TypeNameEnmu.new_black));
            PutMappingResponse typeCreate = client.admin()
                    .indices()
                    .putMapping(putMappingRequest)
                    .actionGet();
            if(!typeCreate.isAcknowledged()){
                Log.sysLogger.error("create index type:{} FAILED!", TypeNameEnmu.new_black.name());
                throw new ESException("create index type: " + TypeNameEnmu.new_black.name() + "failed");
            }
            Log.sysLogger.info("create index type:{} SUCCESS!", TypeNameEnmu.new_black.name());
        }


        typeExist = client
                .admin()
                .indices()
                .prepareTypesExists(TypeNameEnmu.user.name())
                .setTypes(TypeNameEnmu.user.name())
                .execute()
                .actionGet();

        if(!typeExist.isExists()) {
            Log.sysLogger.info("index type:{} is not exists, prepare create!", TypeNameEnmu.user.name());
            PutMappingRequest putMappingRequest = Requests
                    .putMappingRequest(TypeNameEnmu.user.name())
                    .type(TypeNameEnmu.user.name())
                    .source(getMapping(TypeNameEnmu.user));
            PutMappingResponse typeCreate = client.admin()
                    .indices()
                    .putMapping(putMappingRequest)
                    .actionGet();
            if(!typeCreate.isAcknowledged()){
                Log.sysLogger.error("create index type:{} FAILED!", TypeNameEnmu.user.name());
                throw new ESException("create index type: " + TypeNameEnmu.user.name() + "failed");
            }
            Log.sysLogger.info("create index type:{} SUCCESS!", TypeNameEnmu.user.name());
        }

        Log.sysLogger.info("connect es client success es.host: {}",host + ":" + port);
        start = true;
    }


    private XContentBuilder getMapping(TypeNameEnmu typeName) {
        if(typeName.equals(TypeNameEnmu.danmu)) {
            try {
                return XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("properties")
                        .startObject("t").field("type","date").endObject()
                        .startObject("rid").field("type","long").endObject()
                        .startObject("uid").field("type","long").endObject()
                        .startObject("level").field("type","integer").endObject()
                        .startObject("nn").field("type","keyword").endObject()
                        //.startObject("gid").field("type","long").endObject()
                        //.startObject("ic").field("type","keyword").endObject()
                        //.startObject("nl").field("type","integer").endObject()
                        //.startObject("nc").field("type","integer").endObject()
                        //.startObject("bnn").field("type","keyword").endObject()
                        //.startObject("bl").field("type","integer").endObject()
                        //.startObject("brid").field("type","long").endObject()
                        .startObject("type").field("type","keyword").endObject()
                        .startObject("txt").field("type","keyword").endObject()
                        //.startObject("cid").field("type","keyword").endObject()
                        //.startObject("col").field("type","integer").endObject()
                        //.startObject("rev").field("type","integer").endObject()
                        //.startObject("hl").field("type","integer").endObject()
                        //.startObject("ifs").field("type","integer").endObject()
                        .startObject("cnt").field("type","integer").endObject()
                        .startObject("lev").field("type","integer").endObject()
                        .startObject("gfid").field("type","long").endObject()
                        .startObject("gfcnt").field("type","integer").endObject()
                        .startObject("hits").field("type","integer").endObject()
                        .endObject()
                        .endObject();

            } catch (IOException e) {
            }
        }

        if(typeName.equals(TypeNameEnmu.new_black)) {
            try {
                return XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("properties")
                        .startObject("t").field("type","date").endObject()
                        .startObject("rid").field("type","long").endObject()
                        .startObject("type").field("type","keyword").endObject()
                        .startObject("ret").field("type","keyword").endObject()
                        .startObject("otype").field("type","integer").endObject()
                        .startObject("sid").field("type","long").endObject()
                        .startObject("did").field("type","long").endObject()
                        .startObject("snic").field("type","keyword").endObject()
                        .startObject("dnic").field("type","keyword").endObject()
                        .startObject("endtime").field("type","long").endObject()
                        .endObject()
                        .endObject();

            } catch (IOException e) {
            }
        }


        if(typeName.equals(TypeNameEnmu.user)) {
            try {
                return XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("properties")

                        .startObject("t").field("type","date").endObject()
                        .startObject("rid").field("type","long").endObject()
                        .startObject("uid").field("type","long").endObject()
                        .startObject("level").field("type","integer").endObject()
                        .startObject("nn").field("type","keyword").endObject()
                        .startObject("ic").field("type","keyword").endObject()
                        .startObject("nl").field("type","integer").endObject()
                        .startObject("bnn").field("type","keyword").endObject()
                        .startObject("bl").field("type","integer").endObject()
                        .startObject("brid").field("type","long").endObject()

                        .endObject()
                        .endObject();

            } catch (IOException e) {
            }
        }

        return null;
    }



    /**
     * 带缓冲地同步写入es
     */
    @Override
    public void bufferedInsert(Model model) {
        bufferedModelQueue.add(model);
        if(bufferedModelQueue.size() > modelBufferSize && isStart()) {
            List<Model> models = new LinkedList<>();
            bufferedModelQueue.drainTo(models,modelBufferSize);
            while (models.size() < modelBufferSize) {
                bufferedModelQueue.drainTo(models,modelBufferSize);
            }
            batchInsert(true,models);
        }
    }

    @Override
    public void batchInsert(final boolean async,final List<Model> models) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                final BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
                for (final Model model: models) {
                    if(async) {
                        IndexRequestBuilder indexRequestBuilder = client
                                .prepareIndex(model.getMType(), model.getMType())
                                .setSource(JSON.toJSONString(model), XContentType.JSON)
                                .setId(model.getPK());

                        bulkRequestBuilder.add(indexRequestBuilder.request());
                    }
                }

                long _s = System.currentTimeMillis();
                BulkResponse bulkItemResponses = bulkRequestBuilder.get();
                long _e = System.currentTimeMillis();
                int queueSize = insertQueue.size();
                Log.defLogger.info("es bufferedInsert cost time: {} ms, hasFailures: {}, waitInsert: {}",_e-_s,bulkItemResponses.hasFailures(), queueSize);
                //动态调整
                if(queueSize > dangerInstertQueueSize) {
                    if(modelBufferSize < originModelBufferSize * 100) {
                        modelBufferSize = modelBufferSize * 10;
                    }
                    Log.defLogger.warn("es dynamic regulation 'modelBufferSize' ,waitInsert:{}, new value: {}",queueSize,modelBufferSize);
                } else if(modelBufferSize != originModelBufferSize && queueSize == 0) {
                    modelBufferSize = originModelBufferSize;
                    Log.defLogger.warn("es dynamic regulation 'modelBufferSize' ,waitInsert:{}, new value: {}",queueSize,modelBufferSize);
                }

                try {
                    waitCheckQueue.put(bulkItemResponses);
                } catch (InterruptedException e) {

                }
            }
        });

    }

    @Override
    public PageInfo search(Search search) {

        QueryBuilder queryBuilder = QueryBuilders.termQuery(search.getKey(),search.getKeyWord());
        SearchResponse response = client.prepareSearch(search.getIndex())
                .setTypes(search.getType())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addSort("t", SortOrder.DESC)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        PageInfo pageInfo = null;
        if(hits.getTotalHits() > 0) {
            List result = new ArrayList((int) hits.getTotalHits());
            for (SearchHit hit: hits.getHits()) {
                Map sourceMap = hit.getSourceAsMap();
                result.add(sourceMap);
            }
            pageInfo = new PageInfo((int)hits.getTotalHits(),result.size(),result);
        } else {
            pageInfo = new PageInfo(0,0,Collections.emptyList());
        }

        return pageInfo;
    }


    @Override
    public void insert(Model model) {

    }



    @Override
    public void shutdown(Object...args) {
        if(client != null && start) {
            threadPool.shutdown();
            client.close();
            //TODO [清空写入队列]
        }
    }


    @Override
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



