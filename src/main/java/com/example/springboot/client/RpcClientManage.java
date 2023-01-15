package com.example.springboot.client;

import com.example.springboot.handler.RpcResponseMessageHandler;
import com.example.springboot.message.RpcRequestMessage;
import com.example.springboot.protocal.MessageCodecSharable;
import com.example.springboot.protocal.ProcotolFrameDecoder;
import com.example.springboot.protocal.SequenceIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;


import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: jacky
 * @create: 2023-01-03 09:30
 **/
public class RpcClientManage {
    private static Channel channel;

    private static final Object lock = new Object();

    /**
     * double check
     *
     * @return
     */
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (lock) {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }


    public static <T> T getProxyService(Class<T> serviceClass) {

        Object o = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, ((proxy, method, args) -> {
            RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(SequenceIdGenerator.nextId(), serviceClass.getName(), method.getName()
                    , method.getReturnType(), method.getParameterTypes(), args);
            Channel channel = getChannel();
            //channel.eventLoop() 构造方法需要一个去异步处理的线程，虽然这里并没有用到异步
            DefaultPromise<Object> objectDefaultPromise = new DefaultPromise<>(channel.eventLoop());
            RpcResponseMessageHandler.promiseMap.put(rpcRequestMessage.getSequenceId(), objectDefaultPromise);
            channel.writeAndFlush(rpcRequestMessage);
            Promise<Object> await = objectDefaultPromise.await();
            if (await.isSuccess()) {
                return await.getNow();
            } else {
                throw new RuntimeException(await.cause());
            }
        }));
        @SuppressWarnings("unchecked")
        T result = (T) o;
        return result;
    }

    private static void initChannel() {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
        //自定义的数据协议
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        //解决粘包半包问题
                        socketChannel.pipeline().addLast(new ProcotolFrameDecoder());
                        socketChannel.pipeline().addLast(loggingHandler);
                        socketChannel.pipeline().addLast(messageCodecSharable);
                        socketChannel.pipeline().addLast(rpcResponseMessageHandler);
                    }
                });
        try {
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 8088).sync();
            channel = sync.channel();

            channel.closeFuture().addListener(future -> nioEventLoopGroup.shutdownGracefully());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       /* HelloService proxyService = getProxyService(HelloService.class);
        String hahahha = proxyService.sayHello("hahahha");
        System.out.println(hahahha);
        String hehhehe = proxyService.sayHello("hehhehe");
        System.out.println(hehhehe);*/
    }

}
