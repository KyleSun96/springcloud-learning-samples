package com.itheima.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Program: SpringCloud
 * @ClassName: ElasticSearchApp
 * @Description:
 * @Author: KyleSun
 **/
@EnableEurekaClient
@SpringBootApplication
public class ElasticSearchApp {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApp.class, args);
    }

}
