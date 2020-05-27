package com.itheima.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Program: SpringCloud
 * @ClassName: ConfigServerApp
 * @Description:
 * @Author: KyleSun
 **/
@EnableEurekaClient
@EnableConfigServer // 启用config server功能
@SpringBootApplication
public class ConfigServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class,args);
    }

}

