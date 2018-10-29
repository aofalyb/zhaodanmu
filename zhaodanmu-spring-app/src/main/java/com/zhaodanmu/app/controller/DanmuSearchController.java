package com.zhaodanmu.app.controller;

import com.zhaodanmu.app.api.DouyuDanmuQuery;
import com.zhaodanmu.app.api.IDouyuSearchService;
import com.zhaodanmu.app.api.Response;
import com.zhaodanmu.app.model.DanmuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/douyu/danmu")
public class DanmuSearchController {

    @Autowired
    private IDouyuSearchService douyuSearchService;

    @GetMapping("/_q/{keyword}")
    public Response search(@PathVariable String keyword) {

        List<DanmuModel> danmuModels = douyuSearchService.searchDouyuDanmu(new DouyuDanmuQuery());
        return new Response(danmuModels);
    }

}
