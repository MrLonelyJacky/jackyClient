package com.jacky.rpc.protocal.handler;

import com.jacky.rpc.common.RpcRequestHolder;
import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.RpcResponse;
import com.jacky.rpc.provider.RpcServiceMapUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.Method;

/**
 * @Author: jacky
 * @Date:2024/1/20 13:26
 * @Description:
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequest) throws Exception {
        RpcRequest requestBody = rpcRequest.getBody();
        String className = requestBody.getClassName();
        String methodName = requestBody.getMethodName();
        Class<?>[] parameterTypes = requestBody.getParameterTypes();
        Object[] data = {requestBody.getData()};
        Object service = RpcServiceMapUtil.getService(className);
        Method method = service.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        method.invoke(service, data);
    }
}
