package com.jacky.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jacky
 * @Date:2024/1/18 18:12
 * @Description:
 **/
public class RpcConsumerClient {
    //客户端boss
    private Bootstrap bootstrap;
    //客户端work group
    private EventLoopGroup eventLoopGroup;
    private Logger logger = LoggerFactory.getLogger(RpcConsumerClient.class);

    private static volatile RpcConsumerClient rpcConsumerClient;

    private static final Object lock = new Object();

    public static RpcConsumerClient getConsumerInstance() {
        if (rpcConsumerClient == null) {
            synchronized (lock) {
                if (rpcConsumerClient == null) {
                    rpcConsumerClient = new RpcConsumerClient();
                }
            }
        }
        return rpcConsumerClient;
    }

    private RpcConsumerClient() {
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup();
        //开启客户端监听
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //todo 编码解码器
                    }
                });

    }

    public void sendData() {
        this.bootstrap.connect();
    }
}
