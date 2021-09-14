package com.course.controller;

import com.alibaba.fastjson.JSONObject;
import com.course.module.User;
import io.restassured.response.ValidatableResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String hello(@RequestParam(value = "fileKey", defaultValue = "test") String fileKey) throws IOException {
//        HttpPost post = new HttpPost("http://172.20.11.151:8035/V1/files/verifyFileKey");
//        HttpClient client = new DefaultHttpClient();
//        JSONObject data =new JSONObject();
//        data.put("fileKey",fileKey);
//        post.setHeader("Content-Type", "application/json");
//        post.setHeader("x-timevale-project-id", "1000000");
//        post.setHeader("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
//        String path = "http://172.20.11.151:8035/V1/files/verifyFileKey";
//        StringEntity entity = new StringEntity(data.toString(),"utf-8");
//        post.setEntity(entity);
//        HttpResponse response = client.execute(post);
//        String result = EntityUtils.toString(response.getEntity(),"utf-8");
//        System.out.println(result);
//        return result;
        String path = "http://172.20.11.151:8035/V1/files/verifyFileKey";
        JSONObject data =new JSONObject();
        Map<String, String> headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/json");
        headers.put("x-timevale-project-id", "1000000");
        headers.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
        data.put("fileKey",fileKey);
        ValidatableResponse response=given().
                headers(headers).
                and().
                body(data).
                when().
                post(path).
                then();
//        System.out.println("第"+count+"个接口："+url + "\n");
        String test = response.extract().response().asString();
        return test;
    }

}

