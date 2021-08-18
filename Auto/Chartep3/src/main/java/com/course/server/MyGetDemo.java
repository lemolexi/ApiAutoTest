package com.course.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * FileName: MyGetDemo
 * Author:   wanglin
 * Date:     2021/8/18 9:29
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Api(value = "/",description = "全部的get方法")
public class MyGetDemo {

    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    @ApiOperation(value = "get接口获取cookie",httpMethod = "GET")
    public String getCookies(HttpServletResponse response){
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        return "恭喜你获取cookie成功";
    }

    /*
    * 带着cookie进行请求
    *
    * */
    @RequestMapping(value = "/getwithcookie",method = RequestMethod.GET)
    @ApiOperation(value = "带着cookie请求接口",httpMethod = "GET")
    public String getWithCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            return "你必须携带cookie进行请求";
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")){
                return "恭喜你访问成功";
            }else {
                return "你的cookie信息错误";
            }
        }
        return "你必须携带cookie进行请求";
    }

    /**
     * 带参数的get请求
     * 请求方法：key=value&key=value
     */

    @RequestMapping(value = "/getwithparameter",method = RequestMethod.GET)
    @ApiOperation(value = "携带参数进行请求",httpMethod = "GET")
    public Map<String,Integer> getList(@RequestParam Integer start,@RequestParam Integer end){

        Map<String, Integer> mylist = new HashMap<>();
        mylist.put("上衣",200);
        mylist.put("裤子",300);
        mylist.put("短裤",320);
        mylist.put("绿帽子",10000);
        return mylist;

    }

    /**
     *
     * url:ip:port/uri/{parameter}
     */
    @RequestMapping(value = "/getv2/{start}/{end}",method = RequestMethod.GET)
    @ApiOperation(value = "带参数的get请求",httpMethod = "GET")
    public Map<String,Integer> getlist02(@PathVariable Integer start, @PathVariable Integer end){

        Map<String, Integer> mylist = new HashMap<>();
        mylist.put("王林",2000);
        mylist.put("宁缺",3000);
        mylist.put("云歌",3300);
        mylist.put("琥珀",4000);
        return mylist;

    }

}
