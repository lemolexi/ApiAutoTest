package com.course.controller;

import com.course.module.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FileName: StartApi
 * Author:   wanglin
 * Date:     2021/8/19 10:37
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@Log4j2
@RestController
@Api(value = "v1",description = "第一个版本的接口demo")
@RequestMapping("v1")
public class StartApi {

    @Autowired
    private SqlSessionTemplate template;

    /**
     * 增加用户信息接口
     * @param user
     * @return
     */
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ApiOperation(value = "新增用户",httpMethod = "POST")
    public int addUser(@RequestBody User user){
        return template.insert("addUser",user);
    }

    /**
     * 根据id删除用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/deleUser",method = RequestMethod.POST)
    @ApiOperation(value = "根据id删除用户信息",httpMethod = "POST")
    public int deleUser(@RequestBody User user){
        return template.delete("delUser", user);
    }

    /**
     * 根据id更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/updataUser",method = RequestMethod.POST)
    @ApiOperation(value = "根据id更新用户信息",httpMethod = "POST")
    public int updataUser(@RequestBody User user){
        return template.update("updataUser", user);
    }

    /**
     * 根据姓名查询年龄
     * @param name
     * @return
     */
    @RequestMapping(value = "/getUsername",method = RequestMethod.POST)
    @ApiOperation(value = "根据name查询用户年龄",httpMethod = "POST")
    public int getUser(@RequestParam String name){
        return template.selectOne("getUser",name);
    }

    @RequestMapping(value = "/get/name/User/sex",method = RequestMethod.POST)
    @ApiOperation(value = "根据姓名查询性别",httpMethod = "POST")
    public String getUsersex(@RequestBody User user){
        return template.selectOne("getUsersex",user);
    }

}

