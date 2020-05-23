package com.itheima.elasticsearch.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Program: SpringCloud
 * @ClassName: RabbitMQConfig
 * @Description:
 * @Author: KyleSun
 **/
@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String GOODS_TOPIC_EXCHANGE = "goods_topic_exchange";
    // 队列名称
    public static final String GOODS_QUEUE = "goods_queue";

    //声明交换机
    @Bean("goodsExchange")
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(GOODS_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean("goodsQueue")
    public Queue itemQueue() {
        return QueueBuilder.durable(GOODS_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding itemQueueExchange(@Qualifier("goodsQueue") Queue queue,
                                     @Qualifier("goodsExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("goods.#").noargs();
    }
}
