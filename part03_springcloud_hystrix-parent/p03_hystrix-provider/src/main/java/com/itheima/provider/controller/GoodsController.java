package com.itheima.provider.controller;

import com.itheima.provider.domain.Goods;
import com.itheima.provider.service.GoodsService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description: //TODO 服务提供方提供降级方法
 * @ClassName: GoodsController
 * @Author: KyleSun
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Value("${server.port}")
    private int port;

    /*
        会出现降级的情况
            1. 出现异常
            2. 服务调用超时，默认1秒
     */
    @GetMapping("/findOne/{id}")
    @HystrixCommand(
            fallbackMethod = "findOne_fallback",  // 指定降级方法
            commandProperties = {                 // 设置Hystrix的超时时间，默认1s
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            })
    public Goods findOne(@PathVariable("id") int id) {

        // 如果id == 1 ，则出现异常，id != 1 则正常访问
        if (id == 1) {
             int i = 1 / 0;
        }

        // 2. 超时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Goods goods = goodsService.findOne(id);
        goods.setTitle(goods.getTitle() + ":" + port);//将端口号，设置到了 商品标题上
        return goods;
    }


    /**
     * @description: //TODO 定义降级方法
     * @param: [id]
     * @return: com.itheima.provider.domain.Goods
     * <p>
     * 1. 方法的返回值需要和原方法一样
     * 2. 方法的参数需要和原方法一样
     * 3. 就是只有方法名不同
     */
    public Goods findOne_fallback(int id) {
        Goods goods = new Goods();
        goods.setTitle("服务提供方降级了。。。");
        return goods;
    }
}
