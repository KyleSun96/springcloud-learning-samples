package com.itheima.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Program: SpringCloud
 * @ClassName: EurekaApp
 * @Description: 注册中心启动类
 * @Author: KyleSun
 **/
@SpringBootApplication
@EnableEurekaServer     // 启用EurekaServer
public class EurekaApp {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp.class, args);
    }
}
