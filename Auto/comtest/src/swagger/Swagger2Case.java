package swagger;
/**
 * FileName: Swagger2Case
 * Author:   wenmin
 * Date:     2020/10/14 17:30
 * Description: Swagger2Case
 * version: IT2020${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import swagger.Util.CheckUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import static swagger.Util.ExcelUtil.executeWiteExcel;
import static swagger.Util.ExcelUtil.getXlsExcelData;
import static swagger.Util.HttpUtil.doHttpReq;

public class Swagger2Case {
    private static String swaggerUrl = "";

    public static String PATH_ROOT() {
        return new File("src/swagger/").getAbsolutePath();
    }

    public static void main(String[] args) {
        String url = "/V1/accounts/innerAccounts/list";
        //4个参数：
        //swagger的地址：如 http://192.168.2.134:8035
        // url-swagger上的api接口：如 /V1/accounts/innerAccounts/list
        // rootPath-要生成case存放的目录；
        // pathType-代表传入的路径类型0或者其他值-不处理路径，1-传入的路径是相对路径，2-传入的路径是绝对路径
        Swagger2Case sc=new Swagger2Case();
        sc.swagger2CaseJava(Constants.TY_OPNE_API, url, "D:\\workspace\\idea\\D_Tianying\\tgtest-esignpro-api-5.3\\src\\testcase_init\\", 2);
    }

    /**
     * @description: 具体方法调用封装实现通过swagger获取信息转换成用例
     **/
    public static void swagger2CaseJava(String host, String url, String rootPath, Integer pathType) {
        //pathType: 代表传入的路径类型0或者其他值-不处理路径，1-传入的路径是相对路径，2-传入的路径是绝对路径
        //相对路径

        JSONObject json = getSwaggerInfo(host, url);
        CheckUtil.printMsg("[json]:" + json);
        String filePath = "";
        if (null == pathType) {
            pathType = 0;
        }
        switch (pathType) {
            case 1:
                File directory = new File("");//设定为当前文件夹
                try {
                    rootPath = directory.getAbsolutePath() + "/" + rootPath;
                    System.out.println(directory.getAbsolutePath());//获取绝对路径
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                break;
            default:
                rootPath = null;
                break;
        }
        filePath = writeExcelCase(json, url, rootPath);
        readExcelCaseToJava(filePath);
    }

    /**
     * @author: wenmin
     * @date: 2020/12/4 15:59
     * @description: 从excel解析请求自动生成java类
     **/
    public static void readExcelCaseToJava(String filePath) {
        JSONObject cases = getXlsExcelData(filePath).getJSONObject(0);
        String fileName = cases.getString("fileName");
        File f1 = new File(filePath);
        String rootpath = f1.getParent() + "/../casebyjava/";
        File f2 = new File(rootpath);
        if (!f2.exists()) {
            f2.mkdirs();
        }
        String filePathJAVA = rootpath + "/" + fileName + ".java";
        System.out.println("[test]" + filePathJAVA);
        DateFormat timeFormat = DateFormat.getTimeInstance();
        DateFormat dataFormat = DateFormat.getDateInstance();

        //写注释
//        String packageInfo=String.format("%s.class.getPackage().getName();",fileName);
//        String content = String.format("package %s ;\n",packageInfo);
        String content = "";
        String decription = String.format("\n/**\n" +
                " * FileName: %s\n" +
                " * Author:   $Author$\n" +
                " * Date:     %s %s\n" +
                " * Description: ${description}\n" +
                " * version: $版本号$\n" +
                " * History:\n" +
                " * <author>          <time>          <version>          <desc>\n" +
                " * 作者姓名           修改时间           版本号              描述\n" +
                " */\n", fileName, dataFormat.format(new Date()), timeFormat.format(new Date()));

        String imports = "import swagger.Util.CheckUtil;\n" +
                "import swagger.Constants;\n" +
                "import net.sf.json.JSONObject;\n" +
                "import org.testng.annotations.Test;\n" +
                "import swagger.Util.ReadExcelUtil;\n" +
                "import swagger.RequestBo;\n" +
                "import static swagger.Util.HttpUtil.*;\n";

        String httpMethod = cases.getString("httpMethod");

        String httpRequest = null;

        String description = "CheckUtil.testCaseMsg(bo.getDescription());";
        String header = "JSONObject header = JSONObject.fromObject(bo.getHeader());";
        String requestBody = "JSONObject body = JSONObject.fromObject(bo.getRequestBody());";
        String url = "String url = Constants.TY_OPNE_API + bo.geturl();";

        if ("POST".equals(httpMethod.toUpperCase())) {
            httpRequest = " JSONObject result=JSONObject.fromObject(doHttpReq(\"post\", url,null,null,null,null,postHeaderWithSignature(body, header),null,body));";
        } else if ("GET".equals(httpMethod.toUpperCase())) {
            httpRequest = "JSONObject result=JSONObject.fromObject(doHttpReq(\"get\", url,null,body,null,null,getHeaderWithSignature(header),null,null));";

        } else if ("PUT".equals(httpMethod.toUpperCase())) {
        } else {
        }

        String assertInfo = "CheckUtil.assertBo(bo,result);";
        String apiDetail = String.format(" @Test(dataProvider = \"readexcel\", dataProviderClass = ReadExcelUtil.class)\n" +
                "    public void %s(RequestBo bo) {%s \n" +
                " %s\n" +
                " %s\n" +
                " %s\n" +
                " %s\n" +
                " %s\n}", fileName, description, url, header, requestBody, httpRequest, assertInfo);
        String APIdefine = String.format("public class %s {  %s }\n", fileName, apiDetail);
        content = content + decription + imports + APIdefine;

        try {
            //写接口类文件
            File file = new File(filePathJAVA);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            CheckUtil.printMsg("[创建类文件]:" + filePathJAVA);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @author: wenmin
     * @date: 2020/12/4 15:59
     * @description: 从swagger解析的请求信息写入excel
     **/
    public static String writeExcelCase(JSONObject obj, String url, String rootpath) {
        String[] titlemap = {"caseNO", "isRun", "description", "header", "requestBody", "httpMethod", "fileName", "url", "assertInfo"};

        String fileName = obj.getString("fileName");
        if (rootpath == null) {
            rootpath = PATH_ROOT() + "/" + obj.getString("tags");
        }
        File file = new File(rootpath + "/casebyexcel/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = rootpath + "/casebyexcel/" + fileName + ".xls";
        JSONObject assertInfo = new JSONObject();
        assertInfo.put("errCode", 0);
        assertInfo.put("msg", "success");
        Object[][] colData = {
                {"1", "Y", "正常调用接口", obj.getString("headers"), obj.getJSONObject("params"), obj.getString("httpMethod"), fileName, url, assertInfo},
        };

        executeWiteExcel(filePath, colData, titlemap);
        return filePath;
    }

    /**
     * @author: wenmin
     * @date: 2020/10/16 16:01
     * @description: 通过URL解析swagger的格式
     **/
    public static JSONObject getSwaggerInfo(String host, String url) {
        CheckUtil.printMsg("获取swagger接口文档");
        if (host == null) {
            host = Constants.TY_OPNE_API;
        }
        if (url.startsWith("http")) {
            String port = url.split(":")[2].split("/")[0];
            host = url.split(port)[0] + port;
            url = url.split(port)[1];
        }
        swaggerUrl = host + "/v2/api-docs";
        JSONObject response = new JSONObject();
        try {
            String docs = doHttpReq("get", swaggerUrl, null, null, null, null, null, null, null);
            JSONObject json = JSONObject.fromObject(docs);
            JSONArray tagsArr = json.getJSONArray("tags");
            JSONObject data = urlInfo(json, url);

            for (int i = 0; i < tagsArr.size(); i++) {
                JSONObject tmp = tagsArr.getJSONObject(i);
                if (tmp.getString("name").equals(data.getString("tags"))) {
                    String description = tmp.getString("description").trim();
                    response.put("tags", description.replace(" ", "").toLowerCase());
                }
            }

            //获取接口的请求方式
            String httpMethod = data.getString("httpMethod");
            response.put("httpMethod", httpMethod);

            //获取请求头
            JSONArray paramsArr = data.getJSONObject("urlInfo").getJSONObject(httpMethod).getJSONArray("parameters");
            JSONObject headers = new JSONObject();
            for (int i = 0; i < paramsArr.size(); i++) {
                JSONObject item = paramsArr.getJSONObject(i);
                if (item.getString("in").equals("header")) {
                    headers.put(item.getString("name"), item.getString("description"));
                }
            }
            if (data.has("consumes")) {
                headers.put("Content-Type", data.getJSONArray("consumes").getString(0));
            }
            response.put("headers", headers);

            //生成接口文件名称
            String[] urlSubs = url.split("/");
            String fileNameStr = null;
            for (int index = 1; index < urlSubs.length; index++) {
                if (null == fileNameStr) {
                    fileNameStr = urlSubs[index];
                } else {
                    char[] str = urlSubs[index].toCharArray();
                    str[0] -= 32;
                    fileNameStr = fileNameStr + String.valueOf(str);
                }
            }
            response.put("fileName", fileNameStr);

            //获取请求体
            if ("POST".equals(httpMethod.toUpperCase())) {
                if (data.has("requestRef")) {
                    response.put("params", definitionsParam(json.getJSONObject("definitions"), data.getString("requestRef")));
                } else {
                    response.put("params", definitionsParam(data.getJSONObject("urlInfo").getJSONObject(httpMethod), null));
                }

            } else if ("GET".equals(httpMethod.toUpperCase())) {
                response.put("params", definitionsParam(data.getJSONObject("urlInfo").getJSONObject(httpMethod), null));

            } else if ("PUT".equals(httpMethod.toUpperCase())) {
            } else {
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Boolean checkDataJson(JSONObject data) {
        try {
            if (null != data) {
//                CheckUtil.printMsg("检查返回的数据是否有效");
                if (!(data instanceof JSONObject)) {
                    CheckUtil.printMsg("swagger return json error.");
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject urlInfo(JSONObject apiDocJson, String url) {
        JSONObject data = new JSONObject();
        String requestRef = "";
        String responsesRef = "";
        try {
            if (checkDataJson(apiDocJson)) {
                JSONObject paths = apiDocJson.getJSONObject("paths");
                if (checkDataJson(paths)) {
                    CheckUtil.printMsg(String.format("获取%s的接口定义", url));
                    JSONObject urlInfo = paths.getJSONObject(url);
                    data.put("urlInfo", urlInfo);
                    if (checkDataJson(urlInfo)) {
                        String httpMethod = paths.getJSONObject(url).keys().next().toString();
                        data.put("httpMethod", httpMethod);
                        JSONObject subInfo = urlInfo.getJSONObject(httpMethod);
                        if (subInfo.has("consumes")) {
                            data.put("consumes", subInfo.getString("consumes"));
                        }
                        data.put("tags", subInfo.getJSONArray("tags").getString(0));
                        JSONArray parameters = urlInfo.getJSONObject(httpMethod).getJSONArray("parameters");
                        for (int i = 0; i < parameters.size(); i++) {
                            if (checkDataJson(parameters.getJSONObject(i))) {
                                if (parameters.getJSONObject(i).getString("in").equals("body")) {
                                    requestRef = parameters.getJSONObject(i).getJSONObject("schema").getString("$ref");
                                    data.put("requestRef", requestRef);
                                }
                            }
                        }
                        if (checkDataJson(urlInfo.getJSONObject(httpMethod).getJSONObject("responses"))) {
                            JSONObject responseOK = urlInfo.getJSONObject(httpMethod).getJSONObject("responses").getJSONObject("200");
                            if (checkDataJson(responseOK)) {
                                responsesRef = responseOK.getJSONObject("schema").getString("$ref");
                                data.put("responsesRef", responsesRef);
                            }
                        }
                    }
                    return data;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject definitionsParam(JSONObject definitions, String ref) {
        JSONObject data = new JSONObject();
        //解析参数体
        try {
            if (ref != null) {
                //常规的post请求
                String param = ref.substring(14);
                JSONObject paramJson = definitions.getJSONObject(param).getJSONObject("properties");
//            System.out.println("[paramJson]: "+paramJson);
                //通过迭代器获得json当中所有的key值
                Iterator keys = paramJson.keys();
                //然后通过循环遍历出的key值
                while (keys.hasNext()) {
                    String key = String.valueOf(keys.next());
                    //通过通过刚刚得到的key值去解析后面的json了
                    //入参类型
                    String type = paramJson.getJSONObject(key).getString("type");
                    if ("string".equals(type)) {
                        data.put(key, key.toUpperCase());

                    } else if ("array".equals(type)) {
                        if (paramJson.getJSONObject(key).getString("items").contains("#/definitions/")) {
                            ref = paramJson.getJSONObject(key).getJSONObject("items").getString("$ref");
                            JSONArray temp = new JSONArray();
                            temp.add(definitionsParam(definitions, ref));
                            data.put(key, temp);
                        } else {
                            data.put(key, "JSONArray");
                        }

                    } else if ("integer".equals(type)) {
                        data.put(key, "0");

                    } else if ("int32".equals(type)) {
                        data.put(key, "0");

                    } else if ("boolean".equals(type)) {
                        data.put(key, false);

                    } else if ("object".equals(type)) {
                        data.put(key, "JSONObject");

                    } else {
                        data.put(key, "Other");

                    }
                }
                return data;
            } else {
                //get请求
                JSONArray paramsArr = definitions.getJSONArray("parameters");
                for (int i = 0; i < paramsArr.size(); i++) {
                    JSONObject item = paramsArr.getJSONObject(i);
                    if (item.getString("in").equals("query")) {
                        data.put(item.getString("name"), item.getString("name").toUpperCase());
                    }
                }
                return data;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
