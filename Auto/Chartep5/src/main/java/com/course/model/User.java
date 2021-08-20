package com.course.model;

import lombok.Data;

/**
 * FileName: User
 * Author:   wanglin
 * Date:     2021/8/20 10:13
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class User {

    private String id;
    private String userName;
    private String passWd;
    private String sex;
    private String age;
    private String permission;
    private String isDelete;

    @Override
    public String toString(){
        return (
                "{id:" + id + "," +
                "userName:" + userName + "," +
                "passWd:" + passWd + "," +
                "sex:" + sex + "," +
                "age:" + age + "," +
                "permission:" + permission + "," +
                "isDelete:" + isDelete + "}"
        );
    }
}
