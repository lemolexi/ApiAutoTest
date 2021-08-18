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
import common.reqparamter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FileName: cookie
 * Author:   wanglin
 * Date:     2021/8/17 9:58
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class cookie {

    private String url;
    private ResourceBundle bundle;
    private CookieStore cookieStore;

    @BeforeTest
    public void Before(){
        bundle = ResourceBundle.getBundle("application", Locale.CANADA);
        url = bundle.getString("test_url");
    }

    @Test
    public void test() throws IOException {
        String  test_url = this.url + bundle.getString("getCookie_uri");
        HttpPost httpPost = new HttpPost(test_url);
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//        CloseableHttpClient client = HttpClients.createDefault();
        httpPost.setHeader("Content-Type","application/json");
        httpPost.setHeader("AppId","1000000");
        JSONObject data = reqparamter.logindata("wanglin","123456");
        StringEntity entity = new StringEntity(data.toString(),"utf-8");
        httpPost.setEntity(entity);
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

    @Test(dependsOnMethods = {"test"})
    public void testget() throws IOException {
        String  test_url = this.url + bundle.getString("test_uri");
        HttpGet httpGet = new HttpGet(test_url);
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
        HttpResponse response = client.execute(httpGet);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
    }


}
