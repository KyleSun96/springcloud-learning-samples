package com.itheima.seller.feign;

import com.itheima.domain.Goods;
import com.itheima.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Program: SpringCloud
 * @InterfaceName: GoodsFeign
 * @Description:
 * @Author: KyleSun
 **/
@FeignClient(value = "business-goods", fallback = GoodsFeignClientFallback.class)
public interface GoodsFeignClient {

    @PostMapping("/goods/add")
    public Result addGoods(@RequestBody Goods good);

}
