package com.example.springboot.listener;

import com.example.springboot.handler.RpcRequestMessageHandler;
import com.example.springboot.protocal.MessageCodecSharable;
import com.example.springboot.protocal.ProcotolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @description:
 * @author: jacky
 * @create: 2023-01-13 16:00
 **/
public class ServiceBean implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware {

    private transient String beanName;

    private transient ApplicationContext applicationContext;

    @Override
    public void setBeanName(String s) {
        this.beanName = beanName;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
        //自定义的数据协议
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        RpcRequestMessageHandler rpcRequestMessageHandler = new RpcRequestMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new ProcotolFrameDecoder());
                            nioSocketChannel.pipeline().addLast(loggingHandler);
                            nioSocketChannel.pipeline().addLast(messageCodecSharable);
                            nioSocketChannel.pipeline().addLast(rpcRequestMessageHandler);
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(20220).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
