package com.itheima.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsApp
 * @Description:
 * @Author: KyleSun
 **/
@EnableCircuitBreaker  // 开启Hystrix功能
@EnableEurekaClient
@SpringBootApplication
public class GoodsApp {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApp.class, args);
    }

}
