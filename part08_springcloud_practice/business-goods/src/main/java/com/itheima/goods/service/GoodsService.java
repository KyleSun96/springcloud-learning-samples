package com.itheima.goods.service;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.Goods;
import com.itheima.goods.config.RabbitMQConfig;
import com.itheima.goods.mapper.GoodsMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsService
 * @Description:
 * @Author: KyleSun
 **/
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void addGoods(Goods good) {

        // 将goods信息存入mysql
        goodsMapper.addGoods(good);

        // 准备信息内容
        /*
         当spec为map类型时，需要通过specStr转换

          String specStr = goods.getSpecStr();
          Map spec = JSON.parseObject(specStr,Map.class);
          goods.setSpec(spec);

          String data = JSON.toJSONString(goods);
          System.out.println(data);
         */
        String data = JSON.toJSONString(good);
        System.out.println(data);

        // 发送信息
        rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_TOPIC_EXCHANGE, "goods.add", data);
    }
}
