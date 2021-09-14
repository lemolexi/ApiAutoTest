package com.course.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.course.model.User;
import io.restassured.response.ValidatableResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

/**
 * FileName: UserManager
 * Author:   wanglin
 * Date:     2021/8/24 17:47
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Log4j2
@RestController
@RequestMapping("v1")
@Api(value = "v1",description = "用户管理系统")
public class UserManager {

    @Autowired
    private SqlSessionTemplate template;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录接口",httpMethod = "POST")
    public Boolean Login(HttpServletResponse response, @RequestBody User user){
        int i = template.selectOne("Login",user);
        System.out.println(i);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        log.info("查询结果为："+i);
        if (i==1){
            log.info("登录信息："+user.getUserName());
            return true;
        }

        return false;
    }

    @RequestMapping(value = "/AddUser",method = RequestMethod.POST)
    @ApiOperation(value = "添加用户接口",httpMethod = "POST")
    public Boolean AddUser(HttpServletRequest request,@RequestBody User user){
        Boolean cookie = verifyCookie(request);
        int result = 0;
        if (cookie != null){
            result = template.insert("AddUser",user);
        }
        if (result > 0){
            log.info("添加用户数量："+result);
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "查询用户信息",httpMethod = "POST")
    public List<User> GetUserInfo(HttpServletRequest request,@RequestBody User user) {
        Boolean cookie = verifyCookie(request);
        if (cookie == true) {
            List<User> users = template.selectList("GetUserInfo", user);
            log.info("获取用户数量：" + users);
            return users;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/UpdateUserInfo",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息",httpMethod = "POST")
    public int UpdateUserInfo(HttpServletRequest request,@RequestBody User user){
        Boolean cookie = verifyCookie(request);
        int i = 0;
        if (cookie == true){
            i = template.selectOne("UpdateUserInfo",user);
        }
        log.info("更新条目数量为："+i);
        return i;
    }

    private Boolean verifyCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            log.info("cookie为空");
            return false;
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")){
                log.info("cookie验证通过");
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/getFileKey",method = RequestMethod.POST)
    @ApiOperation(value = "查询filekey",httpMethod = "POST")
    public JSONArray getFileKey(@RequestBody User user) {
        List getfile = template.selectList("selectTodayFileKey", user);
        String path = "http://172.20.11.151:8035/V1/files/verifyFileKey";
        JSONArray array = new JSONArray(getfile);
        JSONArray array1 = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            log.info("查询数据为:"+array.get(i));
            JSONObject data = new JSONObject();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            headers.put("x-timevale-project-id", "1000000");
            headers.put("x-timevale-signature", "c054b2215990b107b7e0abc7a9b1a3e057ec79f424a7a809e3598e59191e59e0");
            data.put("fileKey", array.get(i));
            ValidatableResponse response = given().
                    headers(headers).
                    and().
                    body(data).
                    when().
                    post(path).
                    then();
//        System.out.println("第"+count+"个接口："+url + "\n");
            String test = response.extract().response().asString();
            array1.add(test);
        }
        return array1;
    }

}
