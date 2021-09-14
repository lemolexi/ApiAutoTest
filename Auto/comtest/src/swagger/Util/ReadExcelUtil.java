package swagger.Util;

import cn.hutool.core.util.StrUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.testng.annotations.DataProvider;
import swagger.RequestBo;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 描述EXCEL读取请求参数工具类:
 *
 * @author yuqiang
 * @date 2020年11月6日
 */
public class ReadExcelUtil {
    String EXCEL_NAME = null;

    /**
     * 描述:EXCEL数据集合
     *
     * @author yuqiang
     * @date 2020年11月6日
     */
    @DataProvider(name = "readexcel")
    public Iterator<Object[]> dp(Method method) {
        String name = method.getName();
        String path=method.getDeclaringClass().getName();
        String javapath=path.replace(".","/");
        EXCEL_NAME=new File("src").getAbsolutePath()+"/"+javapath.replace("casebyjava","casebyexcel")+ ".xls";
        System.out.println("[test]"+EXCEL_NAME);
        List<Object> item = new ArrayList<Object>();
        List<Object[]> bos = new ArrayList<Object[]>();

        JSONArray list= ExcelUtil.getXlsExcelData(EXCEL_NAME);

        for (int i=0;i<list.size();i++) {
            JSONObject map=list.getJSONObject(i);
            RequestBo bo = new RequestBo();

            bo.setHttpMethod(map.getString("httpMethod"));
            bo.setFileName(map.getString("fileName"));
            bo.seturl(map.getString("url"));
            bo.setHeader(map.getString("header"));
            bo.setDescription(map.getString("description"));
            bo.setRequestBody(map.getString("requestBody"));
            bo.setAssertInfo(map.getString("assertInfo"));

            item.add(bo);
        }
        for (Object u : item) {
            // 做一个形式转换
            bos.add(new Object[]{u});
        }
        return bos.iterator();
    }

    /**
     * 描述:传参参数替换方法
     *
     * @return JSONObject
     * @author yuqiang
     * @date 2020年11月6日
     */
    public static JSONObject readJson(RequestBo bo, Map<String, String> map) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        String s = p.matcher(bo.getRequestBody()).replaceAll("");
        for (Entry<String, String> vo : map.entrySet()) {
            String k = vo.getKey();
            String v = vo.getValue();
            s = StrUtil.replace(s, k, v);
        }
        JSONObject param = JSONObject.fromObject(s);
        return param;
    }

    public static String getString(String base64) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        String s = p.matcher(base64).replaceAll("");
        return s;

    }

    /**
     * 不传参参数替换
     * 描述:
     *
     * @return JSONObject
     * @author yuqiang
     * @date 2020年11月6日
     */
    public static JSONObject readJson(RequestBo bo) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        String s = p.matcher(bo.getRequestBody()).replaceAll("");
        JSONObject param = JSONObject.fromObject(s);
        return param;
    }


}
