package common;

public class Properties {
    public static String getPro(){
//        String envdef = "initconftest-1"; //尝鲜版配置
        String envdef = "172.20.11.151"; //稳定版配置
//        String envdef = "sml-3"; //尝鲜版模拟环境配置
        String env = System.getProperty("env");
        if(env != null){
            return env;
        }else {
            return envdef;
        }
    }
}

