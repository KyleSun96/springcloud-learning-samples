package com.itheima.consumer.feign;

import com.itheima.consumer.domain.Goods;
import org.springframework.stereotype.Component;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsFeignClientFallback
 * @Description: Feign服务消费方的降级处理类
 * @Author: KyleSun
 * <p>
 * 1. 定义类 实现 Feign 客户端接口
 * 2. 使用 @Component 注解将该类的Bean加入SpringIOC容器
 **/
@Component
public class GoodsFeignClientFallback implements GoodsFeignClient {
    @Override
    public Goods findGoodsById(int id) {
        Goods goods = new Goods();
        goods.setTitle("服务消费方降级了。。。");
        return goods;
    }
}
