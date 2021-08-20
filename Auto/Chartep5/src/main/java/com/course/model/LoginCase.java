package com.course.model;

import lombok.Data;

/**
 * FileName: LoginCase
 * Author:   wanglin
 * Date:     2021/8/20 10:33
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class LoginCase {

    private String id;
    private String userName;
    private String passWd;
    private String expected;

}
