package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.AddUserCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * FileName: AddUserTest
 * Author:   wanglin
 * Date:     2021/8/23 15:15
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class AddUserTest {

    @Test(dependsOnGroups = "logintrue",description = "添加用户测试用例")
    public void addUserInfo() throws IOException, InterruptedException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        AddUserCase addUserCase = sqlSession.selectOne("AddUserInfo",1);
        sqlSession.close();
        System.out.println(addUserCase.toString());
        System.out.println(TestConfig.addUseruri);

        //获取请求结果
        String result = getResult(addUserCase);
        SqlSession sqlSession1 = DatabaseUtil.getSqlSession();
        User user = sqlSession1.selectOne("addUser",addUserCase);
        System.out.println(user.toString());
        //验证结果
        Assert.assertEquals(addUserCase.getExpected(),result);
    }

    private String getResult(AddUserCase addUserCase) throws IOException {

        HttpPost post = new HttpPost(TestConfig.addUseruri);
        JSONObject param = new JSONObject();
        param.put("userName",addUserCase.getUserName());
        param.put("passWd",addUserCase.getPassWd());
        param.put("sex",addUserCase.getSex());
        param.put("age",addUserCase.getAge());
        param.put("permission",addUserCase.getPermission());
        param.put("isDelete",addUserCase.getIsDelete());
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        post.setHeader("Content-Type","application/json");
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        String result;
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }
}
