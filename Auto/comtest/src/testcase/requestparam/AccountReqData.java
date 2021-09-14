package testcase.requestparam;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * FileName: AccountReqData
 * Author:   wanglin
 * Date:     2021/9/13 11:45
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class AccountReqData {

    public JSONObject createaccountdata(String contactsMobile, String licenseType, String name, String cardNo, String contactsEmail, String dingUserId, String licenseNumber,
                                        String loginEmail, String loginMobile, String uniqueId){
        Map<String,Object> map = new HashMap<>();
        if (contactsMobile != null){
            map.put("contactsMobile",contactsMobile);
        }
        if (licenseType != null){
            map.put("licenseType",licenseType);
        }
        if (name != null){
            map.put("name",name);
        }
        if (cardNo != null){
            map.put("cardNo",cardNo);
        }
        if (contactsEmail != null){
            map.put("contactsEmail",contactsEmail);
        }
        if (dingUserId != null){
            map.put("dingUserId",dingUserId);
        }
        if (licenseNumber != null){
            map.put("licenseNumber",licenseNumber);
        }
        if (loginEmail != null){
            map.put("loginEmail",loginEmail);
        }
        if (loginMobile != null){
            map.put("loginMobile",loginMobile);
        }
        if (uniqueId != null){
            map.put("uniqueId",uniqueId);
        }
        return JSONObject.fromObject(map);

    }
}
