package com.course.module;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "用户id",example = "1")
    private int id;

    @ApiModelProperty(value = "用户姓名",example = "王八蛋")
    private String name;

    @ApiModelProperty(value = "用户年龄",example = "18")
    private int age;

    @ApiModelProperty(value = "用户性别",example = "不男不女")
    private String sex;
}
