package com.course.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * FileName: TestConfig
 * Author:   wanglin
 * Date:     2021/8/20 11:29
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class TestConfig {

    public static String loginuri;
    public static String updateUserInfouri;
    public static String getUserListInfouri;
    public static String getUserInfouri;
    public static String addUseruri;
    public static DefaultHttpClient defaultHttpClient;
    public static CookieStore cookieStore;
}
