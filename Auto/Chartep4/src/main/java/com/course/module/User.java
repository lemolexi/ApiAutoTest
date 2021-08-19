package com.course.module;

import lombok.Data;

/**
 * FileName: User
 * Author:   wanglin
 * Date:     2021/8/19 10:37
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class User {
    private int id;
    private String name;
    private int age;
    private String sex;
}
