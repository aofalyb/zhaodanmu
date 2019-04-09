package com.zhaodanmu.persistence.elasticsearch;

import com.zhaodanmu.common.Listener;
import com.zhaodanmu.common.service.BaseService;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.persistence.api.ESException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;

public class ElasticsearchClient extends BaseService {

    private static final String _INDEX_DOUYU = "douyu";

    private static TransportClient client;

    private String indexName = _INDEX_DOUYU;
    private String host;
    private int port;

    public ElasticsearchClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return "ElasticsearchClient{" +
                "indexName='" + indexName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        if(client != null) {
            client.close();
            listener.onSuccess();
        }
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        Log.sysLogger.info("connecting elastic server -> {} ,_index: {}",host + ":" + port,indexName);
        client  = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        IndicesAdminClient indicesAdmin = client.admin().indices();

        IndicesExistsResponse indexExsit = indicesAdmin
                .prepareExists(indexName)
                .execute()
                .actionGet();

        if(!indexExsit.isExists()) {
            Log.sysLogger.info("_Index:{} is not exists,creating...",indexName);
            CreateIndexResponse indexCreate = indicesAdmin
                    .prepareCreate(indexName)
                    .setSettings(Settings.builder().put("index.number_of_replicas",0).build())
                    .execute()
                    .actionGet();
            if(!indexCreate.isAcknowledged()) {
                Log.sysLogger.error("_Index: {} create failed",indexName);
                throw new ESException("create _index: " + indexName + "failed");
            }
            Log.sysLogger.info("_Index: {} create success",indexName);
        }

        final String _TYPE_NAME = "message";
        //message
        TypesExistsResponse typeExist = indicesAdmin
                .prepareTypesExists(indexName)
                .setTypes(_TYPE_NAME)
                .execute()
                .actionGet();

        if(!typeExist.isExists()) {
            Log.sysLogger.info("_Type:{}#{} is not exists, creating...", indexName,_TYPE_NAME);
            PutMappingRequest putMappingRequest = Requests
                    .putMappingRequest(indexName)
                    .type(_TYPE_NAME)
                    .source(getMapping());
            AcknowledgedResponse typeCreate = indicesAdmin
                    .putMapping(putMappingRequest)
                    .actionGet();
            if(!typeCreate.isAcknowledged()){
                Log.sysLogger.info("_Type:{}#{} create failed", indexName,_TYPE_NAME);
                throw new ESException("create _type: " + indexName + "#" + _TYPE_NAME + "failed");
            }
            Log.sysLogger.info("_Type:{}#{} create success", indexName,_TYPE_NAME);
        }
        listener.onSuccess();
        Log.sysLogger.info("elastic server connect success -> {}",host + ":" + port);
    }

    private XContentBuilder getMapping() {
        try {
            return XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("t").field("type","date").endObject()
                    .startObject("type").field("type","keyword").endObject()
                    .startObject("gid").field("type","long").endObject()
                    .startObject("rid").field("type","long").endObject()
                    .startObject("uid").field("type","long").endObject()
                    .startObject("nn").field("type","text").endObject()
                    .startObject("txt").field("type","text").endObject()

//                    .startObject("cid").field("type","keyword").endObject()

                    .startObject("level").field("type","short").endObject()
                    .startObject("gt").field("type","short").endObject()
                    .startObject("col").field("type","short").endObject()
                    .startObject("ct").field("type","short").endObject()
                    .startObject("rg").field("type","short").endObject()

                    .startObject("pg").field("type","short").endObject()
                    .startObject("dlv").field("type","short").endObject()
                    .startObject("dc").field("type","short").endObject()
                    .startObject("bdlv").field("type","short").endObject()
                    .startObject("cmt").field("type","short").endObject()
                    .startObject("ic").field("type","keyword").endObject()
                    .startObject("nl").field("type","short").endObject()
                    .startObject("nc").field("type","short").endObject()
                    .startObject("bnn").field("type","keyword").endObject()
                    .startObject("bl").field("type","short").endObject()
                    .startObject("brid").field("type","long").endObject()
                    .startObject("ol").field("type","short").endObject()
                    .startObject("rev").field("type","short").endObject()
                    .startObject("hl").field("type","short").endObject()

                    .endObject()
                    .endObject();
        } catch (IOException e) {
            return null;
        }
    }


    public TransportClient transportClient() {
        return client;
    }

    public static void main(String[] args) {

        ElasticsearchClient elasticsearchClient = new ElasticsearchClient("127.0.0.1",9301);
        elasticsearchClient.start();



    }


}



