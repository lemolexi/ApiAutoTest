package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginCase;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * FileName: LoginTest
 * Author:   wanglin
 * Date:     2021/8/23 15:15
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class LoginTest {

    @BeforeTest(groups = "logintrue",description = "测试准备工作，获取httpclient对象")
    public void BeforeTest(){
        TestConfig.loginuri = ConfigFile.geturl(InterfaceName.LOGIN);
        TestConfig.addUseruri = ConfigFile.geturl(InterfaceName.ADDUSERINFO);
        TestConfig.getUserListInfouri = ConfigFile.geturl(InterfaceName.GETUSERLISTINFO);
        TestConfig.updateUserInfouri = ConfigFile.geturl(InterfaceName.UPDATEUSERINFO);
        TestConfig.getUserInfouri = ConfigFile.geturl(InterfaceName.GETUSERINFO);

        TestConfig.defaultHttpClient = new DefaultHttpClient();
    }

    @Test(groups = "logintrue",description = "登录成功测试用例")
    public void logintrue() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("LoginCase",1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginuri);

        //请求结果
        String result = getResult(loginCase);
        System.out.println(result);
        //验证结果
        Assert.assertEquals(loginCase.getExpected(),result);

    }

    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.loginuri);
        JSONObject param = new JSONObject();
        param.put("userName",loginCase.getUserName());
        param.put("passWd",loginCase.getPassWd());
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        post.setHeader("Content-Type","application/json");
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        String result;
        result = EntityUtils.toString(response.getEntity());
        TestConfig.cookieStore = TestConfig.defaultHttpClient.getCookieStore();
        return result;
    }


    @Test(groups = "logintrue",description = "登录失败测试用例")
    public void loginfalse() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        LoginCase loginCase = sqlSession.selectOne("LoginCase",2);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginuri);

        //请求结果
        String result = getResult(loginCase);
        System.out.println(result);
        //验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
    }
}
