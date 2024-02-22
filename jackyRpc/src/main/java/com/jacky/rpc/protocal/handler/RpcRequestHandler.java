package com.jacky.rpc.protocal.handler;

import com.jacky.rpc.exception.RpcException;
import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.RpcResponse;
import com.jacky.rpc.protocal.constant.MsgType;
import com.jacky.rpc.provider.RpcServiceMapUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: jacky
 * @Date:2024/1/20 13:26
 * @Description: 完成请求调用的handler 找到对应的类，并调用方法
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequest) throws Exception {
        RpcRequest requestBody = rpcRequest.getBody();
        RpcResponse rpcResponse = new RpcResponse();
        try {
            Object result = submitRequest(requestBody);
            rpcResponse.setData(result);
            rpcResponse.setDataClass(result == null ? null : result.getClass());
        } catch (Exception e) {
            logger.error("处理请求异常：",e);
            rpcResponse.setException(new RpcException("请求调用方法失败：" + e.getCause().getMessage()));
        }
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        MsgHeader originHeader = rpcRequest.getHeader();
        MsgHeader header = new MsgHeader();
        header.setMagic(originHeader.getMagic());
        header.setMsgType((byte) MsgType.RESPONSE.ordinal());
        header.setRequestId(originHeader.getRequestId());
        header.setVersion(originHeader.getVersion());
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(rpcResponse);
        channelHandlerContext.writeAndFlush(responseRpcProtocol);
    }

    /**
     * 提交处理请求 通过反射找到方法和参数 调用方法
     *
     * @param requestBody
     * @return 调用结果
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object submitRequest(RpcRequest requestBody) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String className = requestBody.getClassName();
        String methodName = requestBody.getMethodName();
        Class<?>[] parameterTypes = requestBody.getParameterTypes();
        Object service = RpcServiceMapUtil.getService(className);
        Method method = service.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        List<Object> dataList = (List<Object>) requestBody.getData();
        return method.invoke(service, dataList.toArray());
    }
}
