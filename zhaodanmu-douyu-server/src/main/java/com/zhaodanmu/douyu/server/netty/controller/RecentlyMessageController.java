package com.zhaodanmu.douyu.server.netty.controller;

import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;

public class RecentlyMessageController implements Controller {



    @Override
    public String getRequestMapping() {
        return "douyu/hs";
    }

    @Override
    public Object handle(URI uri, String body) {
        return null;
    }
}
