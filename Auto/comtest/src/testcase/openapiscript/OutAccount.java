package testcase.openapiscript;

import common.TestCaseUtil;
import net.sf.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testcase.api.OutAccountIds;
import testcase.requestparam.AccountReqData;
import tgtest.utils.RandomUtil;

/**
 * FileName: OutAccount
 * Author:   wanglin
 * Date:     2021/9/13 11:46
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class OutAccount {

    String contactsMobile = "150" + RandomUtil.NumberString(8);
    String licenseType = "IDCard";
    String name = RandomUtil.ChineseString(5);
    String cardNo = "";
    String contactsEmail = "";
    String dingUserId = "";
    String licenseNumber = RandomUtil.IDString("");
    String loginEmail = "";
    String loginMobile = "";
    String uniqueId = "";
    AccountReqData reqData = new AccountReqData();

    @DataProvider(name = "testData")
    public Object[][] testData(){
        return new Object[][]{
                {"创建外部用户",reqData.createaccountdata(contactsMobile,licenseType,name,cardNo,contactsEmail,dingUserId,licenseNumber,loginEmail,loginMobile,uniqueId),new Object[]{0,"success"}},

        };
    }

    @Test(dataProvider = "testData")
    public void testCase(String msg, JSONObject data,Object[] except){
        TestCaseUtil.testCaseMsg(msg);
        JSONObject json = OutAccountIds.create(data);
        if (json.get("errCode").equals(0)){
            Assert.assertEquals(json.getJSONArray("data").getJSONObject(0).get("msg"),except[1]);
        }
    }
}

