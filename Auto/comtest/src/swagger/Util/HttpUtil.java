package swagger.Util;
/**
 * FileName: HttpUtil
 * Author:   wenmin
 * Date:     2020/12/4 14:22
 * Description: http请求处理
 * version:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import net.sf.json.JSONObject;
import org.testng.annotations.Test;
import sun.misc.BASE64Encoder;
import tgtest.utils.http.ProjectAccesser;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.Charset;

import static io.restassured.RestAssured.given;
import static tgtest.utils.http.HttpSign.sign;

/**
 * @author: wenmin
 * Date:     2020/12/4 14:22
 * @Description: http请求处理
 */
public class HttpUtil {
    private static final String CHARSET = "UTF-8";
    private static final String LANGUAGE = "zh_CN";
    private static final String PROJECT_ID = "";
    private static final String PROJECT_SECRET = "";
    private static final String PROJECT_WUKONG = "";
    private static final String PROJECT_SECRET_WUKONG = "";
    private static final String PROJECT_XUANYUAN = "";
    private static final String PROJECT_SECRET_XUANYUAN = "";

    //需要签名验签的请求头信息处理
    public static JSONObject postHeaderWithSignature(JSONObject data, JSONObject headers) {
        JSONObject headers2 = new JSONObject();
        String signature = null;
        try {
            if (headers != null) {
                String projectId = headers.getString("x-timevale-project-id");
                if (projectId.equals("projectId") || projectId.equals("1000000")) {
                    //内置的签名验签白名单
                    headers2.put("x-timevale-project-id", "1000000");
                    headers2.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
                    headers2.put("Accept-Language", LANGUAGE);
                } else {
                    signature = headers.getString("x-timevale-signature");
                    String projectSecret = null;
                    if (signature.equals("signature")) {
                        //匹配内置的项目秘钥
                        if (projectId.equals(PROJECT_ID)) {
                            projectSecret = PROJECT_SECRET;
                        }
                        if (projectId.equals(PROJECT_WUKONG)) {
                            projectSecret = PROJECT_SECRET_WUKONG;
                        }
                        if (projectId.equals(PROJECT_XUANYUAN)) {
                            projectSecret = PROJECT_SECRET_XUANYUAN;
                        }
                        if (projectSecret == null) {
                            projectSecret = headers.getString("x-timevale-signature");
                        }
                    }
                    byte[] stream = data.toString().getBytes(CHARSET);
                    signature = sign(stream, new ProjectAccesser(projectId, projectSecret));

                    headers2.put("x-timevale-project-id", projectId);
                    headers2.put("x-timevale-signature", signature);
                    headers2.put("Accept-Language", LANGUAGE);
                }
                if (headers.has("Content-Type")) {
                    headers2.put("Content-Type", headers.getString("Content-Type"));
                } else {
                    headers2.put("Content-Type", "application/json;charset=UTF-8");
                }
            }
            return headers2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getHeaderWithSignature(JSONObject headers) {
        JSONObject heaers2 = new JSONObject();
        String signature = null;
        try {
            if (headers != null) {
                String projectId = headers.getString("x-timevale-project-id");
                if (projectId.equals("projectId") || projectId.equals("1000000")) {
                    //内置的签名验签白名单
                    heaers2.put("x-timevale-project-id", "1000000");
                    heaers2.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
                    heaers2.put("Accept-Language", LANGUAGE);
                } else {
                    signature = headers.getString("x-timevale-signature");
                    String projectSecret = null;
                    if (signature.equals("signature")) {
                        //匹配内置的项目秘钥
                        if (projectId.equals(PROJECT_ID)) {
                            projectSecret = PROJECT_SECRET;
                        }
                        if (projectId.equals(PROJECT_WUKONG)) {
                            projectSecret = PROJECT_SECRET_WUKONG;
                        }
                        if (projectId.equals(PROJECT_XUANYUAN)) {
                            projectSecret = PROJECT_SECRET_XUANYUAN;
                        }
                        if (projectSecret == null) {
                            projectSecret = headers.getString("x-timevale-signature");
                        }
                    }

                    signature = sign(new byte[]{}, new ProjectAccesser(projectId, projectSecret));

                    heaers2.put("x-timevale-project-id", projectId);
                    heaers2.put("x-timevale-signature", signature);
                    heaers2.put("Accept-Language", LANGUAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //需要换算成token的请求头信息处理
    public static JSONObject tokenHeader(JSONObject data, JSONObject headers) {
        JSONObject headers2 = new JSONObject();
        String requestTimeStamp = String.valueOf(System.currentTimeMillis());
        try {
            String token = hmacSHA256(data.toString(), requestTimeStamp);
            headers2.put("x-timevale-project-id", "1000001");
            headers2.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
            headers2.put("requestTimeStamp", requestTimeStamp);
            headers2.put("token", token);
            headers2.put("Accept-Language", LANGUAGE);
            headers2.put("Content-Type", headers.getString("Content-Type") + "charset=" + CHARSET);
            return headers2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author: wenmin
     * @date: 2021/1/15 10:29
     * @description: 获取页面请求的token
     **/
    private static String hmacSHA256(String data, String key) throws Exception {
        //data指的是请求体或是GET的参数，key指的是请求的时间戳
        Charset UTF_8 = Charset.forName("UTF-8");
        if (key == null) {
            key = String.valueOf(System.currentTimeMillis());
        }
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key =
                new SecretKeySpec(key.getBytes(UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes(UTF_8));

        System.out.println("[timestap]: " + key);
        System.out.println("[token]: " + byte2String(array));
        return byte2String(array);
    }

    private static String byte2String(byte[] b) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }

    //post请求，请求体是json
    @Test
    public static String doPost(JSONObject reqData, String url, JSONObject headers) {

        if (headers == null) {
            headers = new JSONObject();
            headers.put("Content-Type", "application/json");
            headers.put("x-timevale-project-id", "1000000");
            headers.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
            headers.put("Accept-Language", "zh-CN");
        }

        ValidatableResponse response = given()
                .headers(headers)
                .body(reqData)
                .when()
                .post(url)
                .then();

        CheckUtil.printMsg("[code]:" + response.extract().statusCode());
        CheckUtil.printMsg("[URL]:" + url);
        CheckUtil.printMsg("[header]:" + headers.toString());
        CheckUtil.printMsg("[request]:" + reqData.toString());
        CheckUtil.printMsg("[response]:" + response.extract().response().asString());

        return response.extract().response().asString();
    }

    /**
     * @author: wenmin
     * @date: 2020/12/9 17:59
     * @description: 通过指定信息，调用http请求
     **/
    public static String doHttpReq(String httpMethod, String url, JSONObject requestParams, JSONObject queryParams, JSONObject formParams, JSONObject pathParams, JSONObject header, JSONObject multiparts, JSONObject body) {
        try {
            if (header == null) {
                header = new JSONObject();
                header.put("x-timevale-project-id", "1000000");
                header.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
            }
            if (header.has("x-timevale-project-id") && header.getString("x-timevale-project-id").equals("projectId")) {
                header.put("x-timevale-project-id", "1000000");
                header.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
            }

            RequestSpecification requestSpecification = given().log().all();
//            RequestSpecification requestSpecification = given();

            if (header != null) {
                requestSpecification.headers(header);
            }
            if (requestParams != null) {
                requestSpecification.params(requestParams);
            }
            if (queryParams != null) {
                requestSpecification.queryParams(queryParams);
            }
            if (formParams != null) {
                requestSpecification.formParams(formParams);
            }
            if (pathParams != null) {
                requestSpecification.pathParams(pathParams);
            }
            if (multiparts != null && multiparts.has("file")) {
                requestSpecification.multiPart(new File(multiparts.getString("file")));
            }
            if (body != null) {
                requestSpecification.body(body);
            }

            ValidatableResponse response = requestSpecification.request(httpMethod, url).then();
            CheckUtil.printMsg("[response]:" + response.extract().response().asString());
            return response.extract().response().asString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
