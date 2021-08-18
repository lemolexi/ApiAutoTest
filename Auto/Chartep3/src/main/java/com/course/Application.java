package com.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * FileName: Application
 * Author:   wanglin
 * Date:     2021/8/18 9:24
 * Description: ${DESCRIPTION}
 * version: IT2021${Iteration}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@SpringBootApplication
@ComponentScan("com.course")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
