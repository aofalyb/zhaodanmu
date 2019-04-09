package com.zhaodanmu.douyu.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhaodanmu.common.exception.HttpException;
import com.zhaodanmu.common.utils.HttpUtils;
import com.zhaodanmu.common.utils.Log;
import com.zhaodanmu.douyu.server.cache.SimpleCache;
import com.zhaodanmu.douyu.server.util.ThreadUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**礼物配置列表
 * http://webconf.douyucdn.cn/resource/common/prop_gift_list/prop_gift_config.json
 */
public class PropGiftConfig {

    private static Map<String,PropGiftInfo> giftInfoMap = new HashMap<>();

    private static final String URL = "http://webconf.douyucdn.cn/resource/common/prop_gift_list/prop_gift_config.json";

    public static void init() {
        Log.sysLogger.info("Getting gift_conf -> {}",URL);
        getGiftConfigFromDouyu();
        ThreadUtils.getScheduledThread().scheduleWithFixedDelay(() -> {
            try {
                getGiftConfigFromDouyu();
            } catch (Exception e) {
                Log.sysLogger.error("timer-gift-config-update failed",e);
            }
        },15 * 60,15 * 60, TimeUnit.SECONDS);
    }


    private static void getGiftConfigFromDouyu() {
        String dyConfigCallback = null;
        try {
            dyConfigCallback = HttpUtils.get(URL);
        } catch (Exception e) {
            throw new HttpException("get dy prop gift config failed",e);
        }

        int startIndex = dyConfigCallback.indexOf("(");
        int endIndex = dyConfigCallback.lastIndexOf(")");
        String originJson = dyConfigCallback.substring(startIndex + 1, endIndex);
        String dataJson = JSON.parseObject(originJson).getString("data");
        giftInfoMap = JSON.parseObject(dataJson, new TypeReference<Map<String, PropGiftInfo>>(){});

        Collection<PropGiftInfo> propGiftInfos = giftInfoMap.values();

        for (PropGiftInfo giftInfo: propGiftInfos ) {
            Map giftCache = new HashMap();
            int devote = giftInfo.getDevote();
            giftCache.put("devote",devote);
            giftCache.put("name",giftInfo.getName());
            giftCache.put("himg",giftInfo.getHimg());
            SimpleCache.store("gift:" + giftInfo,giftCache);
        }

        Log.sysLogger.info("get prop_gift_config SUCCESS,gifts len:{}", propGiftInfos.size());
    }



    public static PropGiftInfo getPropGift(String giftId) {
        return giftInfoMap.get(giftId);
    }




}
