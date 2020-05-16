package com.itheima.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Program: SpringCloud
 * @ClassName: ProviderApp
 * @Description:
 * @Author: KyleSun
 **/
@SpringBootApplication
@EnableEurekaClient
public class ProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class, args);
    }
}
