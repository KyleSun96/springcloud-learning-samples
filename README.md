# springcloud-learning-samples

***

## part01_springcloud_parent

SpringCloud入门案例

  1. 搭建 Provider 和 Consumer 服务
  2. 使用 RestTemplate 完成远程调用
  3. 搭建 Eureka Server 服务
  4. 改造 Provider 和 Consumer 称为 Eureka Client
  5. Consumer 服务 通过从 Eureka Server 中抓取 Provider 地址 完成 远程调用

## part02_springcloud_feign-parent

Feign 声明式服务调用优化SpringCloud中Eureka的远程调用。

## part03_springcloud_hystrix-parent

Hystrix 熔断器入门案例

- Hystix 是 Netflix 开源的一个延迟和容错库，用于隔离访问远程服务、第三方库，防止出现级联失败（雪崩）。
  
## part04_springcloud_gateway-parent

Spring Cloud Gateway 网关入门案例
网关旨在为微服务架构提供一种简单而有效的统一的API路由管理方式。网关也是一个应用，有了网关之后，我们所有的请求都访问网关应用，由网关帮我们去调用其他服务。

- Gateway 与 Eureka 结合，让网关应用能够从注册中心Eureka中，动态的通过应用的名称来获取应用的地址。
  
- 网关的主要功能有两个：

  1. 路由
  2. 过滤

## part05_springcloud_config-parent

- Spring Cloud Config 用在分布式场景下多环境配置文件的管理和维护的分布式配置中心。
- Spring Cloud Bus 消息总线，是用轻量的消息中间件将分布式的节点连接起来，可以用于广播配置文件的更改或者服务的监控管理。其关键思想就是，消息总线可以为微服务做监控，也可以实现应用程序之间相通信。

优点：

  1. 集中管理配置文件
  2. 不同环境不同配置，动态化的配置更新
  3. 配置信息改变时，不需要重启即可更新配置信息到服务——热部署
  4. Bus总线可以让多个客户端同时更新

## part06_ElasticSearch

SpringBoot整合ES，及 ES api的基础学习。

## part07_elasticsearch_demo2

SpringBoot整合ES操作进阶。

1. 使用bulk将数据库中的数据批量导入处理到ES中
2. ElasticSearch中的常用查询
   1. matchAll
   2. termQuery
   3. matchQuery
   4. 模糊查询
      - 通配符查询 wildcard
      - 正则表达式查询
      - 前缀查询  
   5. 范围&排序查询
   6. queryString 多条件查询
   7. 布尔查询
   8. 聚合查询
   9. 高亮查询

## part08_springcloud_practice

SpringCloud整合复习

需求：

   1. 基于 Spring Boot 搭建商家服务，商品服务

   2. 搭建 Eureka Server 注册中心

   3. 用户服务
      1. 根据ID查询商家信息
      2. 添加商品信息（调用商品服务）

   4. 商品服务
      1. 添加商品信息到MySQL：测试数据
      2. 使用 Hystix 对添加商品进行降级处理
