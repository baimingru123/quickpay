package cn.pay.quickpay.utils.platform;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author bmr
 * @time 2019-02-21 9:35
 * 读取properties属性文件
 */
public class PropertiesUtil {
    // 静态块中不能有非静态属性，所以加static
    private static Properties prop = null;



    public static Properties readProperties(String path){
        try {
            prop = new Properties();
            // prop.load(new FileInputStream(new File("C:\\jdbc.properties")));
            prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(path));
            return prop;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }



}
