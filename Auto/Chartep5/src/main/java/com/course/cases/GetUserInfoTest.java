package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * FileName: GetUserInfoTest
 * Author:   wanglin
 * Date:     2021/8/23 15:15
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class GetUserInfoTest {

    @Test(dependsOnGroups = "logintrue",description = "查询用户信息测试")
    public void GetUserInfo() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = sqlSession.selectOne("GetUserInfo",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfouri);
    }
}
