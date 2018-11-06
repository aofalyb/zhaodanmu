package com.zhaodanmu.douyu.server.netty.controller;

import com.zhaodanmu.common.PageInfo;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;
import com.zhaodanmu.persistence.api.ESException;
import com.zhaodanmu.persistence.api.PersistenceService;
import com.zhaodanmu.persistence.api.Search;
import com.zhaodanmu.persistence.api.TypeNameEnmu;

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
                return null;
            }

            @Override
            public String getKeyWord() {
                return null;
            }
        });

        return pageInfo;
    }
}
