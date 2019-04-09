package com.zhaodanmu.douyu.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaodanmu.common.exception.HttpException;
import com.zhaodanmu.common.utils.HttpUtils;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.douyu.server.cache.SimpleCache;
import com.zhaodanmu.douyu.server.util.ClientHolder;
import com.zhaodanmu.douyu.server.util.ThreadUtils;
import com.zhaodanmu.persistence.api.PersistenceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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
//            douyuCrawlerClient.setRoomDetail(checkExist(roomsArray[i]));
            douyuCrawlerClient.sync();
        }
        ThreadUtils.getScheduledThread().scheduleWithFixedDelay(() -> {
            for (String roomId: roomsArray
                 ) {
                DouyuCrawlerClient douyuCrawlerClient = ClientHolder.get(roomId);
                if(douyuCrawlerClient != null) {
                    try {
                        douyuCrawlerClient.setRoomDetail(checkExist(roomId));
                    } catch (Exception e) {
                        Log.sysLogger.error("timer-room-update failed",e);
                    }
                }
                Log.sysLogger.info("success update douyu room detail,room id: {}.",roomId);
            }
        },5 * 60,5 * 60, TimeUnit.SECONDS);

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
        RoomDetail roomDetail = JSON.parseObject(data, RoomDetail.class);
        List<RoomDetail.GiftEntity> roomDetailGifts = roomDetail.getGift();

        for (RoomDetail.GiftEntity giftInfo:roomDetailGifts) {
            Map giftCache = new HashMap();
            int devote = giftInfo.getGx();
            giftCache.put("devote",devote);
            giftCache.put("name",giftInfo.getName());
            giftCache.put("himg",giftInfo.getHimg());
            SimpleCache.store("gift:" + giftInfo,giftCache);
        }

        return roomDetail;
    }
}
