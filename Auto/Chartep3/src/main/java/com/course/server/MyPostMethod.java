package com.course.server;

import com.course.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FileName: MyPostMethod
 * Author:   wanglin
 * Date:     2021/8/18 10:48
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/v1")
@Api(value = "/",description = "我的全部post请求接口")
public class MyPostMethod {

    private static Cookie cookie;

    /**
     * 模拟用户登录接口
     * @param response
     * @param userName
     * @param passWord
     * @return
     */

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录接口，返回cookie",httpMethod = "POST")
    public String login(HttpServletResponse response,
                        @RequestParam(value = "username",required = true) String userName,
                        @RequestParam(value = "password",required = true) String passWord){
        if (userName.equals("wanglin") && passWord.equals("123456")){
            cookie = new Cookie("login","true");
            response.addCookie(cookie);
            return "恭喜你登录成功";
        }else {
            return "用户名或密码错误";
        }
    }

    /**
     * 携带cookie的指定用户列表信息
     * @param request
     * @param user
     * @returen
     */
    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    @ApiOperation(value = "获取用户列表",httpMethod = "POST")
    public String getUser(HttpServletRequest request,
                        @RequestBody User user){

        User user1 = new User();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("login") && cookie.getValue().equals("true") && user.getUsername().equals("wanglin") && user.getPassword().equals("123456")){
                user1.setName("wanglin");
                user1.setAge("20");
                return user1.toString();
            }
        }
        return "参数不合法";
    }
}
