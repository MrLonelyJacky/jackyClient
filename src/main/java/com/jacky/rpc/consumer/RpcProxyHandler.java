package com.jacky.rpc.consumer;

import com.jacky.rpc.anno.RpcAutowired;
import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.constant.MsgType;
import com.jacky.rpc.protocal.constant.ProtocolConstants;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @Author: jacky
 * @Date:2024/1/18 11:58
 * @Description:
 **/
public class RpcProxyHandler implements InvocationHandler {
    private final RpcAutowired rpcAutowired;

    public RpcProxyHandler(RpcAutowired rpcAutowired) {
        this.rpcAutowired = rpcAutowired;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //具体调用的逻辑
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        MsgHeader header = new MsgHeader();

        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(UUID.randomUUID().toString());
        //todo 序列化
        /*final byte[] serialization = RpcProperties.getInstance().getSerialization().getBytes();
        header.setSerializationLen(serialization.length);
        header.setSerializations(serialization);*/
        header.setMsgType((byte) MsgType.REQUEST.ordinal());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);
        //构建请求体
        RpcRequest request = new RpcRequest();
        request.setVersion(rpcAutowired.serviceVersion());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setData(ObjectUtils.isEmpty(args) ? new Object[0] : args);
        request.setDataClass(ObjectUtils.isEmpty(args) ? null : args[0].getClass());
        protocol.setBody(request);
        //todo 过滤器或拦截器在请求之前做拓展
        //路由发现和负载均衡

        return null;
    }
}
