package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoCase;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * FileName: UpdateUserInfoTest
 * Author:   wanglin
 * Date:     2021/8/23 15:16
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class UpdateUserInfoTest {


    @Test(dependsOnGroups = "logintrue",description = "更新用户信息测试")
    public void UpdateUserInfo() throws IOException {

        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = sqlSession.selectOne("UpdateUserInfo",1);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfouri);
    }

    @Test(dependsOnGroups = "logintrue",description = "删除用户信息")
    public void DeleUserInfo() throws IOException {

        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = sqlSession.selectOne("UpdateUserInfo",2);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfouri);
    }


}
