package com.zhaodanmu.douyu.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaodanmu.common.exception.HttpException;
import com.zhaodanmu.common.utils.HttpUtils;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.core.redis.RedisServer;
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
            douyuCrawlerClient.setRoomDetail(checkExist(roomsArray[i]));
            douyuCrawlerClient.sync();
        }

    }


    private RoomDetail checkExist(String roomId) {
        final String json;
        final String url = "http://open.douyucdn.cn/api/RoomApi/room/" + roomId;
        try {
            json = HttpUtils.get(url);
        } catch (Exception e) {
            Log.httpLogger.error("http GET request failed,url: {}",url,e);
            throw new HttpException(e);
        }

        JSONObject resp = JSON.parseObject(json);
        if(resp.getInteger("error") != 0) {
            Log.httpLogger.error("http GET request success,but resp error code is:{} ,url: {},resp: {}",resp.getInteger("error"),url,json);
            throw new HttpException("resp error,message:" + resp.getString("data"));
        }
        String data = resp.getString("data");

        return JSON.parseObject(data, RoomDetail.class);
    }
}
