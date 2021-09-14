package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * FileName: GetUserListTest
 * Author:   wanglin
 * Date:     2021/8/23 15:15
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class GetUserListTest {

    @Test(dependsOnGroups = "logintrue",description = "获取用户信息列表测试")
    public void GetUserList() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = sqlSession.selectOne("GetUserList",1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListInfouri);
    }
}
