package com.zhaodanmu.douyu.server.netty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaodanmu.common.PageInfo;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;
import com.zhaodanmu.persistence.api.ESException;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.api.Search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2018/10/31.
 */
public class SearchController implements Controller {

    private PersistenceService persistenceService;

    public SearchController(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String getRequestMapping() {
        return "douyu/search";
    }

    @Override
    public Object handle(URI uri, String body) {

        String key = uri.getParameter("key");
        String keyWord;
        try {
            keyWord = URLDecoder.decode(uri.getParameter("keyWord"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        String from = uri.getParameter("from");

        PageInfo search = persistenceService.search(new Search() {
            @Override
            public String getType() {
                return "danmu";
            }

            @Override
            public String getIndex() {
                return "danmu";
            }

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getKeyWord() {
                return keyWord;
            }

            @Override
            public int from() {
                int fromNum = (from == null ? 0 : Integer.parseInt(from));
                if(fromNum > 10000) {
                    throw new ESException("too large num 'from'");
                }
                return fromNum;
            }
        });

        return search;
    }
}
