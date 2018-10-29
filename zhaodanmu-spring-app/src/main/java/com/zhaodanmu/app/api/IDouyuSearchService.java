package com.zhaodanmu.app.api;

import com.zhaodanmu.app.aspect.RemoteInvoke;
import com.zhaodanmu.app.model.DanmuModel;

import java.util.List;

public interface IDouyuSearchService extends Service {


    @RemoteInvoke(requestMapping = "hhh")
    List<DanmuModel> searchDouyuDanmu(DouyuDanmuQuery query);

}
