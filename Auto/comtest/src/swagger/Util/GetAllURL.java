/**
 * FileName: GetAllURL
 * Author:   TODO
 * Date:     2021/5/17 10:55
 * Description: 获取所有接口信息
 * version: IT20211.0
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package swagger.Util;

import net.sf.json.JSONObject;
import org.testng.annotations.Test;
import swagger.Constants;

import java.util.Iterator;

import static swagger.Util.HttpUtil.doHttpReq;

/**
 * @author ningque
 * @date 2021/5/17 10:55
 */
public class GetAllURL {

    @Test
    public void test() {
        String swaggerUrl = Constants.TY_WEB_API + "/v2/api-docs";
        String docs = doHttpReq("get", swaggerUrl, null, null, null, null, null, null, null);
        JSONObject json = JSONObject.fromObject(docs);
        JSONObject jsonPath =json.getJSONObject("paths");
        String urls ="";
        int count =0;
        Iterator iterator = jsonPath.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            urls = urls + key +"\n";
            count++;
        }
        System.out.print(urls);
        System.out.print("总计"+count);
    }

    @Test
    public void testLoginId() {
        String swaggerUrl = Constants.TY_OPNE_API + "/v2/api-docs";
        String docs = doHttpReq("get", swaggerUrl, null, null, null, null, null, null, null);
        JSONObject json = JSONObject.fromObject(docs);
        JSONObject jsonPath =json.getJSONObject("definitions");
        String urls ="";
        int count =0;
        Iterator iterator = jsonPath.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = jsonPath.get(key).toString();
            if (value.contains("loginId")) {
                urls = urls + key + "\n";
                System.out.println(value);
                count++;
            }
        }
        System.out.print(urls);
        System.out.print("总计"+count);
    }
}
