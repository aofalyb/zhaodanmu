package com.zhaodanmu.persistence.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.common.PageInfo;
import com.zhaodanmu.common.thread.NamedPoolThreadFactory;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.persistence.api.Model;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.api.Search;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ElasticPersistenceService implements PersistenceService {

    private TransportClient client;

    private static BlockingQueue<Object> bufferedModelQueue = new LinkedBlockingQueue<>();

    private static final int THREAD_COUNT = 1;
    private static LinkedBlockingQueue insertQueue = new LinkedBlockingQueue<Runnable>();
    //可能被调整
    private volatile int modelBufferSize = 100;
    private int originModelBufferSize = modelBufferSize;
    private int dangerInstertQueueSize = 388;
    //用线程池的想法是：线程池是天然的缓冲区，insert本身是阻塞的，可以缓冲写入。
    private static final ThreadPoolExecutor threadPool =  new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
            0L, TimeUnit.MILLISECONDS,
            insertQueue,new NamedPoolThreadFactory("es-writer"));

    private ElasticsearchClient elasticsearchClient;

    public ElasticPersistenceService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
        client = elasticsearchClient.transportClient();
    }

    /**
     * 带缓冲地同步写入es
     */
    @Override
    public void bufferedInsert(Object model) {
        bufferedModelQueue.add(model);
        if(bufferedModelQueue.size() > modelBufferSize && elasticsearchClient.isRunning()) {
            List<Object> models = new LinkedList<>();
            bufferedModelQueue.drainTo(models,modelBufferSize);
            while (models.size() < modelBufferSize) {
                bufferedModelQueue.drainTo(models,modelBufferSize);
            }
            batchInsert(true,models);
        }
    }

    @Override
    public void batchInsert(final boolean async,final List<Object> models) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                final BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
                for (final Object model: models) {
                    if(async) {
                        Map map = (Map) model;
                        IndexRequestBuilder indexRequestBuilder = client
                                .prepareIndex("douyu", "message")
                                .setSource(JSON.toJSONString(model), XContentType.JSON)
                                .setId((String) map.get("cid"));

                        bulkRequestBuilder.add(indexRequestBuilder.request());
                    }
                }

                long _s = System.currentTimeMillis();
                BulkResponse bulkItemResponses = bulkRequestBuilder.get();
                long _e = System.currentTimeMillis();
                int queueSize = insertQueue.size();
                if((_e-_s) > 100) {
                    Log.defLogger.warn("busy disk:es insert: {} rows ,cost time: {} ms, hasFailures: {}, waitInsert: {}",models.size(),_e-_s,bulkItemResponses.hasFailures(), queueSize);
                } else {
                    Log.defLogger.debug("es insert: {} rows , cost time: {} ms, hasFailures: {}, waitInsert: {}",models.size(),_e-_s,bulkItemResponses.hasFailures(), queueSize);
                }
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

            }
        });

    }

    @Override
    public PageInfo search(Search search) {
        QueryBuilder queryBuilder = null;
        if(search.getKey() != null) {
            queryBuilder = QueryBuilders.termQuery(search.getKey(),search.getKeyWord());
        } else {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        SearchResponse response = client.prepareSearch(search.getIndex())
                .setTypes(search.getType())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addSort("t", SortOrder.DESC)
                .setFrom(search.from()).setSize(15)
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
            pageInfo = new PageInfo(0,0, Collections.emptyList());
        }

        return pageInfo;
    }


    @Override
    public void insert(Model model) {

    }
}
