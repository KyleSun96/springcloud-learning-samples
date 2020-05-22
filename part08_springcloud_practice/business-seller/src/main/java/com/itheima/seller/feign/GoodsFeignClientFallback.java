package com.itheima.seller.feign;

import com.itheima.domain.Goods;
import com.itheima.entity.Result;
import org.springframework.stereotype.Component;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsFeignClientFallback
 * @Description: 调用方服务降级
 * @Author: KyleSun
 **/
@Component
public class GoodsFeignClientFallback implements GoodsFeignClient {
    @Override
    public Result addGoods(Goods good) {
        return new Result(false,"服务调用方降级！");
    }
}
