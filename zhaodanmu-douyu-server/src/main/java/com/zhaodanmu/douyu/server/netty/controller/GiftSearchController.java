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

/**
 * Created by Administrator on 2018/11/8.
 */
public class GiftSearchController implements Controller {

    private PersistenceService persistenceService;


    public GiftSearchController(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public String getRequestMapping() {
        return "douyu/gifts";
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
                return TypeNameEnmu.danmu.name();
            }

            @Override
            public String getIndex() {
                return TypeNameEnmu.danmu.name();
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
        return null;
    }

}
