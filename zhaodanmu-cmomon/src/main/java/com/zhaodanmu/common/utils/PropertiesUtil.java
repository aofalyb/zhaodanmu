package com.zhaodanmu.common.utils;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {


    private static Properties properties;

    public static synchronized Properties load(String fileName) {
        if(properties == null) {
            properties = new Properties();
        }
        String filePath = System.getProperty("user.dir") + "/etc/" + fileName;
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            Log.sysLogger.info("loading config path: {},stream:{}",filePath,inputStream);
            if(inputStream != null){
                properties.load(inputStream);
            }
        } catch (Exception e) {
        }
        if(properties.isEmpty()) {
            try {
                InputStream resource = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
                Log.sysLogger.info("loading resource file: {},stream:{}",fileName,resource);

                if(resource != null) {
                    properties.load(resource);
                }
            } catch (Exception e) {
            }
        }
        return properties;
    }


    public static String getValue(String key) {
        return properties.getProperty(key);
    }

}
