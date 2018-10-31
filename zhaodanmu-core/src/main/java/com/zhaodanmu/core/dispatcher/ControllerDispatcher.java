package com.zhaodanmu.core.dispatcher;

import com.zhaodanmu.common.URI;
import com.zhaodanmu.core.common.Result;
import com.zhaodanmu.core.controller.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/31.
 */
public class ControllerDispatcher {

    private static Map<String,Controller> controllerMap = new HashMap<>();


    public Result dispatch(URI uri, String body) {
        Controller controller = controllerMap.get(uri.getPath());

        try {
            Object handle = controller.handle(uri, body);
            return new MappingResult(handle);
        } catch (Exception e) {
            return new MappingResult(false,null,e);
        }

    }

    public void register(Controller controller) {
        controllerMap.put(controller.getRequestMapping(), controller);
    }

}
