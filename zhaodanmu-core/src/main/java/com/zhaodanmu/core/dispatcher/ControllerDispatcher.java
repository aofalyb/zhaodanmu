package com.zhaodanmu.core.dispatcher;

import com.zhaodanmu.common.MyRuntimeException;
import com.zhaodanmu.common.URI;
import com.zhaodanmu.common.utils.Log;
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

        if(controller == null) {
            Log.httpLogger.debug("404 not found: {}",uri);
            return new MappingResult(false,null,"404 not found: /" + uri.getPath(),null);
        }

        try {
            Object handle = controller.handle(uri, body);
            return new MappingResult(handle);
        } catch (Exception e) {
            Log.sysLogger.error("handle failed,uri:{}",uri,e);
            if(e instanceof MyRuntimeException) {
                return new MappingResult(false,null,e.getMessage(),e);
            }
            return new MappingResult(false,null,"unknown error",e);
        }

    }

    public void register(Controller controller) {
        controllerMap.put(controller.getRequestMapping(), controller);
    }

}
