package testNg;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CookieHttp {
     private String url;
     private ResourceBundle bundle;//用于读取配置文件
     private CookieStore store;//用于存储cookies信息

     @BeforeTest
     public void beforeTest() {

        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        //上行代码用于读取配置文件，baseName和类在同一目录的resource文件中
        url = bundle.getString("test.url");
        //上行代码是获取配置文件中的域名
        }
      @Test
     public void getTestCookie() throws IOException {
        String result;
        String uri = bundle.getString("getCookies.uri");
        //以上代码是获取配置文件中的getCookies.uri对应的路径
        String testurl = this.url + uri;
        HttpGet get = new HttpGet(testurl);
        System.out.println("这是testurl的地址" + testurl);
        //        HttpClient client = new DefaultHttpClient(); HttpClient无法获取cookie信息
        DefaultHttpClient client = new DefaultHttpClient();
        //创建HttpClient对象，用于执行get请求
        HttpResponse response = client.execute(get);
        System.out.println("这是response的值" + response);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        //以下代码是获取cookie信息
        this.store = client.getCookieStore();
        List<Cookie> cookkielist = store.getCookies();
        for (Cookie cookie : cookkielist) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("cookie name=" + name + "  cookie value=" + value);
            }


     }

     @Test(dependsOnMethods = {"getTestCookie"})
     public void postTestMethods() throws IOException {
        String uri = bundle.getString("testPostWithCookies.uri");
        //以上代码是获取配置文件中的getCookies.uri对应的路径
        String testurl = this.url + uri;
        DefaultHttpClient client = new DefaultHttpClient();//声明一个client对象用来进行方法的执行
        HttpPost post = new HttpPost(testurl);//什么一个post方法
        JSONObject param = new JSONObject();//添加参数 接口要求post参数的格式是json格式
        param.put("name", "zhangshan");
        param.put("age", "18");
        post.setHeader("Accept-Encoding", "gzip, deflate"); //设置请求信息,设置header
        post.setHeader("Content-Type", "application/json"); //设置请求信息,设置header
        StringEntity entity = new StringEntity(param.toString(), "utf-8");//将参数信息添加到方法中
        post.setEntity(entity);//将post方法和参数绑定在一起
        String result; //声明一个对象，进行响应结果的存储
        client.setCookieStore(this.store);//设置cookies信息
        HttpResponse response = client.execute(post);//执行post方法
        result = EntityUtils.toString(response.getEntity());//获取响应结果
        System.out.println(result);
        JSONObject resultJson = new JSONObject(result); //将返回的响应结果字符串转换成json对象
        String success = resultJson.getString("zhangshan");//获取响应的参数值
        int status = resultJson.getInt("status");
        //    String status = (String) resultJson.get("status");//获取响应的参数值
        Assert.assertEquals("成功", success); //处理结果，判断期望结果和实际结果

        Assert.assertEquals(1, status);


     }

}