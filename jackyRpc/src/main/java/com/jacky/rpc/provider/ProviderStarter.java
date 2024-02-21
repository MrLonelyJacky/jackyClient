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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Author: jacky
 * @Date:2024/1/24 11:42
 * @Description: rpc 服务提供者启动starter spring容器启动后完成两件事
 * 1、开启服务端netty   2、将ip端口注册到nacos中
 **/
@Component
public class ProviderStarter implements SmartLifecycle, EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(ProviderStarter.class);

    @Autowired
    private RpcNacosProperties rpcNacosProperties;

    @Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
    private String service;

    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    private boolean isRunning;
    private Environment environment;


    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
    }

    @Override
    public void start() {
        startRpcServer();
    }

    private void startRpcServer() {
        boss = new NioEventLoopGroup(1);
        work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
            serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new ProcotolFrameDecoder())
                                    .addLast(messageCodecSharable)
                                    .addLast(new RpcRequestHandler());
                        }
                    });
            String addr = environment.getProperty("rpc.serverAddr");
            int port = Integer.parseInt(environment.getProperty("rpc.port"));
            logger.info("netty start bind addr:{},port:{}", addr, port);
            //todo 端口改为读取配置
            ChannelFuture sync = serverBootstrap.bind(addr, port).sync();
            registerNacos(addr, port);
            isRunning = true;
            sync.channel().closeFuture().sync();
        } catch (InterruptedException | NacosException e) {
            throw new RuntimeException("启动失败：{}", e);
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

    /**
     * 向nacos注册
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
    public void stop() {
        boss.shutdownGracefully();
        work.shutdownGracefully();
        isRunning = false;
        logger.info("netty close......");
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
