package common;

import org.json.JSONObject;

/**
 * FileName: reqparamter
 * Author:   wanglin
 * Date:     2021/8/17 17:33
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class reqparamter {

    public static JSONObject logindata(String user, String pwsd){
        JSONObject data = new JSONObject();
        data.put("user",user);
        data.put("pwsd",pwsd);
        return data;
    }
}
