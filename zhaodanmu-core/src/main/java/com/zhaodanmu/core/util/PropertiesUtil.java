package com.zhaodanmu.core.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {


    private static Properties properties;

    public static synchronized void load(String fileName) {
        if(properties == null) {
            properties = new Properties();
        }
        String filePath = System.getProperty("user.dir") + "\\etc\\" + fileName;
        try {
            InputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(bufferedInputStream);
        } catch (Exception e) {
        }
        if(properties.isEmpty()) {
            try {
                properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
            } catch (Exception e) {
            }
        }
    }


    public static String getValue(String key) {
        return properties.getProperty(key);
    }

}
