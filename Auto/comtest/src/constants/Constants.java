package constants;

import common.SystemProperties;
import static common.Properties.getPro;


public class Constants {
    static {
        utils.Constants.ENVI = getPro();
        utils.Constants.DBENVI = "dbconf";
    }

    // dbConfig
    public static final String ENVI =  SystemProperties.getPropValue("env_id")+".";
    //内置超级管理员账号及webapi默认账号密码
    public static final String LOGIN_ID = SystemProperties.getPropValue("login_id");
    public static final String USER_NAME = SystemProperties.getPropValue("username");
    public static final String USER_PASSWORD = SystemProperties.getPropValue("password");
    //请求地址
    public static final String HOST = SystemProperties.getPropValue("http_host_webapi");
    public static final String ESIGN_API_HOST = SystemProperties.getPropValue("http_host_signapi");
    public static final String WEB_API_HOST = SystemProperties.getPropValue("http_host_webapi");
    public static final String OPEN_API_HOST = SystemProperties.getPropValue("http_host_openapi");

    /**
     * @Description project
     */
    //纯本地项目
    public static final String PROJECT_ID = SystemProperties.getPropValue("project_inner");
    public static final String PROJECT_SECRET = SystemProperties.getPropValue("project_secret_inner");
    //悟空项目
    public static final String PROJECT_WUKONG= SystemProperties.getPropValue("project_wukong");
    public static final String PROJECT_SECRET_WUKONG= SystemProperties.getPropValue("project_secret_wukong");
    //轩辕项目
    public static final String PROJECT_XUANYUAN= SystemProperties.getPropValue("project_xuanyuan");
    public static final String PROJECT_SECRET_XUANYUAN= SystemProperties.getPropValue("project_secret_xuanyuan");
    //网络版悟空项目
    public static final String PROJECT_INTERNET_WUKONG = SystemProperties.getPropValue("project_internet_wukong");
    public static final String PROJECT_INTERNET_SECRET_WUKONG = SystemProperties.getPropValue("project_secret_internet_wukong");

    /**
     * @Description 其他配置
     */
    //其他特殊字符
    public static final String SYMBOL = "~ !@#$%^&()_+{}'.,;[]=-`（）·！@#￥%……&——+、；‘，。、《》：“©™®℗"; //支持特殊字符集
    // 名称中不支持下面的符号
    public static final String UNSUPPORTSYMBOL= "\\ / : * ? \" < > |";
    //部分生僻字部分集合
    public static final String UNCOMMON_VARCHAR = "蕘垚犇骉镳䶮叕囍囎呝";
    //接口调用的语言
    public static final String LANGUAGE = SystemProperties.getPropValue("accept-language");
    //回调接口地址
    public static final String CALLBACKURL = "http://172.20.62.157/simpleTools/notice/";

    /**
     * @Description 内置账号设置
     */
    public static final String OPENAPI_ACCOUNT_ID= SystemProperties.getPropValue("inner_account_id");
    public static final String OPENAPI_DEPARTMENT_ID= SystemProperties.getPropValue("inner_organize_id");
    public static final String OPENAPI_EXTERNAL_ACCOUNT_ID= SystemProperties.getPropValue("external_account_id");
    public static final String OPENAPI_EXTERNAL_DEPARTMENT_ID= SystemProperties.getPropValue("external_organize_id");

    /**
     * @Description 公有云项目ID
     */
    public static final String CLOUD_APPID = SystemProperties.getPropValue("cloud_appId");
    public static final String CLOUD_SECRET = SystemProperties.getPropValue("cloud_secret");

}
