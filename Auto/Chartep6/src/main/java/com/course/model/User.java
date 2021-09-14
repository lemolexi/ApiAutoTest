package com.course.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * FileName: User
 * Author:   wanglin
 * Date:     2021/8/24 17:44
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class User {

    private int id;
    private String userName;
    private String passWd;
    private String age;
    private String sex;
    private String permission;
    private String isDelete;
    private String startDate;
    private String endDate;
    private JSONArray getFikey;
}
