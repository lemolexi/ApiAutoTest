/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openras-utils <br/>
 * 文件名：SystemProperties.java <br/>
 * 包：com.timevale.config <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2017年4月8日下午2:57:18]创建文件 by Administrator
 */
package common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utils.Constants;

/**
 * 类名：SystemProperties.java <br/>
 * 功能说明：TODO <br/>
 * 修改历史： <br/>
 * 1.[2017年4月8日下午2:57:18]创建类 by
 */
public class SystemProperties {

    static String configfile;

    static PropertiesConfiguration p;

    public static void initData(String env) {
        configfile = String.format("%s.properties", env);
        p = new PropertiesConfiguration();
        p.setEncoding("UTF-8");
        //System.out.println("conf: "+configfile);

        try {
            p.load(configfile);
        } catch (ConfigurationException e) {
            System.out.println("load configuration failed :" + e.getMessage());
        }
    }

    /**
     * 获取propertity文件中配置的数据
     */
    public static String getPropValue(String propertyName) {
        initData(utils.Constants.ENVI);
        return p.getString(propertyName);
    }

    public static String getDBValue(String propertyName) {
        initData(Constants.DBENVI);
        return p.getString(propertyName);
    }
}
