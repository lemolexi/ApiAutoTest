package swagger.Util;
/**
 * FileName: CheckUtil
 * Author:   wenmin
 * Date:     2020/12/14 14:20
 * Description: ${description}
 * version: IT2020${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


import net.sf.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import swagger.RequestBo;

import java.util.Iterator;

/**
 * @author: wenmin
 * Date:     2020/12/14 14:20
 * @Description: 断言校验方法
 */
public class CheckUtil {
    /**
     * @author: wenmin
     * @date: 2020/12/13 18:08
     * @description: 获取excel中的断言对象和请求的出参中的值对比
     **/
    public static void assertBo(RequestBo bo, JSONObject result) {
        //入参：excel里面的断言信息，请求的出参
        JSONObject paramJson = JSONObject.fromObject(bo.getAssertInfo());
        //通过迭代器获得json当中所有的key值
        Iterator keys = paramJson.keys();
        //然后通过循环遍历出的key值
        while (keys.hasNext()) {
            String key = String.valueOf(keys.next());
            String vale = paramJson.getString(key);
            Iterator keys2 = result.keys();
            while (keys2.hasNext()) {
                String key2 = String.valueOf(keys2.next());
                if (key.equals(key2)) {
                    Assert.assertEquals(vale, result.getString(key2));
                    break;
                }
            }
        }
    }

    /**
     * @author: wenmin
     * @date: 2020/12/14 14:22
     * @description: 用例描述打印输出
     **/
    public static void testCaseMsg(String msg) {
        Reporter.log("[用例描述] " + msg);
        System.out.println("[用例描述] " + msg);
        StackTraceElement[] steArray = Thread.currentThread().getStackTrace();
        if (steArray.length > 2) {
            System.out.println("\n[用例所在类名] " + steArray[2].getClassName());
            Reporter.log("\n[用例所在类名] " + steArray[2].getClassName());
        }

    }

    /**
     * @author: wenmin
     * @date: 2020/12/14 14:22
     * @description: 提示信息打印输出
     **/
    public static void printMsg(String msg) {
        Reporter.log("\n[提示] " + msg);
        System.out.println("\n[提示] " + msg);
    }
}
