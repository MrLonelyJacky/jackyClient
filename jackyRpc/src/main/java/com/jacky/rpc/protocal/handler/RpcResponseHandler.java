package com.jacky.rpc.protocal.handler;

import com.jacky.rpc.common.RpcRequestHolder;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

/**
 * @Author: jacky
 * @Date:2024/1/20 13:26
 * @Description: 结果处理器，将结果放入promise中
 **/
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        //这里必须remove防止map越来越大
        Promise<RpcResponse> responsePromise = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        responsePromise.setSuccess(rpcResponseRpcProtocol.getBody());
    }
}
