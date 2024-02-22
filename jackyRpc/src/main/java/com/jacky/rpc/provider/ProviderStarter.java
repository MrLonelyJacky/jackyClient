package com.jacky.rpc.provider;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.jacky.rpc.protocal.handler.MessageCodecSharable;
import com.jacky.rpc.protocal.handler.ProcotolFrameDecoder;
import com.jacky.rpc.protocal.handler.RpcRequestHandler;
import com.jacky.rpc.router.RpcNacosProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Author: jacky
 * @Date:2024/1/24 11:42
 * @Description: 监听spring容器启动 spring容器启动后完成两件事
 * 1、开启服务端netty   2、将ip端口注册到nacos中
 **/
@Component
public class ProviderStarter implements ApplicationListener<ContextRefreshedEvent>, EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(ProviderStarter.class);

    @Autowired
    private RpcNacosProperties rpcNacosProperties;

    @Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
    private String service;

    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    private Environment environment;


    private void startRpcServer() {
        boss = new NioEventLoopGroup(1);
        work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
            serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new ProcotolFrameDecoder())
                                    .addLast(loggingHandler)
                                    .addLast(messageCodecSharable)
                                    .addLast(new RpcRequestHandler());
                        }
                    });
            String addr = environment.getProperty("rpc.serverAddr");
            int port = Integer.parseInt(environment.getProperty("rpc.port"));
            logger.info("netty start bind addr:{},port:{}", addr, port);
            ChannelFuture sync = serverBootstrap.bind(addr, port).sync();
            //绑定端口成功后注册nacos
            registerNacos(addr, port);
            sync.channel().closeFuture().sync();
            Runtime.getRuntime().addShutdownHook(new Thread(() ->
            {
                logger.info("ShutdownHook execute start...");
                logger.info("Netty NioEventLoopGroup shutdownGracefully...");
                logger.info("Netty NioEventLoopGroup shutdownGracefully2...");
                boss.shutdownGracefully();
                work.shutdownGracefully();
                logger.info("ShutdownHook execute end...");
            }, "shutDown-thread"));
        } catch (InterruptedException | NacosException e) {
            throw new RuntimeException("启动失败：{}", e);
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

    /**
     * 向nacos注册
     *
     * @param addr 地址
     * @param port ip
     * @throws NacosException
     */
    private void registerNacos(String addr, int port) throws NacosException {
        NamingService namingService = NacosFactory.createNamingService(rpcNacosProperties.getNacosProperties());
        Instance instance = new Instance();
        instance.setIp(addr);
        instance.setPort(port);
        instance.setWeight(rpcNacosProperties.getWeight());
        instance.setClusterName(rpcNacosProperties.getClusterName());
        namingService.registerInstance(service, rpcNacosProperties.getGroup(), instance);
        logger.info("注册nacos成功 addr：{} port：{}", addr, port);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Thread thread = new Thread(this::startRpcServer);
        thread.setDaemon(true);
        thread.start();
    }
}
