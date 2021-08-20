package com.course.model;

import lombok.Data;

/**
 * FileName: AddUserCase
 * Author:   wanglin
 * Date:     2021/8/20 11:23
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class AddUserCase {

    private String id;
    private String userName;
    private String passWd;
    private String sex;
    private String age;
    private String permission;
    private String isDelete;
    private String expected;
}
