package com.jacky.rpc.consumer;

import com.jacky.rpc.common.ServiceMeta;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.handler.MessageCodecSharable;
import com.jacky.rpc.protocal.handler.ProcotolFrameDecoder;
import com.jacky.rpc.protocal.handler.RpcResponseHandler;
import com.netflix.loadbalancer.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        //开启客户端监听
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new ProcotolFrameDecoder())
                                .addLast(messageCodecSharable)
                                .addLast(new RpcResponseHandler());
                    }
                });

    }

    public void sendData(RpcProtocol<RpcRequest> protocol, ServiceMeta chooseServer) throws InterruptedException {
        if (chooseServer != null) {
            ChannelFuture channelFuture = this.bootstrap.connect(chooseServer.getServiceIp(), chooseServer.getServicePort()).sync();
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    logger.info("连接 rpc server {} 端口 {} 成功.", chooseServer.getServiceIp(), chooseServer.getServicePort());
                } else {
                    logger.error("连接 rpc server {} 端口 {} 失败.", chooseServer.getServiceIp(), chooseServer.getServicePort());
                    channelFuture1.cause().printStackTrace();
                }
            });
            ChannelFuture writeAndFlush = channelFuture.channel().writeAndFlush(protocol);
            writeAndFlush.addListener((ChannelFutureListener) channelFuture12 -> {
                Channel channel = writeAndFlush.channel();
                channel.close();
                logger.info("发送完数据，关闭通道！");
            });

        }

    }
}
