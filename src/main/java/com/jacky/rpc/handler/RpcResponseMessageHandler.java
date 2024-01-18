package com.jacky.rpc.handler;

import com.jacky.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    public static final Map<Integer, Promise<Object>> promiseMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage rpcRequestMessage) throws Exception {
        System.out.println("============rpc开始输出"+rpcRequestMessage+"============");
        //这里用remove防止map越来越大
        Promise<Object> promise = promiseMap.remove(rpcRequestMessage.getSequenceId());
        if (promise!=null){
            Object returnValue = rpcRequestMessage.getReturnValue();
            if (rpcRequestMessage.getExceptionValue()!=null){
                promise.setFailure(rpcRequestMessage.getExceptionValue());
            }else {
                promise.setSuccess(returnValue);
            }
        }
    }
}
