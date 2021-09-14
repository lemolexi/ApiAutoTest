package com.course.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ResourceBundle;

/**
 * FileName: DatabaseUtil
 * Author:   wanglin
 * Date:     2021/8/23 16:07
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DatabaseUtil {

    public static SqlSession getSqlSession() throws IOException {
        //获取配置资源文件
        Reader reader = Resources.getResourceAsReader("databaseConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
        //执行sql语句
        SqlSession sqlSession = factory.openSession();
        return sqlSession;
    }
}
