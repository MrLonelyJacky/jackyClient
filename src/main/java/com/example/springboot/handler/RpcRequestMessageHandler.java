package com.example.springboot.handler;

import com.example.springboot.message.RpcRequestMessage;
import com.example.springboot.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage rpcRequestMessage) throws Exception {
        System.out.println("==============开始rpc调用=============");
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        rpcResponseMessage.setSequenceId(rpcRequestMessage.getSequenceId());
        try {
            Object service = ServicesFactory.getService(Class.forName(rpcRequestMessage.getInterfaceName()));
            Method method = service.getClass().getMethod(rpcRequestMessage.getMethodName(), rpcRequestMessage.getParameterTypes());
            Object invoke = method.invoke(service, rpcRequestMessage.getParameterValue());
            rpcResponseMessage.setReturnValue(invoke);
        }catch (Exception e){
            e.printStackTrace();
            rpcResponseMessage.setExceptionValue(e);
        }
        ctx.writeAndFlush(rpcResponseMessage);

    }
}
