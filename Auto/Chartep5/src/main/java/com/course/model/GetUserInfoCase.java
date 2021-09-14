package com.course.model;

import lombok.Data;

/**
 * FileName: GetUserInfoCase
 * Author:   wanglin
 * Date:     2021/8/20 11:27
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class GetUserInfoCase {

    private int id;
    private String userId;
    private String expected;

}
