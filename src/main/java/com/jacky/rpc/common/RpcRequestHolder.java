package com.jacky.rpc.common;

import com.jacky.rpc.protocal.RpcResponse;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description: 请求连接
 * @Author: jacky
 * @Date:2024/1/19 11:58
 */
public class RpcRequestHolder {
    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    // 绑定请求
    public static final Map<Long, Promise<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
