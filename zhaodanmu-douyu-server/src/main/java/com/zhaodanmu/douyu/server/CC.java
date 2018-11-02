package com.zhaodanmu.douyu.server;

import com.zhaodanmu.common.utils.PropertiesUtil;

import java.util.Properties;

public interface CC {

    Properties properties = load();

    static Properties load() {
        return PropertiesUtil.load("douyu.properties");
    }

    String[] rooms = properties.getProperty("rooms").trim().split(",");

    String esHost = properties.getProperty("es.host").trim();
    int esPort = Integer.parseInt(properties.getProperty("es.port").trim());

    String httpHost = properties.getProperty("http.host").trim();
    int httpPort = Integer.parseInt(properties.getProperty("http.port").trim());

    String redisHost = properties.getProperty("redis.host").trim();
    int redisPort = Integer.parseInt(properties.getProperty("redis.port").trim());

}
