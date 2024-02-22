package com.jacky.rpc.protocal.handler;

import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.RpcResponse;
import com.jacky.rpc.protocal.constant.MsgType;
import com.jacky.rpc.provider.RpcServiceMapUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: jacky
 * @Date:2024/1/20 13:26
 * @Description:  完成请求调用的handler 找到对应的类，并调用方法
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequest) throws Exception {
        RpcRequest requestBody = rpcRequest.getBody();
        String className = requestBody.getClassName();
        String methodName = requestBody.getMethodName();
        Class<?>[] parameterTypes = requestBody.getParameterTypes();
        //Object[] data = {requestBody.getData()};
        Object service = RpcServiceMapUtil.getService(className);
        Method method = service.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        List<Object> dataList = (List<Object>) requestBody.getData();
        Object result = method.invoke(service, dataList.toArray());
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        MsgHeader originHeader = rpcRequest.getHeader();
        MsgHeader header = new MsgHeader();
        header.setMagic(originHeader.getMagic());
        header.setMsgType((byte) MsgType.RESPONSE.ordinal());
        header.setRequestId(originHeader.getRequestId());
        header.setVersion(originHeader.getVersion());
        //todo 异常处理
        responseRpcProtocol.setHeader(header);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData(result);
        rpcResponse.setDataClass(result == null ? null : result.getClass());
        responseRpcProtocol.setBody(rpcResponse);
        channelHandlerContext.writeAndFlush(responseRpcProtocol);
    }
}
