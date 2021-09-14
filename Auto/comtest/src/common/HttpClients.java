/**
 * FileName: HttpClients
 * Author:   hupo
 * Date:     2020/8/27 17:45
 * Description: 测试
 * version: IT20200827
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package common;


import cn.tsign.commons.util.config.redissonlock.RedissLockUtil;
import constants.Constants;
import io.restassured.response.ValidatableResponse;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import tgtest.utils.AccessToolUtil;
import tgtest.utils.MD5Util;
import tgtest.utils.http.HttpConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static common.PostUtil.getTimeStamp;
import static common.PostUtil.hmacSHA256;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static swagger.Util.HttpUtil.doHttpReq;

public class HttpClients {
    private static final String CHARSET = "UTF-8";
    private static final String projectId = "1000000";
    private static final String projectSignature = "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0";

    /**
     * Content-Type: application/x-www-form-urlencoded
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String postFormUrlEncode(String url, List<BasicNameValuePair> params, HttpConfig config) throws Exception {
        Reporter.log("[URL] ：" + url);
        Reporter.log("[Request] ：" + params);

        System.out.println("[URL] ：" + url);
        System.out.println("[Request] ：" + params);

        CloseableHttpClient client = org.apache.http.impl.client.HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String strRes = "";
        try {
            if (null != params) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            if (null != config) {
                if (null != config.getHttpHeader()) {
                    config.getHttpHeader().buildHeader(httpPost);
                }
                if (null != config.getProjectAccesser()) {
                    HttpSigns.signature(httpPost, config);
                }
            }
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                strRes = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Response] ：" + strRes);
        return strRes;
    }

    /**
     * Content-Type: application/x-www-form-urlencoded
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getFormUrlEncode(String url, HttpConfig config) throws Exception {
        Reporter.log("[URL] ：" + url);

        System.out.println("[URL] ：" + url);

        CloseableHttpClient client = org.apache.http.impl.client.HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String strRes = "";
        try {
            httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
            if (config != null) {
                if (null != config.getHttpHeader()) {
                    config.getHttpHeader().buildHeader(httpGet);
                }
                if (null != config.getProjectAccesser()) {
                    HttpSigns.signature(null, config);
                }
            }
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                strRes = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reporter.log("[Response] ：" + strRes);
        System.out.println("[Response] ：" + strRes);
        return strRes;
    }

    /**
     * Request Method: DELETE
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String deleteFormUrlEncode(String url, HttpConfig config) throws Exception {
        Reporter.log("[URL] ：" + url);

        System.out.println("[URL] ：" + url);

        CloseableHttpClient client = org.apache.http.impl.client.HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        String strRes = "";
        try {
            if (null != config.getHttpHeader()) {
                config.getHttpHeader().buildHeader(httpDelete);
            }
            if (config != null) {
                if (null != config.getProjectAccesser()) {
                    HttpSigns.signature(null, config);
                }
            }
            HttpResponse response = client.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                strRes = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reporter.log("[Response] ：" + strRes);
        System.out.println("[Response] ：" + strRes);
        return strRes;
    }

    public static String doPut(String url, JSONObject data, HttpConfig config) throws UnsupportedEncodingException {
        Reporter.log("[URL] ：" + url);
        Reporter.log("[Request] ：" + null);

        System.out.println("[URL] ：" + url);
        System.out.println("[Request] ：" + data);
        //byte[] stream = content.getBytes();
        byte[] stream = data.toString().getBytes("UTF-8");
        // 执行请求
        HttpEntityEnclosingRequestBase req = new HttpPut(url);

        // 设置HTTP请求体
        HttpEntity entity = new ByteArrayEntity(stream, ContentType
                .create(ContentType.APPLICATION_JSON.getMimeType()));
        req.setEntity(entity);

        // 设置HTTP请求头部
        if (null != config.getHttpHeader()) {
            config.getHttpHeader().buildHeader(req);
        }
        //执行请求、获取响应结果

        // 服务器响应数据
        String strRes = new String(
                HttpClients.sendRequest(req, config.getCookieStore()), "UTF-8");

        Reporter.log("[Response] ：" + strRes);
        System.out.println("[Response] ：" + strRes);

        return strRes;
    }

    private static byte[] sendRequest(HttpUriRequest req, CookieStore cookieStore) {

        //执行请求
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient cli = httpClientBuilder.build();

        HttpResponse res;
        InputStream in = null;
        byte[] resp = null;

        try {

            res = cli.execute(req);
            //获取响应
            in = res.getEntity().getContent();
            resp = AccessToolUtil.readStream(in);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                cli.close();
            } catch (IOException e) {
                e.printStackTrace();
                //LOGGER.error("Http request close error", e);
            }

            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //LOGGER.error("Http request close error", e);
                }
            }

        }
        return resp;
    }

    public static JSONObject postRequestWithFile(String url, RequestBody body) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("file", "/D:/TestData/temp/a-signfile/flowable/d01.xlsx",
//                        RequestBody.create(MediaType.parse("application/octet-stream"),
//                                new File("/D:/TestData/temp/a-signfile/flowable/d01.xlsx")))
//                .addFormDataPart("ruleName", "DA0049")
//                .addFormDataPart("ruleNo", "PostMA00349j")
//                .addFormDataPart("ruleType", "1")
//                .addFormDataPart("ruleDescription", "ujsflajglsjgowjfo")
//                .build();

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("x-timevale-project-id", projectId)
                .addHeader("x-timevale-signature", projectSignature)
                .build();

        Response response = client.newCall(request).execute();
        JSONObject json = JSONObject.fromObject(response.body().string());

        Reporter.log("[URL] ：" + url);
        Reporter.log("[Request] ：" + body.toString());
        Reporter.log("[Response] ：" + json);

        System.out.println("[URL] ：" + url);
        System.out.println("[Request] ：" + JSONObject.fromObject(body));
        System.out.println("[Response] ：" + json);

        return json;
    }

    //获取请求的cookie的请求举例
    @Test
    public static void demoGetCookie() {
        String loginUrl = "";
        Object jsonString = "{}";

        loginUrl = "https://smltapi.esign.cn/account-webserver/login/commit/user";
        jsonString = "{\"principal\":\"13758141429\",\"credentials\":\"0659c7992e268962384eb17fafe88364\",\"loginParams\":{\"endpoint\":\"PC\",\"env\":{\"fingerprint\":\"39ce203511a30189444b5294df94682d\"}}}";

        Map<String, String> headers = new HashMap<String,String>();
        headers.put("X-Tsign-Service-GROUP", "DEFAULT");
        headers.put("X-Tsign-Open-Auth-Mode", "simple");
        headers.put("X-Tsign-Open-App-Id", "4438791882");
        headers.put("accept-language", "zh-CN");


        ValidatableResponse validate = given()
                .headers(headers)
                .contentType("application/json; charset=utf-8")
                .body(jsonString)
                .when()
                .post(loginUrl)
                .then();
        System.out.println("[response]:" + validate.extract().response().asString() + "\n");

        validate.log().all(); //打印全部的出参包含响应头信息
        //获取cookie
        Map cookies = validate.extract().cookies();

        //使用
        String url2 = "https://smltapi.esign.cn/v1/oauth2/authorize?redirectUri=http%3A%2F%2F192.168.2.134%3A8086%2Fesign%2Frest%2Fcallback%2Fauth%2FeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTYwNzQzNTk5NSwiYXV0aG9yaXRpZXMiOlsicm9sZV9vdXRlcl9hY2NvdW50Il0sImp0aSI6Ijk2YWViZjIzLTJkNzMtNDQ3Zi05NTJkLTliNTIxNjQ0NWY3MCIsImFjY291bnQiOnsiaWQiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJpbm5lciI6ZmFsc2V9LCJjbGllbnRfaWQiOiIxMDAxMDAxIn0.OYerKczLApBCeaapA9--nwdkkQ0yb-PX_Emc6q8cHhk&responseType=code&appId=4438791743&scope=get_user_info,op_seal&mobile=13758141429&state=sign&lang=zh-CN";

        Map<String, String> headers2 = new HashMap<String,String>();
        headers2.put("Cookie", "sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221763d196e9a510-00570d36dfd0c6-5c41291e-2073600-1763d196e9b3c2%22%2C%22%24device_id%22%3A%221763d196e9a510-00570d36dfd0c6-5c41291e-2073600-1763d196e9b3c2%22%2C%22props%22%3A%7B%22%24latest_referrer%22%3A%22http%3A%2F%2F192.168.2.134%3A8086%2Fesign-web%2Fsign%3FaccountId%3Dc205166f-2c1e-49cf-aa5e-c59312a60816%26processId%3D109947%26flag%3Dtrue%26lang%3Dzh-CN%22%2C%22%24latest_referrer_host%22%3A%22192.168.2.134%22%2C%22%24latest_traffic_source_type%22%3A%22%E5%BC%95%E8%8D%90%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%7D%7D; TSIGN.SESSION.SANDBOX=ed523000-b3a7-4718-8009-c03df649b3d8");
        headers2.put("Origin", "https://smlfront.esign.cn:8870");
        headers2.put("Content-Type", "application/x-www-form-urlencoded");
        headers2.put("Upgrade-Insecure-Requests", "1");
        headers2.put("Referer", "https://smlfront.esign.cn:8870/oauth/show?redirectUri=http%3A%2F%2F192.168.2.134%3A8086%2Fesign%2Frest%2Fcallback%2Fauth%2FeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTYwNzQzNTk5NSwiYXV0aG9yaXRpZXMiOlsicm9sZV9vdXRlcl9hY2NvdW50Il0sImp0aSI6Ijk2YWViZjIzLTJkNzMtNDQ3Zi05NTJkLTliNTIxNjQ0NWY3MCIsImFjY291bnQiOnsiaWQiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJpbm5lciI6ZmFsc2V9LCJjbGllbnRfaWQiOiIxMDAxMDAxIn0.OYerKczLApBCeaapA9--nwdkkQ0yb-PX_Emc6q8cHhk&responseType=code&appId=4438791743&scope=get_user_info,op_seal&mobile=13758141429&state=sign");

        ValidatableResponse response2 = given()
                .headers(headers2)
                .when()
                .post(url2)
                .then();


        System.out.println("[code]:"+response2.extract().statusCode());
        System.out.println("[response2]:" + response2.extract().response().asString() + "\n");

        String url3="https://smltapi.tsign.cn/v2/oauth2/access_token";

        JSONObject data=new JSONObject();
        data.put("appId","4438791743");
        data.put("secret","9fe190b27d87bc32c53ea9f9f8665648");
        data.put("grantType","authorization_code");
        data.put("state","sign");
        data.put("code","b419f8df6e5278ff5cd866f17f0eeaaa");

        Map<String, String> headers3 = new HashMap<String,String>();
        headers3.put("X-Tsign-Open-Authorization-Version", "v2");
        headers3.put("X-Tsign-Open-App-Id", "4438791743");
        headers3.put("Content-Type", "application/x-www-form-urlencoded");

        ValidatableResponse response3 = given()
                .headers(headers3)
                .body(data)
                .when()
                .get(url3)
                .then();

        System.out.println("[code]:"+response3.extract().statusCode());
        System.out.println("[response3]:" + response3.extract().response().asString() + "\n");


    }


    //请求URL中带有path参数的请求举例
    @Test
    public void getPathParameters() {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("X-Tsign-Service-GROUP", "DEFAULT");
        headers.put("X-Tsign-Open-Auth-Mode", "simple");
        headers.put("X-Tsign-Open-App-Id", "4438791882");
        headers.put("accept-language", "zh-CN");

        given().
                headers(headers).
                and().
                pathParam("accountId", "fac610cbd3bd47b9b3d17ccb5ad0c305").
                when().
                get("https://smlopenapi.esign.cn/v1/accounts/{accountId}/getAllInfo").
                then().
                statusCode(200).log().all();
    }

    //post请求的内容格式不同的请求举例
    @Test
    public void getUrlencodedPost() {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("x-timevale-project-id", "1000000");
        headers.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        ValidatableResponse response=given().log().all().
                headers(headers).
                and().
                param("pageIndex", "1").
                param("pageSize", "15").
                param("licenseNumber", "").
                when().
                post("http://172.24.7.3:8035/V1/organizations/innerOrgans/queryByOrgname").
                then().
                log().all();

        System.out.println("[response]:"+response.extract().response().asString());
        response.body("errCode",hasItems("0"));
    }

    @Test
    public void getUrlencodedGet() {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("x-timevale-project-id", "1000000");
        headers.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        String str="{\"organizeId\":\"\",\"organizeNo\":\"Ozml001\"}";
        Map map=new HashMap<String,String>();
        map.put("organizeId","");
        map.put("organizeNo","Ozml001");
        JSONObject jstr=JSONObject.fromObject(str);
        ValidatableResponse response=given().
                headers(headers).
                and().
                params(jstr).
                when().
                get("http://172.24.7.3:8035/V1/organizations/outerOrgans/query").
                then();

        System.out.println("[response]:"+response.extract().response().asString());
        System.out.println("[errCode]:"+response.extract().path("errCode"));
        response.body("errCode",equalTo(0));
    }

    @Test
    public static void demoGetCookie2() {
        String loginUrl = "";
        Object jsonString = "{}";

        loginUrl = "https://smltapi.esign.cn/account-webserver/login/commit/user";
        jsonString = "{\"principal\":\"13758141429\",\"credentials\":\"0659c7992e268962384eb17fafe88364\",\"loginParams\":{\"endpoint\":\"PC\",\"env\":{\"fingerprint\":\"39ce203511a30189444b5294df94682d\"}}}";

        Map<String, String> headers = new HashMap<String,String>();
        headers.put("X-Tsign-Service-GROUP", "DEFAULT");
        headers.put("X-Tsign-Open-Auth-Mode", "simple");
        headers.put("X-Tsign-Open-App-Id", "4438791882");
        headers.put("accept-language", "zh-CN");


        ValidatableResponse validate = given()
                .headers(headers)
                .contentType("application/json; charset=utf-8")
                .body(jsonString)
                .when()
                .post(loginUrl)
                .then();
        System.out.println("[response]:" + validate.extract().response().asString() + "\n");

        validate.log().all(); //打印全部的出参包含响应头信息
        //获取cookie
        Map cookies = validate.extract().cookies();

        //使用
        String url2 = "https://smltapi.esign.cn/v1/oauth2/authorize?redirectUri=http%3A%2F%2F192.168.2.134%3A8086%2Fesign%2Frest%2Fcallback%2Fauth%2FeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTYwNzQzNTk5NSwiYXV0aG9yaXRpZXMiOlsicm9sZV9vdXRlcl9hY2NvdW50Il0sImp0aSI6Ijk2YWViZjIzLTJkNzMtNDQ3Zi05NTJkLTliNTIxNjQ0NWY3MCIsImFjY291bnQiOnsiaWQiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJpbm5lciI6ZmFsc2V9LCJjbGllbnRfaWQiOiIxMDAxMDAxIn0.OYerKczLApBCeaapA9--nwdkkQ0yb-PX_Emc6q8cHhk&responseType=code&appId=4438791743&scope=get_user_info,op_seal&mobile=13758141429&state=sign&lang=zh-CN";

        Map<String, String> headers2 = new HashMap<String,String>();
        headers2.put("Cookie", "sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221763d196e9a510-00570d36dfd0c6-5c41291e-2073600-1763d196e9b3c2%22%2C%22%24device_id%22%3A%221763d196e9a510-00570d36dfd0c6-5c41291e-2073600-1763d196e9b3c2%22%2C%22props%22%3A%7B%22%24latest_referrer%22%3A%22http%3A%2F%2F192.168.2.134%3A8086%2Fesign-web%2Fsign%3FaccountId%3Dc205166f-2c1e-49cf-aa5e-c59312a60816%26processId%3D109947%26flag%3Dtrue%26lang%3Dzh-CN%22%2C%22%24latest_referrer_host%22%3A%22192.168.2.134%22%2C%22%24latest_traffic_source_type%22%3A%22%E5%BC%95%E8%8D%90%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%7D%7D; TSIGN.SESSION.SANDBOX=ed523000-b3a7-4718-8009-c03df649b3d8");
        headers2.put("Origin", "https://smlfront.esign.cn:8870");
        headers2.put("Content-Type", "application/x-www-form-urlencoded");
        headers2.put("Upgrade-Insecure-Requests", "1");
        headers2.put("Referer", "https://smlfront.esign.cn:8870/oauth/show?redirectUri=http%3A%2F%2F192.168.2.134%3A8086%2Fesign%2Frest%2Fcallback%2Fauth%2FeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTYwNzQzNTk5NSwiYXV0aG9yaXRpZXMiOlsicm9sZV9vdXRlcl9hY2NvdW50Il0sImp0aSI6Ijk2YWViZjIzLTJkNzMtNDQ3Zi05NTJkLTliNTIxNjQ0NWY3MCIsImFjY291bnQiOnsiaWQiOiJjMjA1MTY2Zi0yYzFlLTQ5Y2YtYWE1ZS1jNTkzMTJhNjA4MTYiLCJpbm5lciI6ZmFsc2V9LCJjbGllbnRfaWQiOiIxMDAxMDAxIn0.OYerKczLApBCeaapA9--nwdkkQ0yb-PX_Emc6q8cHhk&responseType=code&appId=4438791743&scope=get_user_info,op_seal&mobile=13758141429&state=sign");

        ValidatableResponse response2 = given()
                .headers(headers2)
                .when()
                .post(url2)
                .then();


        System.out.println("[code]:"+response2.extract().statusCode());
        System.out.println("[response2]:" + response2.extract().response().asString() + "\n");

        String url3="https://smltapi.tsign.cn/v2/oauth2/access_token";

        JSONObject data=new JSONObject();
        data.put("appId","4438791743");
        data.put("secret","9fe190b27d87bc32c53ea9f9f8665648");
        data.put("grantType","authorization_code");
        data.put("state","sign");
        data.put("code","b419f8df6e5278ff5cd866f17f0eeaaa");

        Map<String, String> headers3 = new HashMap<String,String>();
        headers3.put("X-Tsign-Open-Authorization-Version", "v2");
        headers3.put("X-Tsign-Open-App-Id", "4438791743");
        headers3.put("Content-Type", "application/x-www-form-urlencoded");

        ValidatableResponse response3 = given()
                .headers(headers3)
                .body(data)
                .when()
                .get(url3)
                .then();

        System.out.println("[code]:"+response3.extract().statusCode());
        System.out.println("[response3]:" + response3.extract().response().asString() + "\n");


    }

    @Test
    public void test() throws Exception {
        demoGetCookie();
//        System.out.println(json);
    }

    //获取accesstoken第一步
    public static void getUrlencodedGetLoginURL001(String projectId) {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("content-type", "text/html;charset=utf-8");
        headers.put("accept-language", "zh-CN");
        String str="{\"isInner\":\"Y\",\"clientId\":"+projectId+",\"state\":\"Inner_abc\",\"redirectUri\":\"abc\"}";
        JSONObject jstr=JSONObject.fromObject(str);
        String path = getUrl();
        ValidatableResponse response=given().
                headers(headers).
                and().
                params(jstr).
                when().
                get(path+":8086/esignpro-web/api/oauth/getSSOUrl").
                then();
//        System.out.println("[response]:"+response.extract().response().asString());
//        System.out.println("[errCode]:"+response.extract().path("errCode"));
//        String url = response.extract().path("data").toString();
        response.body("errCode",equalTo(0));
//        System.out.println(url);
    }
    //获取accesstoken第二步
    public static String getUrlencodedPostLoginURL002(String projectId) {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String path = getUrl();
        String password = DigestUtils.sha256Hex(Constants.USER_PASSWORD + "MIICdQIBADANBgkqhkiG9w0BAQEFAASC");
        ValidatableResponse response=given().
                headers(headers).
                and().
                param("username", Constants.USER_NAME).
                param("password", MD5Util.getMD5(Constants.USER_PASSWORD).toLowerCase()).
//                param("password", password.toLowerCase()).
                when().
                post(path+":8086/esign/authentication/form?client_id="+projectId+"&isInner=Y&response_type=code&state=Inner_abc&redirect_uri="+path +":8020/esign/rest/oauth/callback").
                then();

//        System.out.println("[response]:"+response.extract().response().asString());
        String url = response.extract().path("data").toString();
        int index = url.indexOf("code=");
        String code =url.substring(index+5,index+11);
//        System.out.println(code);
        response.body("status",equalTo(200));
//        System.out.println(url);
        return code;
    }

    public static String getUrlencodedPostLoginURL002(String projectId,String username,String passwordMd5) {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String path = getUrl();
        ValidatableResponse response=given().
                headers(headers).
                and().
                param("username", username).
                param("password", passwordMd5).
                when().
                post(path+":8086/esign/authentication/form?client_id="+projectId+"&isInner=Y&response_type=code&state=Inner_abc&redirect_uri="+path +":8020/esign/rest/oauth/callback").
                then();

//        System.out.println("[response]:"+response.extract().response().asString());
        String url = response.extract().path("data").toString();
        int index = url.indexOf("code=");
        String code =url.substring(index+5,index+11);
//        System.out.println(code);
        response.body("status",equalTo(200));
//        System.out.println(url);
        return code;
    }

    public static String getUrlencodedPostLoginURL002(String projectId,String username,String passwordMd5,String state) {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String path = getUrl();
        ValidatableResponse response=given().
                headers(headers).
                and().
                param("username", username).
                param("password", passwordMd5).
                when().
                post(path+":8086/esign/authentication/form?client_id="+projectId+"&isInner=Y&response_type=code&state="+state+"&redirect_uri="+path +":8020/esign/rest/oauth/callback").
                then();

        System.out.println("[response]:"+response.extract().response().asString());
        String url = response.extract().path("data").toString();
        int index = url.indexOf("code=");
        String code =url.substring(index+5,index+11);
//        System.out.println(code);
        response.body("status",equalTo(200));
//        System.out.println(url);
        return code;
    }

    //获取accesstoken第三步
    public static void getUrlencodedGetLoginURL003(String code) {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("content-type", "text/html;charset=utf-8");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("accept-language", "zh-CN");
        String pathLocal = getUrl();
        String path = pathLocal+":8020/esign/rest/oauth/callback?code="+ code +"&state=Inner_abc";
        ValidatableResponse response=given().
                headers(headers).
                and().
                when().
                get(path).
                then();
//        System.out.println("[response]:"+response.extract().response());
//        System.out.println("[errCode]:"+response.extract().path("errCode"));
    }
    //获取accesstoken第四步
    public static String getUrlencodedPostGetAccessToken(String code,String projectId) {
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/json");
        JSONObject data=new JSONObject();
        data.put("clientId",projectId);
        data.put("code",code);
        data.put("state","Inner_abc");
        String path = getUrl();
        ValidatableResponse response=given().
                headers(headers).
                and().
                body(data).
                when().
                post(path+":8086/esignpro-web/api/oauth/getAccessToken").
                then();

//        System.out.println("[response]:"+response.extract().response());
        JSONObject data2= JSONObject.fromObject((response.extract().response().asString()));
//        System.out.println(data2.getJSONObject("data"));
        String authorization = "bearer " + data2.getJSONObject("data").get("access_token");
        return authorization;
    }
    //获取用户的access_token
    public static String getAccessToken(String projectId) {
        if (projectId==null||projectId=="")
        {
            projectId ="1001000";
        }
        String res = null;
        try {
            getUrlencodedGetLoginURL001(projectId);
            String code =getUrlencodedPostLoginURL002(projectId);
            getUrlencodedGetLoginURL003(code);
            res =getUrlencodedPostGetAccessToken(code,projectId);
        } catch (Exception e) {
            throw new RuntimeException("获取token失败，请检查系统参数用户名密码是否配置正确" + e.getMessage());
        }
//        System.out.println("res = "+res);
        return res;
    }

    public static String getAccessToken(String projectId,String username,String passwordMd5) {
        String res = null;
        try {
        getUrlencodedGetLoginURL001(projectId);
        String code =getUrlencodedPostLoginURL002(projectId,username,passwordMd5);
        getUrlencodedGetLoginURL003(code);
        res =getUrlencodedPostGetAccessToken(code,projectId);
        } catch (Exception e) {
            throw new RuntimeException("获取token失败，请检查系统参数用户名密码是否配置正确" + e.getMessage());
        }
        return res;
    }

    public static String getAccessTokenOuter(String projectId,String username,String passwordMd5) {
        String res = null;
        try {
        getUrlencodedGetLoginURL001(projectId);
        String code =getUrlencodedPostLoginURL002(projectId,username,passwordMd5,"Outer_xyz");
        getUrlencodedGetLoginURL003(code);
        res =getUrlencodedPostGetAccessToken(code,projectId);
        } catch (Exception e) {
            throw new RuntimeException("获取token失败，请检查系统参数用户名密码是否配置正确" + e.getMessage());
        }
        return res;
    }
    @Test
    public void testGetAccessToken()  {
        String res =getAccessToken(null);
//        String res =MD5Util.getMD5(Constants.USER_PASSWORD);
        System.out.println(res);
    }


    public static String getUrl() {
        String path = Constants.OPEN_API_HOST;
        int index = path.indexOf(":8035");
        path = Constants.OPEN_API_HOST.substring(0,index);
        return path;
    }

    @Test
    public void testAllUrl() throws Exception {
        String swaggerUrl = swagger.Constants.TY_WEB_API + "/v2/api-docs";
        String docs = doHttpReq("get", swaggerUrl, null, null, null, null, null, null, null);
        JSONObject json = JSONObject.fromObject(docs);
        JSONObject jsonPath =json.getJSONObject("paths");
        String urls ="";
        Map<String, String> headers = new HashMap<String,String>();
        headers =getTestHeader(null);
        int count =0;
        Iterator iterator = jsonPath.keys();
        while (iterator.hasNext()){
            String url = (String) iterator.next();
            if(url.contains("{"))
            {
               continue;
            }
//            urls = urls + key +"\n";
            getUrlRes(url,count,headers);
//            count++;
        }
        System.out.print(urls);
        System.out.print("总计"+count);

    }
    public void getUrlRes(String url,int count,Map<String, String> headers) {
        String path = Constants.WEB_API_HOST+url;
        JSONObject data =new JSONObject();
        data.put("testKey","testValue");
        ValidatableResponse response=given().
                headers(headers).
                and().
                body(data).
                when().
                post(path).
                then();
//        System.out.println("第"+count+"个接口："+url + "\n");
        String test = response.extract().response().asString();
        if (test.contains("未设置权限"))
        {
            System.out.println(url);
            count ++;
        }
//        System.out.println("第"+count+"个接口："+url + response.extract().response().asString() + "\n");

    }
    public Map<String, String> getTestHeader(String projectId) {
        if (projectId==null||projectId=="")
        {
            projectId ="1001000";
        }
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Accept-Language", Constants.LANGUAGE);
        headers.put("Content-Type", "application/json");
        headers.put("x-timevale-project-id", projectId);
        String authorization = common.HttpClients.getAccessToken(projectId);
        headers.put("Authorization", authorization);
        String timestamp = getTimeStamp();
        String signtoken = null;
        JSONObject data =new JSONObject();
        data.put("testKey","testValue");
        try {
            signtoken = hmacSHA256(data.toString(), timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headers.put("requestTimeStamp", timestamp);
        headers.put("token", signtoken);
        return headers;
    }
    @Test
    public void testWatermark() throws Exception {
//        Map<String, String> headers;
//        headers =getTestHeader(null);
//        int count =0;
//        getUrlRes("/rest/watermark/edit",count,headers);
        RedissLockUtil.tryLock(
                "ADD_DEPARTMENT_DEPARTMENTNUMBER_SUFFIXNINGQUEREDIS2",
                -1,
                60);

    }

}