package com.zhaodanmu.app;

import com.zhaodanmu.common.utils.PropertiesUtil;

import java.util.Properties;

public interface CC {

    Properties properties = load();

    static Properties load() {
        return PropertiesUtil.load("douyu.properties");
    }

    String danmuServerHost = properties.getProperty("danmu.server.host").trim();



}
