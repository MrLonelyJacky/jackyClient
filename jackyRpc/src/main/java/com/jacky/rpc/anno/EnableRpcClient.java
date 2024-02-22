package com.jacky.rpc.anno;

import com.jacky.rpc.consumer.RpcBeanRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: jacky
 * @Date:2024/1/24 10:51
 * @Description: 开启服务提供方注解
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcBeanRegistry.class)
public @interface EnableRpcClient {
}
