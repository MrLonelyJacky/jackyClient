package com.jacky.rpc.anno;

import com.jacky.rpc.consumer.RpcConsumerPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: jacky
 * @Date:2024/1/18 10:51
 * @Deprecated: 已废弃，消费者和提供者已经整合到一起了
 * @Description: 开启消费方装配注解
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcConsumerPostProcessor.class)
@Deprecated
public @interface EnableRpcConsumer {
}
