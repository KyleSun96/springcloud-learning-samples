package com.itheima.seller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Program: SpringCloud
 * @ClassName: SellerApp
 * @Description:
 * @Author: KyleSun
 **/

@EnableFeignClients    // 开启Feign的功能
@EnableEurekaClient
@SpringBootApplication
public class SellerApp {

    public static void main(String[] args) {
        SpringApplication.run(SellerApp.class, args);
    }

}
