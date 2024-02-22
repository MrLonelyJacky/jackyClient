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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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

    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerClient.class);
    private static Map<String, Channel> channelMap = new HashMap<>();

    private static final Object lock = new Object();
    private static Bootstrap bootstrap;

    static {
        bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

        //开启客户端监听
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new ProcotolFrameDecoder())
                                .addLast(loggingHandler)
                                .addLast(messageCodecSharable)
                                .addLast(new RpcResponseHandler());
                    }
                });

    }

    public static void sendData(RpcProtocol<RpcRequest> protocol, ServiceMeta chooseServer) throws InterruptedException {
        if (chooseServer != null) {
            Channel channel = channelMap.get(chooseServer.getServiceAddr());
            //double check 确保线程安全
            if (channel == null) {
                synchronized (lock) {
                    if (channelMap.get(chooseServer.getServiceAddr()) == null) {
                        ChannelFuture channelFuture = bootstrap.connect(chooseServer.getServiceIp(), chooseServer.getServicePort()).sync();
                        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                            if (channelFuture1.isSuccess()) {
                                logger.info("连接 rpc server {} 端口 {} 成功.", chooseServer.getServiceIp(), chooseServer.getServicePort());
                            } else {
                                logger.error("连接 rpc server {} 端口 {} 失败.", chooseServer.getServiceIp(), chooseServer.getServicePort());
                                channelFuture1.cause().printStackTrace();
                            }
                        });
                        channelMap.put(chooseServer.getServiceAddr(), channelFuture.channel());
                        channelFuture.channel().writeAndFlush(protocol);
                    }
                }
            } else {
                channel.writeAndFlush(protocol);
            }

        }

    }


}
