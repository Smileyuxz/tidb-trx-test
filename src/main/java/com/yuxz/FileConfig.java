package com.yuxz;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @package: com.tidb
 * @class: com.yuxz.FileConfig
 * @description: 配置文件解析
 * @author: yuxiuzhen
 * @date: Created in 2020/12/22 1:39 AM
 * @version: V1.0
 */
public class FileConfig {

    private static Properties prop;

    static {
        try {
            prop = loadProperties("/tidb-datasource.properties");
            if (prop == null) {
                System.out.println("配置文件不存在,请检查: tidb-datasource.properties");
            }
        } catch (Exception e) {
            System.out.println("系统配置获取失败: tidb-datasource.properties");
        }
    }

    public static String getFileProperties(String key, String defaultValue) {
        String value = getPropertyValue(prop, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public static String getPropertyValue(Properties prop, String key) {
        if (prop == null) {
            return null;
        }
        String value = prop.getProperty(key);
        if (value == null || value.length() == 0) {
            return null;
        }
        try {
            value = new String(value.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return value;
    }

    public static Properties loadProperties(String cpPath) throws Exception {
        InputStream is = FileConfig.class.getResourceAsStream(cpPath);
        if (is == null) {
            return null;
        }
        Properties prop = new Properties();
        prop.load(is);
        is.close();
        return prop;
    }
}
