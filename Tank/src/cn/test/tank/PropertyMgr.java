package cn.test.tank;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件里的配置信息
 */
public class PropertyMgr {
    private static  Properties props;
    static {
        try {
            props = new Properties() ;
            props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String get(String key){
        return (String) props.get(key);
    }
}
