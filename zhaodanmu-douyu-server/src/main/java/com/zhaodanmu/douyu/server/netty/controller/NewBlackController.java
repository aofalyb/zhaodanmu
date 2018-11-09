package com.zhaodanmu.douyu.server.netty.controller;

import com.zhaodanmu.common.PageInfo;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;
import com.zhaodanmu.persistence.api.ESException;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.api.Search;
import com.zhaodanmu.persistence.api.TypeNameEnmu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class NewBlackController implements Controller {

    private PersistenceService persistenceService;

    public NewBlackController(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String getRequestMapping() {
        return "douyu/newBlack";
    }

    @Override
    public Object handle(URI uri, String body) {
        String from = uri.getParameter("from");
        String key = uri.getParameter("key");
        String keyWord = uri.getParameter("keyWord");
        if(keyWord != null) {
            try {
                keyWord = URLDecoder.decode(keyWord,"utf-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        String finalKeyWord = keyWord;
        PageInfo pageInfo = persistenceService.search(new Search() {
            @Override
            public int from() {
                int fromNum = (from == null ? 0 : Integer.parseInt(from));
                if (fromNum > 10000) {
                    throw new ESException("too large num 'from'");
                }
                return fromNum;
            }

            @Override
            public String getType() {
                return TypeNameEnmu.new_black.name();
            }

            @Override
            public String getIndex() {
                return TypeNameEnmu.new_black.name();
            }

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getKeyWord() {
                return finalKeyWord;
            }
        });

        return pageInfo;
    }
}
