package swagger.Util;


import cn.hutool.core.util.StrUtil;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 描述:json文件读取工具类
 *
 * @author yuqiang
 * @date 2020年11月4日
 */

public class ReadJsonUtil {

    /**
     * 描述:无参数读取
     *
     * @author yuqiang
     * @date 2020年11月4日
     */
    public static JSONObject readJson(String filePath) {

        String s = ReadJsonUtil.readJsonFile(filePath);
        JSONObject obj = JSONObject.fromObject(s);
        return obj;
    }

    /**
     * 描述:有参数读取
     *
     * @author yuqiang
     * @date 2020年11月4日
     */

    public static JSONObject readJson(String filePath, Map<String, String> map) {

        String s = ReadJsonUtil.readJsonFile(filePath);

        for (Entry<String, String> vo : map.entrySet()) {
            String k = vo.getKey();
            String v = vo.getValue();
            s = StrUtil.replace(s, k, v);
        }
        JSONObject obj = JSONObject.fromObject(s);

        return obj;

    }

    /**
     * 描述:读取文件内容
     *
     * @author yuqiang
     * @date 2020年11月4日
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;

            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
