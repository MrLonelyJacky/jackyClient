基于nacos为注册中心 通信协议是tcp/ip的rpc框架
基础架构：Springboot+Nacos+Netty
框架设计图：
![Image text](https://github.com/MrLonelyJacky/jackyClient/blob/main/images/%E8%AE%BE%E8%AE%A1%E5%9B%BE.png)
使用方法：在服务启动类上加上@EnableRpcClient注解 配置文件中配置好端口
![Image text](https://github.com/MrLonelyJacky/jackyClient/blob/main/images/nacos%E6%B3%A8%E5%86%8C.png)
![Image text](https://github.com/MrLonelyJacky/jackyClient/blob/main/images/%E8%B0%83%E7%94%A8%E6%9C%8D%E5%8A%A11.png)
![Image text](https://github.com/MrLonelyJacky/jackyClient/blob/main/images/%E8%B0%83%E7%94%A8%E6%9C%8D%E5%8A%A12.png)

原理：@EnableRpcClient注解会引入主要两个bean providerStarter和rpcConsumerPostProcessor
providerStarter在监听到容器刷新完成后会开启NettyServer 绑定ip和端口，并向nacos中注册信息
rpcConsumerPostProcessor后置处理器会将@RpcAutowired的成员生成代理对象 该代理层会开启NettyClient发送消息

目前支持的功能：
1、提供了nacos方式的注册中心
2、服务路由和轮询方式的负载
3、简单的重试机制和超时机制
4、自定义的协议和json方式的序列化
5、简单的异常处理

遇到过的主要问题：
1、消费方发送完数据如何拿到结果 Netty提供了Promise方式  调用时使用promise.get方法（支持一定时间的等待），结果返回时通过入栈处理器通过promise
setSuccess()方法 唤醒等待的线程拿到结果
2、处理异常时注意重新设置新的异常信息，如果直接将原来的异常塞到返回值中会有问题，因为原来的异常栈信息会非常大，很有可能超过我们的粘包半包处理器处理的范围
3、复用channel，刚开始发送一次请求客户端连接一个channel，导致系统性能下降，通过线程安全的（double check的）方式在Map中存放对应ip端口的channel

将来要做的事情：
1、以spi的方式提供注册中心 提供序列化方式
2、增加更多的负载均衡算法
3、请求前后增加拦截器，对请求前后拓展