/**
 * FileName: TestCaseUtil
 * Author:   jcy
 * Date:     2018-8-2 16:05
 * Description: 用例相关
 * version: IT2018
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import testcase.dbcase.DBSelect;
import tgtest.utils.myassert.AssertHelper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:   jcy
 * Date:     2018-8-2 16:05
 * Description: 用例相关
 **/

public class TestCaseUtil {

    /**
     * 功能描述:用例执行描述
     *
     * @param
     * @return
     * @author jcy
     * @date 2018-8-2 16:02
     */

    public static void testCaseMsg(String msg) {
        Reporter.log("[用例描述] " + msg);
        System.out.println("[用例描述] " + msg);
        StackTraceElement[] steArray = Thread.currentThread().getStackTrace();
        if (steArray.length > 2) {
            System.out.println("[用例所在类名] " + steArray[2].getClassName());
            Reporter.log("[用例所在类名] " + steArray[2].getClassName());
        }

    }

    public static void printMsg(String msg) {
        Reporter.log("[提示] " + msg);
        System.out.println("[提示] " + msg);

    }

    public static void assertContains(Object var, Object varx) {
        boolean c;
//        System.out.println(var);
//        System.out.println(varx);
        String var0 = var.toString();
        String var1 = varx.toString();
        if (var0.length() > var1.length()) {
            c = var0.contains(var1);
            assert (c);
        } else {
            if (var0.contains("：")) {
                var0 = var0.substring(var0.indexOf("：")+1,var0.length());
//                System.out.println("var0 = " +var0);
                var1 = var1.substring(var1.indexOf("：")+1,var1.length());
//                System.out.println("var1 = " +var1);
            }
            c = var1.contains(var0);
            assert (c);
        }

    }

    //errcode不同时写多个errcode以逗号（,）分隔
    public static void assertContainsCode(Object var, Object varx) {
        String var0 = var.toString();
        String var1 = varx.toString();
        String[] strArr = var1.split(",");
//        System.out.println("strArr:" + strArr.length);
        if (strArr.length == 1) {
            Assert.assertEquals(var0, var1);
        } else {
            for (int i = 0; i < strArr.length; ++i) {
                if (var0.equals(strArr[i])) {
                    Assert.assertEquals(var0, strArr[i]);
                }
            }
        }

    }

    //msg不同时写多个msg,以（||）分隔
    public static void assertContainsMsg(Object var, Object varx) {
        String var0 = var.toString();
        String var1 = varx.toString();
        String[] strArr = var1.split("||");
//        System.out.println("strArr:" + strArr.length);
        if (strArr.length == 1) {
            Assert.assertEquals(var0, var1);
        } else {
            for (int i = 0; i < strArr.length; ++i) {
                if (var0.equals(strArr[i])) {
                    Assert.assertEquals(var0, strArr[i]);
                }
            }
        }

    }

    public static void assertErrCode(JSONObject json) {
        if (json.isNullObject()) {
            Assert.fail();
        }
        AssertHelper.assertErrCode(json.getString("errCode"), "0");
    }

    /**
     * @param json
     * @param except
     */
    public static void assertResult(JSONObject json, Object[] except) {
        //原有逻辑，校验errCode和msg
        Assert.assertEquals(json.get("errCode"), except[0]);
        if (except[1] != null) {
            TestCaseUtil.assertContains(json.get("msg"), except[1]);
        }
        //校验后续的key和value值
        if (except.length > 2 && except[2] != null) {
            System.out.print(except[2]);
            Map map = getValue(except[2].toString());
            Set keySet = map.keySet();
            Iterator it = keySet.iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                String value = map.get(key).toString();
                //验证data中返回的值中不存在key的值
                if (value.equals("notExist")) {
                    Assert.assertNull(ReadJsonUtil.getJsonValue(json.toString(), key), "返回结果中存在该关键字，" + key + "校验失败");
                }
                //验证data中返回的值中存在key的值
                else if (value.equals("exist")) {
                    TestCaseUtil.assertContains(json.toString(), key);
//                    Assert.assertNotNull(ReadJsonUtil.getJsonValue(json.toString(), key), "返回结果中不存在该关键字" + key + "校验失败");
                }
                //key跟value具体的值进行比对
                else {
                    if (value.startsWith("select")) {
                        String sql = value;
                        if (DBSelect.selectFetchOne(sql) != null) {
                            value = DBSelect.selectFetchOne(sql).toString();
                        }
                    }
                    //先拿key跟value直接比较，结果一致说明不需要取data中的值，不一致key再取data返回的值，兼容直接比较
                    if (key.equals(value)) {
                        Assert.assertEquals(key, value);
                    } else
                        //拿data里的key对应值和value进行比较
                        Assert.assertEquals(ReadJsonUtil.getJsonValue(json.toString(), key).toString(), value);
                }
            }
        }
    }

    public static Map getMap(String[] keys, String[] values) {
        Map<String, Object> map = new HashMap();
        int length1 = keys.length;
        int length2 = values.length;
        Assert.assertEquals(length1, length2, "键值对的数量不匹配");
        for (int i = 0; i < length1; i++) {
            if (keys[i] != null) {
                map.put(keys[i], values[i]);
            }
        }
        return map;
    }

    @Test
    public void test2() {
        //assertContainsCode(-1, "20050523,-1");
        //assertContainsCode(-1,"20050523");
    }

    public static Map<String, Object> getValue(String param) {
        Map<String, Object> map = new HashMap<>();
        String str = "";
        String key = "";
        Object value = "";
        char[] charList = param.toCharArray();
        boolean valueBegin = false;
        for (int i = 0; i < charList.length; i++) {
            char c = charList[i];
            if (c == '{') {
                if (valueBegin == true) {
                    value = getValue(param.substring(i, param.length()));
                    i = param.indexOf('}', i) + 1;
                    map.put(key, value);
                }
            } else if (c == '=') {
                valueBegin = true;
                key = str;
                str = "";
            } else if (c == ',') {
                valueBegin = false;
                value = str;
                str = "";
                map.put(key, value);
            } else if (c == '}') {
                if (str != "") {
                    value = str;
                }
                map.put(key, value);
                return map;
            } else {
                str += c;
            }
        }
        return map;
    }

    /**
     * @author: wenmin
     * @date: 2020/12/15 15:44
     * @description: 校验一层的json的字段是否全部包含。list-代表自定义需要的字段数组
     **/
    public static void assertJSONFields(JSONObject json, String[] list) {
        //排序创建一个固定的数据
        Arrays.sort(list);
        List<String> list2 = new ArrayList<String>();
        //检查出参中印章的对象字段是否齐全
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next().toString().trim();

            if (key.endsWith("Time")) {
                //校验时间格式，符合yyyy-MM-dd hh:MM:ss
                if (json.getString(key) != null && json.getString(key).length() > 18) {
//                    System.out.println("[key]:"+key+json.getString(key));
                    assertTimeReqEx(3, json.getString(key));
                }
            }

            if (key.equals("accountId")) {
                //校验accountId的长度
                if (json.getString(key) != null) {
                    Assert.assertEquals(36, json.getString(key).length());
                }
            }
            list2.add(key);
        }
        //校验是否有数组是否有空值
        List<String> filterList = new ArrayList<String>();
        for (String str : list2) {
            if (str != null) {
                filterList.add(str);
            }
        }
        String[] array = filterList.toArray(new String[filterList.size()]);
        Arrays.sort(array);
        //校验两个数组一模一样
        Assert.assertEquals(list, array);
    }

    /**
     * @author: wenmin
     * @date: 2020/12/16 19:49
     * @description: 校验时间格式
     **/
    public static void assertTimeReqEx(Integer type, String time) {
        //type-1:日期格式；2-时间格式；3-日期 时间格式校验
        String regEx = "";
        switch (type) {
            case 1:
                regEx = "^[1-9]{3}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$";
                break;
            case 2:
                regEx = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$";
                break;
            case 3:
                regEx = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d";
                break;
            default:
                Assert.assertEquals(true, false);
                break;
        }
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(time);
        Assert.assertEquals(mat.matches(), true);
    }


    /**
     * @author: wenmin
     * @date: 2021/1/29 10:24
     * @description: 修改json的key为大写或者小写
     * * @param changeMode 当值为true时，说明是转小写，false为转大写
     **/
    public static JSONObject transferJsonKey(JSONObject jsonObject, boolean transferMode) {
        JSONObject object = new JSONObject();
        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String jsonKey = iterator.next().toString().trim();
            Object valueObject = jsonObject.get(jsonKey);
            if (transferMode) {
                jsonKey = jsonKey.toLowerCase();
            } else {
                jsonKey = jsonKey.toUpperCase();
            }

            if (valueObject.getClass().toString().endsWith("JSONObject")) {
                JSONObject checkObject = JSONObject.fromObject(valueObject);
                // 当值为null时，valueObject还是JSONObject对象，判空是不成立的，要判断是否是nullObject
                if (!checkObject.isNullObject()) {
                    object.accumulate(jsonKey, transferJsonKey((JSONObject) valueObject, transferMode));
                } else {
                    object.accumulate(jsonKey, null);
                }
            }

            if (valueObject.getClass().toString().endsWith("JSONArray")) {
                object.accumulate(jsonKey, transferJsonArray(jsonObject.getJSONArray(jsonKey), transferMode));
            }

            object.accumulate(jsonKey, valueObject);
        }
        return object;
    }

    public static JSONArray transferJsonArray(JSONArray jsonArray, boolean transferMode) {
        JSONArray array = new JSONArray();
        if (null != jsonArray && jsonArray.size() > 0) {
            for (Object object : jsonArray) {
                if (object.getClass().toString().endsWith("JSONObject")) {
                    array.add(transferJsonKey((JSONObject) object, transferMode));
                } else if (object.getClass().toString().endsWith("JSONArray")) {
                    array.add(transferJsonArray((JSONArray) object, transferMode));
                }
            }
        }
        return array;
    }

    @Test
    public void test() {
        JSONObject json = JSONObject.fromObject("{\"STATUS\":1,\"ID\":903,\"ENCRYPTION\":0,\"DOC_MANAGER_ID\":\"$3f02bb57-4aab-435d-9ddb-78822ef6ad58$1280671162\",\"FILEKEY\":\"$3f02bb57-4aab-435d-9ddb-78822ef6ad58$1280671162\"}\n");
        System.out.println(transferJsonKey(json, false));
    }
}
