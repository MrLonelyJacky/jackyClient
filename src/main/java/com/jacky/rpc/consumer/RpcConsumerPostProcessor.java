package com.jacky.rpc.consumer;

import com.jacky.rpc.anno.RpcAutowired;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: jacky
 * @Date:2024/1/18 10:56
 * @Description: rpc消费方后处理器 用于对所有用RpcAutowired注解的属性进行增强
 **/
@Component
public class RpcConsumerPostProcessor implements BeanPostProcessor {
    //todo log

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Field[] declaredFields = bean.getClass().getDeclaredFields();

        // 遍历所有字段找到 RpcAutowired 注解的字段
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(RpcAutowired.class)) {
                RpcAutowired rpcReference = field.getAnnotation(RpcAutowired.class);
                Class<?> fieldType = field.getType();
                Object proxyInstance = Proxy.newProxyInstance(fieldType.getClassLoader(), new Class[]{fieldType}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                });
                field.setAccessible(true);
                try {
                    field.set(bean, proxyInstance);
                } catch (IllegalAccessException e) {
                    //todo log
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
