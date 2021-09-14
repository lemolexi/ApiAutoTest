package common;

import com.google.gson.JsonObject;
import constants.Constants;
import io.restassured.config.EncoderConfig;
import io.restassured.response.ValidatableResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import sun.misc.BASE64Encoder;
import tgtest.utils.AccessToolUtil;
import tgtest.utils.http.HttpClient;
import tgtest.utils.http.HttpConfig;
import tgtest.utils.http.HttpHeader;
import tgtest.utils.http.ProjectAccesser;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static common.HttpClients.*;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;


/**
 * 功能说明：TODO
 *
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月01日上午17:14] 创建方法 by wlf
 */
public class PostUtil {
    private static final Logger log = LoggerFactory.getLogger(PostUtil.class);

    private static CloseableHttpClient httpClient;

    static {
        httpClient =
                HttpClients.custom()
                        .setConnectionManager(PostUtil.ConnectionManagerHolder.cm)
                        .setDefaultRequestConfig(PostUtil.RequestConfigHolder.requestConfig)
                        .setMaxConnPerRoute(100)
                        .setMaxConnTotal(200)
                        .build();
    }

    public static HttpConfig initHttpConfig() {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        httpConfig.setProjectAccesser(new ProjectAccesser(Constants.PROJECT_ID, Constants.PROJECT_SECRET)); //签名验签
        System.out.println("[httpHeader] " + httpHeader.getHeaders().toString());
        return httpConfig;
    }

    public static HttpConfig initHttpConfigWeb(String data) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        httpHeader.addHeader("x-timevale-project-id", "1001000");
        String authorization = common.HttpClients.getAccessToken("1001000");
        httpHeader.addHeader("Authorization", authorization);
//        httpHeader.addHeader("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
        httpHeader = initSignHeader(httpHeader, data);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        httpConfig.setProjectAccesser(new ProjectAccesser(Constants.PROJECT_ID, Constants.PROJECT_SECRET)); //签名验签
//        Reporter.log("[httpConfig] " +httpConfig);
        System.out.println("[httpHeader] " + httpHeader.getHeaders().toString());

        return httpConfig;
    }

    public static HttpConfig initHttpConfig(String projectId, String projectSecret) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        httpConfig.setProjectAccesser(new ProjectAccesser(projectId, projectSecret)); //签名验签
        System.out.println("\n[httpHeader11] " + httpHeader.getHeaders().toString());
        return httpConfig;
    }

    public static HttpConfig initHttpConfig(String projectId, String projectSecret, String data) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        httpHeader = initSignHeader(httpHeader, data);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        httpConfig.setProjectAccesser(new ProjectAccesser(projectId, projectSecret)); //签名验签
        return httpConfig;
    }

    public static HttpConfig initHttpConfigAuthorization(String projectId, String projectSecret, String authorization, String data) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        httpHeader.addHeader("Content-Type", "application/json");
        httpHeader.addHeader("x-timevale-project-id", projectId);
//        if (authorization != null) {
//            httpHeader.addHeader("Authorization", "bearer " + authorization);
//        }
        String authorization2 = common.HttpClients.getAccessToken(projectId);
        httpHeader.addHeader("Authorization", authorization2);
        httpHeader = initSignHeader(httpHeader, data);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
//        if (projectSecret != null && projectSecret.length() > 0) {
//            httpConfig.setProjectAccesser(new ProjectAccesser(projectId, projectSecret));
//        }
        System.out.println("\n[httpHeader] " + httpHeader.getHeaders().toString());
        return httpConfig;
    }

    public static HttpConfig initHttpConfigAuthorization2(String projectId, String projectSecret, String authorization, String data) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        httpHeader.addHeader("Content-Type", "application/json");
        httpHeader.addHeader("x-timevale-project-id", projectId);
        if (authorization != "null") {
//            httpHeader.addHeader("Authorization", "bearer " + authorization);
            httpHeader.addHeader("Authorization", authorization);
        }else {
            String authorization2 = common.HttpClients.getAccessToken(projectId);
            httpHeader.addHeader("Authorization", authorization2);
        }
        httpHeader = initSignHeader(httpHeader, data);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
//        if (projectSecret != null && projectSecret.length() > 0) {
//            httpConfig.setProjectAccesser(new ProjectAccesser(projectId, projectSecret));
//        }
        System.out.println("\n[httpHeader] " + httpHeader.getHeaders().toString());
        return httpConfig;
    }

    private static HttpConfig initHttpConfigDef() {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);
        httpHeader.addHeader("Content-Type", "application/json");
        httpHeader.addHeader("x-timevale-project-id", "1000000");
        httpHeader.addHeader("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        return httpConfig;
    }

    public static HttpConfig initHttpConfigCloud(String appId, String token) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);

        httpHeader.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpHeader.addHeader("X-Tsign-Open-App-Id", appId);
        httpHeader.addHeader("X-Tsign-Open-Token", token);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        System.out.println("\n[httpHeader] " + httpHeader.getHeaders().toString());
        return httpConfig;
    }

    public static HttpConfig initHttpConfigCloud(String appId, String token, String contentType) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.addHeader("Accept-Language", Constants.LANGUAGE);

        httpHeader.addHeader("Content-Type", contentType);
        httpHeader.addHeader("X-Tsign-Open-App-Id", appId);
        httpHeader.addHeader("X-Tsign-Open-Token", token);
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setHttpHeader(httpHeader);
        System.out.println("\n[httpHeader] " + httpHeader.getHeaders().toString());
        return httpConfig;
    }

    public static JSONObject requestDef(JSONObject map, String url) {
        JSONObject jsonRes = null;
        try {
            String content = AccessToolUtil.JsonHelper.toJson(map);
            JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();
            String myRes = HttpClient.postJson(url, postJson, initHttpConfigDef());
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    private static class RequestConfigHolder {
        public static final RequestConfig requestConfig;

        static {
            requestConfig =
                    RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                            .setExpectContinueEnabled(Boolean.TRUE)
                            .setTargetPreferredAuthSchemes(
                                    Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                            .setSocketTimeout(1000)
                            .setConnectTimeout(2000)
                            .setConnectionRequestTimeout(1000)
                            .build();
        }
    }

    private static class ConnectionManagerHolder {
        public static final PoolingHttpClientConnectionManager cm;

        static {
            try {
                X509TrustManager trustManager =
                        new X509TrustManager() {
                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] xcs, String str) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] xcs, String str) {
                            }
                        };
                SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
                ctx.init(null, new TrustManager[]{trustManager}, null);
                SSLConnectionSocketFactory socketFactory =
                        new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
                Registry<ConnectionSocketFactory> socketFactoryRegistry =
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.INSTANCE)
                                .register("https", socketFactory)
                                .build();
                cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                cm.setValidateAfterInactivity(2000);
                cm.setMaxTotal(100);
                cm.setDefaultMaxPerRoute(200);
            } catch (KeyManagementException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static String getRequestUrl(String path) {

        if (path.startsWith("http")) {
            return path;
        } else {
            return Constants.HOST + path;
        }
    }

    public static String doPostFile(
            String url,
            byte[] bytes,
            String name,
            String filename,
            Map<String, String> headers,
            Map<String, String> bodys)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder =
                MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .setCharset(CharsetUtils.get("UTF-8"));
        builder.addBinaryBody(name, bytes, ContentType.MULTIPART_FORM_DATA, filename);
        if (MapUtils.isNotEmpty(bodys)) {
            for (Map.Entry<String, String> map : bodys.entrySet()) {
                builder.addTextBody(map.getKey(), map.getValue());
            }
        }
        httpPost.setEntity(builder.build());
        addHeaders(httpPost, headers);
        String data = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            checkResponseStatusCode(response);
            data = getJsonStr(response);
        } catch (IOException e) {
            log.error("an IOException occurred while executing post request", e);
            throw new RuntimeException(e);
        } finally {
            httpPost.releaseConnection();
        }
        return data;
    }


    public static String doPostFileAsFormData(
            String url,
            byte[] bytes,
            String name,
            String filename,
            Map<String, String> headers,
            Map<String, String> bodys)
            throws UnsupportedEncodingException {
//        url += "?definitionId=12312&organizationId=123123&organizationName=231241";
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder =
                MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .setCharset(CharsetUtils.get("UTF-8"));
        builder.addBinaryBody(name, bytes, ContentType.MULTIPART_FORM_DATA, filename);
        if (MapUtils.isNotEmpty(bodys)) {
            for (Map.Entry<String, String> map : bodys.entrySet()) {
                builder.addTextBody(map.getKey(), map.getValue(), ContentType.TEXT_PLAIN);
            }
        }
        httpPost.setEntity(builder.build());
        addHeaders(httpPost, headers);
        String data = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            checkResponseStatusCode(response);
            data = getJsonStr(response);
        } catch (IOException e) {
            log.error("an IOException occurred while executing post request", e);
            throw new RuntimeException(e);
        } finally {
            httpPost.releaseConnection();
        }
        return data;
    }


    public static String uploadFileWithParam(String url, String localFile, String fileParamName, Map<String, String> params, Map<String, String> headers) {
        String data = null;
        HttpPost httpPost = new HttpPost(url);
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFile));

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // 相当于<input type="file" name="file"/>
            builder.addPart(fileParamName, bin);
            // 相当于<input type="text" name="userName" value=userName>
            builder.addPart(fileParamName,
                    new StringBody(fileParamName, ContentType.create("text/plain", Consts.UTF_8)));
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addPart(key,
                            new StringBody(params.get(key), ContentType.create("text/plain", Consts.UTF_8)));
                }
            }

            HttpEntity reqEntity = builder.build();
            httpPost.setEntity(reqEntity);
            addHeaders(httpPost, headers);

            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);
            checkResponseStatusCode(response);
            data = getJsonStr(response);
            Reporter.log("<pre><b>返回结果:</b>\n" + data + "</pre>");
            System.out.println("[Response] ：" + data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String uploadPrepareInfo(
            String url,
            byte[] bytes,
            String name,
            String filename,
            Map<String, String> headers,
            Map<String, String> bodys)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder =
                MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .setCharset(CharsetUtils.get("UTF-8"));
        builder.addBinaryBody(name, bytes, ContentType.MULTIPART_FORM_DATA, filename);
        if (MapUtils.isNotEmpty(bodys)) {
            for (Map.Entry<String, String> map : bodys.entrySet()) {
                builder.addTextBody(map.getKey(), map.getValue());
            }
        }
        httpPost.setEntity(builder.build());
        addHeaders(httpPost, headers);
        String data = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            checkResponseStatusCode(response);
            data = getJsonStr(response);
        } catch (IOException e) {
            log.error("an IOException occurred while executing post request", e);
            throw new RuntimeException(e);
        } finally {
            httpPost.releaseConnection();
        }
        return data;
    }

    public static String getOpenAPIUrl(String path) {
        return Constants.OPEN_API_HOST + path;
    }

    public static JSONObject request(JsonObject jsonData, String url) {
        JSONObject jsonRes = null;
        try {
            String myRes = "";
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfig());
            } else {
                myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfigWeb(jsonData.toString()));
            }
            if (null != myRes || myRes.length() != 0) {
                jsonRes = JSONObject.fromObject(myRes);
                return jsonRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject request(JsonObject jsonData, String url, boolean isOpenApi) {
        JSONObject jsonRes = null;
        try {
            String myRes = "";
            if (isOpenApi == true) {
                myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfig());
            } else {
                myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfigWeb(jsonData.toString()));
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static String getJsonStr(HttpResponse httpResponse) {
        String resp = "";
        HttpEntity entity = httpResponse.getEntity();
        try {
            resp = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (IOException e) {
            log.error("an IOException occurred while get content", e);
            throw new RuntimeException(e.getMessage());
        }
        return resp;
    }

    public static JSONObject request(JsonObject jsonData, String url, String projectId, String projectSecret) {
        JSONObject jsonRes = null;
        try {
            String myRes = "";
            url = getRequestUrl(url);
            if (url.contains(Constants.OPEN_API_HOST)){
                myRes = HttpClient.postJson(url, jsonData, initHttpConfig(projectId, projectSecret, jsonData.toString()));
            }else {
                myRes = HttpClient.postJson(url, jsonData, initHttpConfigAuthorization2(projectId, projectSecret, "null",jsonData.toString()));
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject requestAuthorization(JsonObject jsonData, String url, String projectId, String projectSecret, String token) {
        JSONObject jsonRes = null;
        try {
            String myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfigAuthorization2(projectId, projectSecret, token, jsonData.toString()));
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject request(JsonObject jsonData, String url, String projectId, String projectSecret, String token) {
        JSONObject jsonRes = null;
        try {
            String myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfigAuthorization2(projectId, projectSecret, token, jsonData.toString()));
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * @Author zyy (beryl zhang)
     * @Date 2018-08-01 15:36
     * @param:[] 将url匹配
     * @Description add IT-20180731
     * @version
     */
//    public static JSONObject requestTest(JsonObject jsonData, String url) {
//        JSONObject jsonRes = null;
//        try {
//            String myRes = HttpClient.postJson(url, jsonData, initHttpConfig());
//            jsonRes = JSONObject.fromObject(myRes);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonRes;
//    }
    public static JSONObject request(Map<String, Object> map, String url) {
        //请求数据转为JSON字节流,作为HTTP请求体
        String content = AccessToolUtil.JsonHelper.toJson(map);
        JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();
        return PostUtil.request(postJson, url);
    }

//    public static JSONObject request(String url) {
//        //请求数据转为JSON字节流,作为HTTP请求体
//        return PostUtil.request(url);
//    }

    public static void addHeaders(HttpRequestBase request, Map<String, String> headers) {
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> head : headers.entrySet()) {
                request.addHeader(head.getKey(), head.getValue());
            }
        }
    }

    public static JSONObject request(Map<String, Object> map, String url, String projectId, String projectSecret) {
        //请求数据转为JSON字节流,作为HTTP请求体
        String content = AccessToolUtil.JsonHelper.toJson(map);
        JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();
        return PostUtil.request(postJson, url, projectId, projectSecret);
    }

    public static JSONObject request(Map<String, Object> map, String url, String projectId, String projectSecret, String token) {
        //请求数据转为JSON字节流,作为HTTP请求体
        String content = AccessToolUtil.JsonHelper.toJson(map);
        JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();
        return PostUtil.request(postJson, url, projectId, projectSecret, token);
    }

    public static JSONObject doPut(String url, byte[] stream, HttpConfig config) {
        JSONObject jsonRes = null;
        try {
            String myRes = HttpClient.doPut(url, stream, config);
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doPut(JSONArray path, JSONObject data, String url, String appId, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        try {
            String myRes;
            myRes = common.HttpClients.doPut(url, data, initHttpConfigCloud(appId, token));
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;

    }

    public static JSONObject doGet(String param, String url) {
        JSONObject jsonRes = null;
        String uri = null;
        if (param == null || param.equals("")) {
            url = getRequestUrl(url);
        } else {
            url = getRequestUrl(url) + "?" + param;
        }

        try {
            String myRes;
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.doGet(url, initHttpConfig());
            } else {
                if (url.contains(Constants.WEB_API_HOST)) {
                    uri = url.replaceAll(Constants.WEB_API_HOST, "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                } else {
                    uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                }
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    private static void checkResponseStatusCode(CloseableHttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < HttpStatus.SC_OK || statusCode > HttpStatus.SC_MULTI_STATUS) {
            throw new RuntimeException("Http 请求失败,状态码：" + statusCode);
        }
    }

    public static JSONObject doGet(String param, String url, String projectId, String projectSecert, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        if (param.equals("") || param == null) {
            url = getRequestUrl(url);
        } else {
            url = getRequestUrl(url) + "?" + param;
        }

        try {
            String myRes;
            if (url.contains(Constants.WEB_API_HOST)) {
                uri = url.replaceAll(Constants.WEB_API_HOST, "");
                myRes = HttpClient.doGet(url, initHttpConfigAuthorization2(projectId, projectSecert, token, uri));
            } else {
                uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                myRes = HttpClient.doGet(url, initHttpConfigAuthorization2(projectId, projectSecert, token, uri));
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doGet(Map parameterMap, String url, String projectId, String projectSecert, String token) {
        JSONObject jsonRes = null;
        StringBuffer parameterBuffer = new StringBuffer();
        String uri = null;
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }

        try {
            String myRes;
            if (url.contains(Constants.WEB_API_HOST)) {
                uri = url.replaceAll(Constants.WEB_API_HOST, "");
                myRes = HttpClient.doGet(url, initHttpConfigAuthorization(projectId, projectSecert, token, uri));
            } else {
                uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                myRes = HttpClient.doGet(url, initHttpConfigAuthorization(projectId, projectSecert, token, uri));
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doGet(JSONObject path, Map parameterMap, String url, String projectId, String projectSecert, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        /*if (path != null || path.size() > 0) {
            try {
                URIBuilder uriBuilder = new URIBuilder(url);
                List<NameValuePair> list = new LinkedList<>();
                list.add(new BasicNameValuePair(path.getString("key"), path.getString("value")));
                uriBuilder.setParameters(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        if (path != null || path.size() > 0) {
            String oldStr = "{" + path.getString("key") + "}";
            String newStr = path.getString("value");
            url = url.replace(oldStr, newStr);
        }

        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }


        try {
            String myRes;
            if (url.contains(Constants.WEB_API_HOST)) {
                uri = url.replaceAll(Constants.WEB_API_HOST, "");
                myRes = HttpClient.doGet(url, initHttpConfigAuthorization(projectId, projectSecert, token, uri));
            } else {
                uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                myRes = HttpClient.doGet(url, initHttpConfigAuthorization(projectId, projectSecert, token, uri));
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * @Description: 公有云接口调用
     * @Author: wenmin
     * @Date: 2020/9/7 20:12
     * @Param:
     * @Return:
     */
    public static JSONObject doGet(JSONArray path, Map<String, String> parameterMap, String url) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }

        try {
            String myRes;
            myRes = getFormUrlEncode(url, null);
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doGet(JSONArray path, Map<String, String> parameterMap, String url, String appId, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }

        try {
            String myRes;
            myRes = getFormUrlEncode(url, initHttpConfigCloud(appId, token));
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * @Description: 公有云接口调用
     * @Author: wenmin
     * @Date: 2020/9/7 20:12
     * @Param:
     * @Return:
     */
    public static JSONObject doDelete(JSONArray path, Map<String, String> parameterMap, String url) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }

        try {
            String myRes;
            myRes = deleteFormUrlEncode(url, null);
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * @author: wenmin
     * @date: 2020/12/9 17:19
     * @description: 公有云的接口调用，方法是DELETE
     **/
    public static JSONObject doDelete(JSONArray path, Map<String, String> parameterMap, String url, String appId, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }

        try {
            String myRes;
            myRes = deleteFormUrlEncode(url, initHttpConfigCloud(appId, token));
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }


    /**
     * @author: wenmin
     * @date: 2020/12/9 17:59
     * @description: 通过指定信息，调用http请求
     **/
    public static String doHttpReq(String httpMethod, String url, JSONObject requestParams, JSONObject queryParams, JSONObject formParams, JSONObject pathParams, JSONObject header, JSONObject multiparts, JSONObject body) {
        final String urllog="";

        try {
            if (header.has("appid")) {
                header.put("Accept-Language", Constants.LANGUAGE);
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Tsign-Open-App-Id", header.getString("appid"));
                header.put("X-Tsign-Open-Token", header.getString("token"));
            }

            ValidatableResponse response = null;
            switch (httpMethod.toLowerCase()) {
                case "post":
                    response = given()
                            .headers(header)
                            .pathParams(pathParams)
                            .body(body)
                            .when().log().all()
                            .post(url)
                            .then();
                    break;
                case "get":
                    response = given()
                            .headers(header)
                            .pathParams(pathParams)
                            .params(requestParams)
                            .when().log().all()
                            .get(url)
                            .then();
                    break;
                case "delete":
                    if (requestParams == null) {
                        response = given()
                                .config(config().encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("UTF-8", "application/json")))
                                .headers(header)
                                .filter((requestSpec, responseSpec, ctx) -> {
                                    requestSpec.pathParams(pathParams);
                                    return ctx.next(requestSpec, responseSpec);
                                })
                                .when().log().all()
                                .delete(url)
                                .then();
                    } else {
                        response = given()
                                .headers(header)
                                .pathParams(pathParams)
                                .filter((requestSpec, responseSpec, ctx) -> {
                                    requestSpec.params(requestParams);
                                    return ctx.next(requestSpec, responseSpec);
                                })
                                .when()
                                .delete(url)
                                .then();
                    }
                    break;
                case "put":
                    response = given()
                            .headers(header)
                            .pathParams(pathParams)
                            .params(requestParams)
                            .body(body)
                            .when()
                            .put(url)
                            .then();
                    break;
                default:
                    break;

            }

            TestCaseUtil.printMsg("[code]:" + response.extract().statusCode());
            TestCaseUtil.printMsg("[URL]:" + url);
            TestCaseUtil.printMsg("[header]:" + header.toString());
            if (pathParams != null) {
                TestCaseUtil.printMsg("[path]:" + pathParams.toString());
            }
            if (requestParams != null) {
                TestCaseUtil.printMsg("[params]:" + requestParams.toString());
            }
            if (body != null) {
                TestCaseUtil.printMsg("[body]:" + body.toString());
            }
            TestCaseUtil.printMsg("[response]:" + response.extract().response().asString());

            return response.extract().response().asString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 公有云接口调用
     * @Author: wenmin
     * @Date: 2020/9/7 20:12
     * @Param:
     * @Return:
     */
    public static JSONObject doPost(JSONArray path, List<BasicNameValuePair> params, String url) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        try {
            String myRes;

            myRes = postFormUrlEncode(url, params, null);
            if (!StringUtils.isBlank(myRes)) {
                jsonRes = JSONObject.fromObject(myRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * @description: 公有云方法调用
     * @Param: [path, params, url, appId, token]
     * @return: net.sf.json.JSONObject
     * @author: wenmin
     * @Date: 2020/11/4 20:48
     **/
    public static JSONObject doPost(JSONArray path, List<BasicNameValuePair> params, String url, String appId, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        try {
            String myRes;

            myRes = postFormUrlEncode(url, params, initHttpConfigCloud(appId, token));
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doPost(JSONArray path, Map<String, Object> map, String url, String appId, String token) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        try {
            //myRes = postJson(url,data,initHttpConfigCloud(appId,token));
            String content = AccessToolUtil.JsonHelper.toJson(map);
            JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();
            String myRes = HttpClient.postJson(url, postJson, initHttpConfigCloud(appId, token));
            if (!StringUtils.isBlank(myRes)) {
                jsonRes = JSONObject.fromObject(myRes);
                return jsonRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @description: 给公有云调用的post接口
     * @Param: [path, map, url, appId, token, contentType]
     * @return: net.sf.json.JSONObject
     * @author: wenmin
     * @Date: 2020/12/2 18:07
     **/
    public static JSONObject doPost(JSONArray path, Map<String, Object> map, String url, String appId, String token, String contentType) {
        JSONObject jsonRes = null;
        String uri = null;
        if (path != null && path.size() > 0) {
            for (int i = 0; i < path.size(); i++) {
                Iterator<String> it = path.getJSONObject(i).keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = path.getJSONObject(i).getString(key);
                    String oldStr = "{" + key + "}";
                    String newStr = value;
                    url = url.replace(oldStr, newStr);
                }
            }
        }

        try {
            String content = AccessToolUtil.JsonHelper.toJson(map);
            JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();
            String myRes = HttpClient.postJson(url, postJson, initHttpConfigCloud(appId, token, contentType));
            if (!StringUtils.isBlank(myRes)) {
                jsonRes = JSONObject.fromObject(myRes);
                return jsonRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * author: huaying
     * description: TODO
     * create time: 2019-1-9 17:37
     */
    public static JSONObject doGet(String[] paramName, String[] paramValue, String url) {
        JSONObject jsonRes = null;
        String reqParams = "";
        String uri = null;


        for (int i = 0; i < paramName.length; i++) {
            if (i == 0 & paramValue[0] != "") {
                if (paramValue[i] != null) {
                    reqParams = reqParams + paramName[i] + "=" + paramValue[i];
                } else {
                    break;
                }
            } else {
                if (paramValue[i] != null) {
                    if (reqParams.length() > 0) {
                        reqParams = reqParams + "&" + paramName[i] + "=" + paramValue[i];
                    } else {
                        reqParams = reqParams + paramName[i] + "=" + paramValue[i];
                    }
                } else {
                    break;
                }
            }
        }
        url = getRequestUrl(url) + "?" + reqParams;

        try {
            String myRes;
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.doGet(url, initHttpConfig());
            } else {
                if (url.contains(Constants.WEB_API_HOST)) {
                    uri = url.replaceAll(Constants.WEB_API_HOST, "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                } else {
                    uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                }
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doGet(String[] paramName, String[] paramValue, String url, String projectId, String projectSecret) {
        JSONObject jsonRes = null;
        String reqParams = "";
        String uri = null;


        for (int i = 0; i < paramName.length; i++) {
            if (i == 0 & paramValue[0] != "") {
                if (paramValue[i] != null) {
                    reqParams = reqParams + paramName[i] + "=" + paramValue[i];
                } else {
                    break;
                }
            } else {
                if (paramValue[i] != null) {
                    reqParams = reqParams + "&" + paramName[i] + "=" + paramValue[i];
                } else {
                    break;
                }
            }
        }
        url = getRequestUrl(url) + "?" + reqParams;

        try {
            String myRes;
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.doGet(url, initHttpConfig(projectId, projectSecret));
            } else {
                if (url.contains(Constants.WEB_API_HOST)) {
                    uri = url.replaceAll(Constants.WEB_API_HOST, "");
                    myRes = HttpClient.doGet(url, initHttpConfig(projectId, projectSecret, uri));
                } else {
                    uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                    myRes = HttpClient.doGet(url, initHttpConfig(projectId, projectSecret, uri));
                }
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * author: huaying
     * description: TODO
     * create time: 2019-1-10 16:34
     */

    public static JSONObject doGet(Map<String, Object> params, String url) throws UnsupportedEncodingException {
        JSONObject jsonRes = null;

        StringBuilder reqParams = new StringBuilder(url);
        reqParams.append("?");
        for (Map.Entry entry : params.entrySet()) {
            reqParams.append(entry.getKey()).append("=");
            reqParams.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            reqParams.append("&");
        }
        reqParams.deleteCharAt(reqParams.length() - 1);
        url = getRequestUrl(String.valueOf(reqParams));

        String uri = "";
        try {
            String myRes;
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.doGet(url, initHttpConfig());
            } else {
                if (url.contains(Constants.WEB_API_HOST)) {
                    uri = url.replaceAll(Constants.WEB_API_HOST, "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                } else {
                    uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                }
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doGet1(Map<String, Object> params, String url) throws UnsupportedEncodingException {
        JSONObject jsonRes = null;

        StringBuilder reqParams = new StringBuilder(url);
        reqParams.append("?");
        for (Map.Entry entry : params.entrySet()) {
            reqParams.append(entry.getKey()).append("=");
            reqParams.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            reqParams.append("&");
        }
        reqParams.deleteCharAt(reqParams.length() - 1);
        url = getRequestUrl(String.valueOf(reqParams));

        String uri = "";
        try {
            String myRes;
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.doGet(url, initHttpConfig());
            } else {
                if (url.contains(Constants.WEB_API_HOST)) {
                    uri = url.replaceAll(Constants.WEB_API_HOST, "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                } else {
                    uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                    myRes = HttpClient.doGet(url, initHttpConfigWeb(uri));
                }
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static JSONObject doGet(Map parameterMap, String url, String projectId, String projectSecert) {
        JSONObject jsonRes = null;
        StringBuffer parameterBuffer = new StringBuffer();
        String uri = null;
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (String) parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
            url = getRequestUrl(url) + "?" + parameterBuffer.toString();
        } else {
            url = getRequestUrl(url);
        }

        try {
            String myRes;
            if (url.contains(Constants.WEB_API_HOST)) {
                uri = url.replaceAll(Constants.WEB_API_HOST, "");
                myRes = HttpClient.doGet(url, initHttpConfig(projectId, projectSecert, uri));
            } else {
                uri = url.replaceAll(Constants.ESIGN_API_HOST + "/esign", "");
                myRes = HttpClient.doGet(url, initHttpConfig(projectId, projectSecert, uri));
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * author: huaying
     * description: TODO
     * create time: 2019-1-9 16:26
     */
    public static JSONObject doDelete(String[] paramName, String[] paramValue, String url) {
        JSONObject jsonRes = null;
        String reqParams = "";

        for (int i = 0; i < paramName.length; i++) {
            if (i == 0 & paramValue[0] != "") {
                reqParams = reqParams + paramName[i] + "=" + paramValue[i];
            } else {
                reqParams = reqParams + "&" + paramName[i] + "=" + paramValue[i];
            }
        }
        url = getRequestUrl(url) + "?" + reqParams;

        try {
            String myRes = HttpClient.doDelete(url);
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    /**
     * 功能描述:根据入参[Y/N]判断是否执行请求
     *
     * @param
     * @return
     * @author jcy
     * @date 2018-8-8 17:24
     */

    public static JSONObject executeRequest(JsonObject jsonData, String url, String isRun) throws Exception {
        JSONObject jsonRes = null;
        try {
            if (isRun.equals("是") || isRun.equals("Y")) {
                String myRes;
                if (url.contains(Constants.OPEN_API_HOST)) {
                    myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfig());
                } else {
                    myRes = HttpClient.postJson(getRequestUrl(url), jsonData, initHttpConfigWeb(jsonData.toString()));
                }
                jsonRes = JSONObject.fromObject(myRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static String getTimeStamp() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return timestamp;
    }

    public static String hmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key =
                new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return byte2String(array);
    }

    private static String byte2String(byte[] b) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }

    public static HttpHeader initSignHeader(HttpHeader httpHeader, String data) {
        String timestamp = getTimeStamp();
        String signtoken = null;
        try {
            signtoken = hmacSHA256(data, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpHeader.addHeader("requestTimeStamp", timestamp);
        httpHeader.addHeader("token", signtoken);
//        System.out.println("时间戳: " + timestamp);
//        System.out.println("签名: " + signtoken);
        return httpHeader;
    }

    public static void main(String[] args) {
        String token = null;
        String time = getTimeStamp();
        try {
            token = hmacSHA256("{}",time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("token: " + token);
        System.out.println("requestTimeStamp: " + time);

    }

    //支持Content-Type: application/x-www-form-urlencoded 格式的post请求
    public static JSONObject request(List<BasicNameValuePair> params, String url) {
        JSONObject jsonRes = null;
        try {
            String myRes = "";
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = postFormUrlEncode(getRequestUrl(url), params, initHttpConfig());
            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;

    }

    public static JSONObject requestWilling(Map<String, Object> map, String url, HttpConfig config) {
        JSONObject jsonRes = null;

        String content = AccessToolUtil.JsonHelper.toJson(map);
        JsonObject postJson = AccessToolUtil.JsonHelper.fromString(content).getAsJsonObject();

        try {
            String myRes = "";
            if (url.contains(Constants.OPEN_API_HOST)) {
                myRes = HttpClient.postJson(getRequestUrl(url), postJson, config);
            } else {
                myRes = HttpClient.postJson(getRequestUrl(url), postJson, config);

            }
            jsonRes = JSONObject.fromObject(myRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }
}
