package com.course.utils;

import com.course.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FileName: ConfigFile
 * Author:   wanglin
 * Date:     2021/8/20 10:05
 * Description: 获取接口地址
 * version: IT20210820
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ConfigFile {

    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CANADA);

    public static String geturl(InterfaceName name){
        String address = bundle.getString("test.url");
        String uri = "";
        String testUrl;
        if (name == InterfaceName.ADDUSERINFO){
            uri = bundle.getString("addUser.uri");
        }
        if (name == InterfaceName.GETUSERINFO){
            uri = bundle.getString("getUserInfo.uri");
        }
        if (name == InterfaceName.GETUSERLISTINFO){
            uri = bundle.getString("getUserListInfo.uri");
        }
        if (name == InterfaceName.UPDATEUSERINFO){
            uri = bundle.getString("updateUserInfo.uri");
        }
        if (name == InterfaceName.LOGIN){
            uri = bundle.getString("login.uri");
        }
        testUrl = address + uri;
        return testUrl;
    }
}
