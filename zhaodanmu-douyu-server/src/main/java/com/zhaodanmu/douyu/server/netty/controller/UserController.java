package com.zhaodanmu.douyu.server.netty.controller;

import com.zhaodanmu.common.PageInfo;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.api.Search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UserController implements Controller {


    private PersistenceService persistenceService;

    public UserController(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }


    @Override
    public String getRequestMapping() {
        return "douyu/user";
    }

    @Override
    public Object handle(URI uri, String body) {
        String nn;
        try {
            nn = URLDecoder.decode(uri.getParameter("nn"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }


        PageInfo search = persistenceService.search(new Search() {
            @Override
            public String getType() {
                return "user";
            }

            @Override
            public String getIndex() {
                return "user";
            }

            @Override
            public String getKey() {
                return "nn";
            }

            @Override
            public String getKeyWord() {
                return nn;
            }

            @Override
            public int from() {
                return 0;
            }
        });

        return search;
    }
}
