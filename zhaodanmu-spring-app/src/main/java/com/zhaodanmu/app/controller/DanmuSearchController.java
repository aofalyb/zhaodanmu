package com.zhaodanmu.app.controller;

import com.zhaodanmu.app.api.DouyuDanmuQuery;
import com.zhaodanmu.app.api.IDouyuSearchService;
import com.zhaodanmu.app.api.Response;
import com.zhaodanmu.app.model.DanmuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/douyu")
public class DanmuSearchController {


    @Autowired
    private IDouyuSearchService douyuSearchService;

    @PostMapping("/search")
    public Response search(@RequestBody DouyuDanmuQuery query) {

        String keyword = query.getNn();
        if(Objects.isNull(keyword)) {
            return new Response(-1,"请输入你想要查询的用户昵称");
        }

        if(keyword.contains("*") || keyword.contains("?")) {
            return new Response(-2,"不支持的特殊符号'* ?'");
        }

        List<DanmuModel> danmuModels = douyuSearchService.searchDouyuDanmu(query);
        return new Response(danmuModels);
    }

}
