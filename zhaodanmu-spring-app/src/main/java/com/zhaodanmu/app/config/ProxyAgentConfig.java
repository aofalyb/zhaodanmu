package com.zhaodanmu.app.config;

import com.zhaodanmu.app.api.IDouyuSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class ProxyAgentConfig {

    @Bean
    public IDouyuSearchService getDouyuSearchService() {

        return (IDouyuSearchService)newInstance(IDouyuSearchService.class);
    }



    private Object newInstance(Class serviceClass) {

        return Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {




                return "hello";
            }
        });
    }
}
