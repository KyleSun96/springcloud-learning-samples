package com.itheima.consumer.controller;

import com.itheima.consumer.domain.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Program: SpringCloud
 * @ClassName: OrderController
 * @Description: 模拟订单服务的调用方
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/findGoodsById/{id}")
    public Goods findGoodsById(@PathVariable("id") int id) {
        // 远程调用goods服务中的findOne接口

        // 可能有集群，因此返回的是list集合
        List<ServiceInstance> instances = discoveryClient.getInstances("eureka-provider");

        if (instances == null || instances.size() == 0) {
            return null;
        }

        ServiceInstance instance = instances.get(0);
        String host = instance.getHost();
        int port = instance.getPort();

        // String url = "http://localhost:8000/goods/findOne/" + id;
        String url = "http://" + host + ":" + port + "/goods/findOne/" + id;
        return restTemplate.getForObject(url, Goods.class);
    }


    /**
     * @description: //TODO 使用 Ribbon 简化 restTemplate 的调用过程
     * 1. 在声明restTemplate的bean时候，添加一个注解：  @LoadBalanced
     * 2. 在restTemplate发起请求时，需要定义URL时，
     * host:post处替换为服务提供方的 应用名称： spring.application.name
     * @param: [id]
     * @return: com.itheima.consumer.domain.Goods
     */
    @GetMapping("/findGoodsById2/{id}")
    public Goods findGoodsById2(@PathVariable("id") int id) {

        String url = "http://eureka-provider/goods/findOne/" + id;
        return restTemplate.getForObject(url, Goods.class);
    }
}
