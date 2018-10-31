package com.zhaodanmu.douyu.server.netty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.api.Search;
import com.zhaodanmu.persistence.elasticsearch.model.DouyuESModel;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31.
 */
public class DouyuSearchController implements Controller {

    private PersistenceService persistenceService;

    public DouyuSearchController(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String getRequestMapping() {
        return "douyu/search";
    }

    @Override
    public Object handle(URI uri, String body) {
        JSONObject bodyMap = JSON.parseObject(body);

        String search = persistenceService.search(new Search() {
            @Override
            public String getType() {
                return "danmu";
            }

            @Override
            public String getIndex() {
                return "danmu";
            }

            @Override
            public String getN() {
                return bodyMap.getString("nn");
            }
        });
        List<DouyuESModel> douyuESModels = JSON.parseArray(search,DouyuESModel.class);

        return douyuESModels;
    }
}
