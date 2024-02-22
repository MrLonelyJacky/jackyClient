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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(RpcProxyHandler.class);


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
        request.setData(ObjectUtils.isEmpty(args) ? new Object[0] : args);
        //request.setDataClass(ObjectUtils.isEmpty(args) ? null : args[0].getClass());
        protocol.setBody(request);
        //todo 过滤器或拦截器在请求之前做拓展
        //路由发现和负载均衡
        ServiceMeta chooseServer = loadBalancer.choose(rpcAutowired.serviceName());
        Promise<RpcResponse> defaultPromise = new DefaultPromise<>(new DefaultEventLoop());
        RpcRequestHolder.REQUEST_MAP.put(header.getRequestId(), defaultPromise);
        int retryCount = rpcAutowired.retryCount();
        int count = 1;
        while (count <= retryCount) {
            try {
                RpcConsumerClient.getConsumerInstance().sendData(protocol, chooseServer);

                RpcResponse rpcResponse = defaultPromise.get(rpcAutowired.timeout(), TimeUnit.MILLISECONDS);
                if (rpcResponse.getException() != null) {
                    throw rpcResponse.getException();
                }
                logger.info("rpc 调用成功, serviceName: {}", rpcAutowired.serviceName());
                //todo 过滤器或拦截器在此请求后拓展
                return rpcResponse.getData();
            } catch (Throwable throwable) {
                logger.error("rpc 调用失败,进行第{}次重试,异常信息: {}", count, throwable.toString());
                count++;
                //重新负载选择新机器 todo 目前的设计就是失败重新选择节点
                chooseServer = loadBalancer.choose(rpcAutowired.serviceName());
            }

        }


        throw new RuntimeException("rpc调用服务" + rpcAutowired.serviceName() + "失败，超过重试次数" + rpcAutowired.retryCount());
    }
}
