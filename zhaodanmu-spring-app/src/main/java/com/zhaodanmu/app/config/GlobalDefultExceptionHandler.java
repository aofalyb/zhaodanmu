package com.zhaodanmu.app.config;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.app.api.Response;
import com.zhaodanmu.common.utils.Log;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/10/30.
 */
@RestControllerAdvice
public class GlobalDefultExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Response defaultExceptionHandler(HttpServletRequest request, Exception e) {
        Log.sysLogger.error("",e);
        Log.sysLogger.error("################## ERROR START #######################");
        Log.sysLogger.error("URI: {}",request.getRequestURI());
        Log.sysLogger.error("QueryString: {}",request.getQueryString());
        Log.sysLogger.error("Remote: {}",request.getRemoteAddr());
        Log.sysLogger.error("################## ERROR END #########################");
        return new Response(-100,"服务器未知错误，我们已记录相关信息");
    }
}
