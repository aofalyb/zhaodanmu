package com.zhaodanmu.app.config;

import com.alibaba.fastjson.JSON;
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
                if(remoteInvoke == null) {
                    Log.httpLogger.error("annotation @RemoteInvoke not found, method: {}",method.toGenericString());
                }

                return "hello";
            }
        });
    }



    private String postJSON(String url, Object json) throws IOException {

        RequestBody body = RequestBody.create(MEDIA_JSON, JSON.toJSONString(json));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

}
