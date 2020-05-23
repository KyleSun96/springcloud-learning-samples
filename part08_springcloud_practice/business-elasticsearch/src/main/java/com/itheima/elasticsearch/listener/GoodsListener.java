package com.itheima.elasticsearch.listener;

import com.itheima.elasticsearch.config.ElasticSearchConfig;
import com.itheima.elasticsearch.config.RabbitMQConfig;
import com.itheima.elasticsearch.service.ESService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Program: SpringCloud
 * @ClassName: GoodsListener
 * @Description:
 * @Author: KyleSun
 **/
@Component
public class GoodsListener {

    @Autowired
    private ESService esService;

    /**
     * @description: //TODO 监听指定队列的消息，收到消息，并且添加文档
     * @param: [jsonGoods]
     * @return: void
     */
    @RabbitListener(queues = RabbitMQConfig.GOODS_QUEUE)
    public void goodsListener(String jsonGoods) {
        System.out.println("接收到的消息为：" + jsonGoods);

        // 收到消息，并且添加文档到 ES
        try {
            esService.addGoods(jsonGoods);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
