package demo;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FileName: CookieHttp
 * Author:   wanglin
 * Date:     2021/8/20 16:04
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CookieHttp {

    private CookieStore cookieStore;
    private String url;
    private ResourceBundle bundle;

    @BeforeTest
    public void Before(){
        bundle = ResourceBundle.getBundle("application", Locale.CANADA);
        url = bundle.getString("test_url");
    }

    @Test
    public void testCookie() throws IOException {
        String  testUrl = this.url + bundle.getString("login.uri");
        HttpPost httpPost = new HttpPost(testUrl);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        //设置headers
        httpPost.setHeader("Content-Type","application/json");
        //设置参数
        JSONObject data = new JSONObject();
        data.put("username","admin");
        data.put("password","admin");
        StringEntity entity = new StringEntity(data.toString(),"utf-8");
        httpPost.setEntity(entity);
        //执行请求
        HttpResponse response = httpClient.execute(httpPost);
        this.cookieStore = cookieStore;
        List<Cookie> cookkielist = this.cookieStore.getCookies();
        for (Cookie cookie:cookkielist) {
            String JSESSIONID = cookie.getName();
            String cookie_user = cookie.getValue();
            System.out.println("cookie name=" + JSESSIONID + "  cookie value=" + cookie_user);
        }
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
    }

//    @Test(dependsOnMethods = {"testCookie"})
//    public void test1() throws IOException {
//        String  testUrl = this.url + bundle.getString("post.uri");
//
//        HttpPost httpPost1 = new HttpPost(testUrl);
//
//        httpPost1.setHeader("Content-Type","application/json");
//
////        JSONObject parame = new JSONObject();
////        parame.put("user","wanglin");
////        parame.put("pwd","wanglin");
//
////        StringEntity entity = new StringEntity(parame.toString(),"utf-8");
////        httpPost1.setEntity(entity);
//
//        CloseableHttpClient httpClient1 = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
//
//        HttpResponse response = httpClient1.execute(httpPost1);
//
//        String result = EntityUtils.toString(response.getEntity());
//        System.out.println(result);
//    }

    @Test(dependsOnMethods = {"testCookie"})
    public void testpostcookie() throws IOException {
        String url = this.url + bundle.getString("post_uri");
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
        httpPost.setHeader("Content-Type","application/json");
        JSONObject par = new JSONObject();
        par.put("user","wanglin");
        par.put("pwd","wanglin");
        StringEntity entity = new StringEntity(par.toString(),"UTF-8");
        httpPost.setEntity(entity);
        HttpResponse response = client.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        System.out.println(this.cookieStore);
    }

    @Test(dependsOnMethods = {"testCookie"})
    public void testgetuser() throws IOException {
        String  test_url = this.url + bundle.getString("getuser_uri");
        HttpGet httpGet = new HttpGet(test_url);
        httpGet.setHeader("Content-Type","application/json");
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
        HttpResponse response = client.execute(httpGet);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
    }
}
