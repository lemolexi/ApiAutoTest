package testcase.api;

import constants.URL_OPEN_API;
import net.sf.json.JSONObject;

import common.PostUtil;
import constants.URL_OPEN_API;
/**
 * FileName: OutAccountIds
 * Author:   wanglin
 * Date:     2021/9/13 11:28
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class OutAccountIds {

    public static JSONObject create(JSONObject jsonData) {
        return PostUtil.request(jsonData, URL_OPEN_API.OUTERACCOUNTS_CREATE);
    }
}
