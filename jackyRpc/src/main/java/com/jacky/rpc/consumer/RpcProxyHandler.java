package com.jacky.rpc.consumer;

import com.jacky.rpc.anno.RpcAutowired;
import com.jacky.rpc.common.RpcRequestHolder;
import com.jacky.rpc.common.ServiceMeta;
import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.RpcResponse;
import com.jacky.rpc.protocal.constant.MsgType;
import com.jacky.rpc.protocal.constant.ProtocolConstants;
import com.jacky.rpc.router.LoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jacky
 * @Date:2024/1/18 11:58
 * @Description: rpc代理层  对所有使用@RpcAutowired的成员进行代理
 **/
public class RpcProxyHandler implements InvocationHandler {
    private final RpcAutowired rpcAutowired;
    private final LoadBalancer loadBalancer;
    public RpcProxyHandler(RpcAutowired rpcAutowired, LoadBalancer loadBalancer) {
        this.rpcAutowired = rpcAutowired;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //具体调用的逻辑
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        //构建请求头
        MsgHeader header = new MsgHeader();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet());
        header.setMsgType((byte) MsgType.REQUEST.ordinal());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);
        //构建请求体
        RpcRequest request = new RpcRequest();
        request.setVersion(rpcAutowired.serviceVersion());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        //todo 目前只支持一个参数
        request.setData(ObjectUtils.isEmpty(args) ? new Object[0] : args[0]);
        request.setDataClass(ObjectUtils.isEmpty(args) ? null : args[0].getClass());
        protocol.setBody(request);
        //todo 过滤器或拦截器在请求之前做拓展
        //路由发现和负载均衡
        ServiceMeta chooseServer = loadBalancer.choose(rpcAutowired.serviceName());
        Promise<RpcResponse> defaultPromise = new DefaultPromise<>(new DefaultEventLoop());
        RpcRequestHolder.REQUEST_MAP.put(header.getRequestId(), defaultPromise);
        //todo 重试机制
        RpcConsumerClient.getConsumerInstance().sendData(protocol, chooseServer);
        //todo 目前写死3s钟
        RpcResponse rpcResponse = defaultPromise.get(3000, TimeUnit.MILLISECONDS);
        if (rpcResponse.getException()!=null){
            throw rpcResponse.getException();
        }
        //todo 拦截器在此请求后拓展

        return rpcResponse.getData();
    }
}
