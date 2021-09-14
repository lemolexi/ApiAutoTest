package db;

import tgtest.init.SystemProperties;

public class DBConfig {
//    public static String getValue(String key) {
//        System.out.println(key);
//        Properties prop = new Properties();
//        InputStream inputStream = null;
//        try {
//            inputStream = DBConfig.class.getResourceAsStream("/dbconfig.properties");
//            prop.load(inputStream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return prop.getProperty(key);
//    }
//
//    public static void main(String[] args) {
//        System.out.println(getValue("test.esignpro_service.url"));
////        System.out.println(getValue1("test.esignpro_service.url"));
//    }
    public static String getValue(String key) {
        return SystemProperties.getDBValue(key);
    }
}
