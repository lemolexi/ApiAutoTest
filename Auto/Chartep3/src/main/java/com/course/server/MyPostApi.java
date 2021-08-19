package com.course.server;

import com.course.bean.User;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FileName: MyPostApi
 * Author:   wanglin
 * Date:     2021/8/18 17:46
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/v2")
@Api(value = "/",description = "demo接口")
public class MyPostApi {

    private static Cookie cookie;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录接口",httpMethod = "POST")
    public String loginpage(HttpServletResponse response,
                                 @RequestParam(value = "username",required = true) String username,
                                 @RequestParam(value= "password",required = true) String password){
        if (username.equals("wanglin") && password.equals("123456")){
            cookie = new Cookie("login","true");
            response.addCookie(cookie);
            return "登录成功";
        }else
            return "用户名密码错误";
    }

    @RequestMapping(value = "getwithname",method = RequestMethod.POST)
    @ApiOperation(value = "登录后才能查询信息",httpMethod = "POST")
    public String getwithname(HttpServletRequest request, @RequestBody User user){

        User user1 = new User();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if (user.getUsername().equals("wanglin")
                    && user.getPassword().equals("123456")
                    && cookie.getName().equals("login")
                    && cookie.getValue().equals("true")){
                user1.setName("王林");
                user1.setAge("20");
                user1.setUsername("wanglin");
                user1.setPassword("123456");
                return user1.toString();
            }
        }
        return "参数不合法";

    }
}
