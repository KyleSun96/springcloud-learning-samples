package com.itheima.goods.controller;

import com.itheima.domain.Goods;
import com.itheima.entity.Result;
import com.itheima.goods.service.GoodsService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsController
 * @Description:
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    /**
     * @description: //TODO 新增一个商品信息，在service层发消息
     * @param: [good]
     * @return: com.itheima.entity.Result
     */
    @PostMapping("/add")
    @HystrixCommand(fallbackMethod = "addGoods_fallback")
    public Result addGoods(@RequestBody Goods good) {

        // 模拟超时异常
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // 模拟提供端异常
        if (good.getPrice() < 100) {
            throw new RuntimeException("价格不符，无法添加！");
        }

        goodsService.addGoods(good);
        return new Result(true, "添加商品信息成功！");
    }


    /**
     * 定义降级方法：
     * 1. 方法的返回值需要和原方法一样
     * 2. 方法的参数需要和原方法一样
     * 3. 就是只有方法名不同
     */
    public Result addGoods_fallback(@RequestBody Goods good) {
        return new Result(false, "服务提供方降级！");
    }

}
