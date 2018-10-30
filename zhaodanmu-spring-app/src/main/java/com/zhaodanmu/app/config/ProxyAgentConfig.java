package com.zhaodanmu.app.config;

import com.alibaba.fastjson.JSON;
import com.zhaodanmu.app.CC;
import com.zhaodanmu.app.api.IDouyuSearchService;
import com.zhaodanmu.app.aspect.RemoteInvoke;
import com.zhaodanmu.common.utils.Log;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

@Configuration
public class ProxyAgentConfig {

    private static final MediaType MEDIA_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient httpClient = new OkHttpClient()
            .newBuilder()
            .connectionPool(new ConnectionPool(3,1,TimeUnit.MINUTES))
            .retryOnConnectionFailure(false)
            .build();


    @Bean
    public IDouyuSearchService getDouyuSearchService() {

        return (IDouyuSearchService)newInstance(IDouyuSearchService.class);
    }



    private Object newInstance(Class serviceClass) {

        return Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RemoteInvoke remoteInvoke = method.getAnnotation(RemoteInvoke.class);
                String methodName = method.getName();
                String requestMapping = remoteInvoke.requestMapping();
                String returnClass = method.getReturnType().getName();
                Log.httpLogger.debug("invoke method: {},requestMapping: {},arg: {}",methodName,requestMapping,args);
                if(remoteInvoke == null) {
                    Log.httpLogger.error("annotation @RemoteInvoke not found, method: {}",method.toGenericString());
                }

                String respJson = null;
                final String uri = "http://" + CC.danmuServerHost + "/" + requestMapping;
                try {
                    respJson = postJSON(uri, args[0]);
                } catch (Exception e) {
                    Log.httpLogger.error("post uri: {},args: {}",uri,args,e);
                    throw new RuntimeException(e);
                }

                Object object;
                if(returnClass.equals("java.util.Map")) {
                    object = JSON.parseObject(respJson,method.getGenericReturnType());

                } else if(returnClass.equals("java.util.List")) {
                    //解析泛型
                    object = JSON.parseObject(respJson,method.getGenericReturnType());
                } else {
                    object = JSON.parseObject(respJson, Class.forName(returnClass));
                }

                return object;
            }
        });
    }



    private String postJSON(String url, Object json) throws Exception {

        RequestBody body = RequestBody.create(MEDIA_JSON, JSON.toJSONString(json));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

}
