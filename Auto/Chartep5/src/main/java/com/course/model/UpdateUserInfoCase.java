package com.course.model;

import lombok.Data;

/**
 * FileName: UpdateUserInfoCase
 * Author:   wanglin
 * Date:     2021/8/20 11:07
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class UpdateUserInfoCase {

    private int id;
    private String userId;
    private String userName;
    private String age;
    private String sex;
    private String permission;
    private String isDelete;
    private String expected;
    
}
