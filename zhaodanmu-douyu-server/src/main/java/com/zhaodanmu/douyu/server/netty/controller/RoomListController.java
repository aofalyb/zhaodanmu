package com.zhaodanmu.douyu.server.netty.controller;

import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.controller.Controller;
import com.zhaodanmu.douyu.server.DouyuCrawlerClient;
import com.zhaodanmu.douyu.server.RoomDetail;
import com.zhaodanmu.douyu.server.util.ClientHolder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RoomListController implements Controller {

    @Override
    public String getRequestMapping() {
        return "douyu/room/list";
    }

    @Override
    public Object handle(URI uri, String body) {
        Collection<DouyuCrawlerClient> nettyClients = ClientHolder.getAll();

        List<RoomDetail> rooms = nettyClients.stream().map(DouyuCrawlerClient::getRoomDetail).collect(Collectors.toList());

        return rooms;
    }
}
