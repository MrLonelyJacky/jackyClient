package com.example.springboot.client;

import com.example.springboot.handler.RpcResponseMessageHandler;
import com.example.springboot.message.RpcRequestMessage;
import com.example.springboot.module.HelloService;
import com.example.springboot.protocal.MessageCodecSharable;
import com.example.springboot.protocal.ProcotolFrameDecoder;
import com.example.springboot.protocal.SequenceIdGenerator;
import com.example.springboot.util.RedisUtil;
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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: jacky
 * @create: 2023-01-03 09:30
 **/
public class RpcClientManage {
    private static Channel channel;

    private static final Object lock = new Object();

    private static final Map<InetSocketAddress,Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * double check
     *
     * @return
     * @param inetAddress
     */
    public static Channel getChannel(InetSocketAddress inetAddress) {
        Channel channel = channelMap.get(inetAddress);
        if (channel != null) {
            return channel;
        }
        synchronized (lock) {
            if (channelMap.get(inetAddress) != null) {
                return channelMap.get(inetAddress);
            }
            Channel initChannel = initChannel(inetAddress);
            channelMap.put(inetAddress, initChannel);
            return initChannel;
        }
    }


    public static <T> T getProxyService(Class<T> serviceClass) {

        Object o = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, ((proxy, method, args) -> {
            FeignClient annotation = serviceClass.getAnnotation(FeignClient.class);
            String url = annotation.url();
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            String name = postMapping.name();
            InetSocketAddress inetAddress = (InetSocketAddress) RedisUtil.getInstance().get(url + name);
            RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(SequenceIdGenerator.nextId(), serviceClass.getName(), method.getName()
                    , method.getReturnType(), method.getParameterTypes(), args);
            Channel channel = getChannel(inetAddress);
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

    private static Channel initChannel(InetSocketAddress inetAddress) {
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
        Channel channel =null;
        try {
            ChannelFuture sync = bootstrap.connect(inetAddress).sync();
            channel = sync.channel();

            channel.closeFuture().addListener(future -> nioEventLoopGroup.shutdownGracefully());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static void main(String[] args) {
        FeignClient annotation = HelloService.class.getAnnotation(FeignClient.class);
        String url = annotation.url();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost",8088);
        InetSocketAddress inetSocketAddress2 = new InetSocketAddress("localhost",8088);
        System.out.println(inetSocketAddress.equals(inetSocketAddress2));
       /* HelloService proxyService = getProxyService(HelloService.class);
        String hahahha = proxyService.sayHello("hahahha");
        System.out.println(hahahha);
        String hehhehe = proxyService.sayHello("hehhehe");
        System.out.println(hehhehe);*/
    }

}
