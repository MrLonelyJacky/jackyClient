package com.jacky.rpc.anno;

import java.lang.annotation.*;

/**
 * @description: 服务调用方注解
 * @Author: jacky
 * @CreateTime: 2024-01-16 22:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface RpcAutowired {
    /**
     * 版本
     * @return
     */
    String serviceVersion() default "1.0";

    /**
     * 超时时间
     * @return
     */
    long timeout() default 5000;

    /**
     * 重试次数
     * @return
     */
    long retryCount() default 3;


}
