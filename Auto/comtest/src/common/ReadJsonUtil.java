package common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
* 描述:json文件读取工具类
* @author yuqiang
* @date 2020年11月4日
*
 */

public class ReadJsonUtil {
	
	/**
	 * 
	* 描述:无参数读取
	* @author yuqiang
	* @date 2020年11月4日
	*
	 */
	public static JSONObject readJson(String filePath) {
		
        String s = ReadJsonUtil.readJsonFile(filePath);
        JSONObject obj = JSON.parseObject(s);
        return obj;
    }
	
	/**
	 * 
	* 描述:有参数读取
	* @author yuqiang
	* @date 2020年11月4日
	*
	 */
	
	public static JSONObject readJson(String filePath , Map<String,String> map) {
			
        String s = ReadJsonUtil.readJsonFile(filePath);
        
        for(Entry<String, String> vo : map.entrySet()){
        	  String k = vo.getKey();
        	  String v = vo.getValue();
        	  s = StrUtil.replace(s,k,v);
        	  }	      
        JSONObject obj = JSON.parseObject(s);
        
        return obj;
		
	}

	/*@Test
	public void testi () { 		
		String s = "src\\documents\\jsonfile\\checknull.json";	
		readJson(s);
		
	    }*/

	
	/**
	 * 
	* 描述:读取文件内容
	* @author yuqiang
	* @date 2020年11月4日
	*
	 */
	 public static String readJsonFile(String fileName) {
	        String jsonStr = "";
	        try {
	            File jsonFile = new File(fileName);
	            FileReader fileReader = new FileReader(jsonFile);

	            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
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

	/**
	 *  json字符串 转对象
	 * @param jsonString
	 * @param <T>
	 * @return
	 */
	public static <T> T toObject(String jsonString , Class<T> clz){
		if (StringUtils.isNotEmpty(jsonString)) {
			return JSON.parseObject(jsonString , clz);
		}
		return null;
	}

	/**
	 *  jsonString 转list
	 * @param jsonString
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> toList(String jsonString , Class<T> clz){
		if (StringUtils.isNotEmpty(jsonString)) {
			return JSON.parseArray(jsonString, clz);
		}
		return Collections.emptyList();
	}

	/**
	 *  对象转json字符串
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object){
		if (object != null) {
			return JSON.toJSONString(object);
		}
		return null;
	}

	/**
	 *  根据json 中的可以 获取对应value，只会获取第一个被匹配到的值
	 * @param json json 字符串
	 * @param key
	 * @return 返回结果类型可能是 JSONObject、String、JSONArray
	 */
	public static Object getJsonValue(String json , String key){
		if (StringUtils.isNotEmpty(json) && StringUtils.isNotEmpty(key)) {
			Object object = JSON.parse(json);
			if (object != null) {
				if (object instanceof JSONObject) {
					return getJsonValue((JSONObject) object, key);
				}else if (object instanceof JSONArray){
					return getJsonValue((JSONArray) object, key);
				}
			}
			return object;
		}
		throw new IllegalArgumentException("param json or key cannot be empty.");
	}


	/**
	 *  根据json 中的可以 获取对应value，只会获取第一个被匹配到的值
	 * @param jsonObject
	 * @param key
	 * @return 返回结果类型可能是 JSONObject、String、JSONArray
	 */
	public static Object getJsonValue(JSONObject jsonObject , String key) {
		if (jsonObject != null && StringUtils.isNotEmpty(key)) {
			for (Entry<String, Object> entry : jsonObject.entrySet()) {
				String entryKey = entry.getKey();
				Object value = entry.getValue();
				if (key.equals(entryKey)) {
					return value;
				} else {
					if (value != null) {
						String s = value.toString();
						if (StringUtils.isNotEmpty(s)) {
							Object jsonValue = getJsonValue(value, key);
							if (jsonValue != null) {
								return jsonValue;
							}
						}
					}
				}
			}
			return null;
		}
		throw new IllegalArgumentException("param jsonObject or key cannot by empty.");
	}

	public static Object getJsonValue(Object object , String key){
		if (object != null && StringUtils.isNotEmpty(key)) {
			if (object instanceof JSONObject) {
				return getJsonValue((JSONObject) object, key);
			}else if (object instanceof JSONArray){
				return getJsonValue((JSONArray) object, key);
			}
		}
		return null;
	}

	public static Object getJsonValue(JSONArray jsonArray , String key) {
		if (jsonArray != null && StringUtils.isNotEmpty(key)) {
			for (Object next : jsonArray) {
				if (next != null) {
					return getJsonValue(next, key);
				}
			}
		}
		return null;
	}
}

