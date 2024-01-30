package com.jacky.rpc.provider;

import com.jacky.rpc.anno.RpcAutowired;
import com.jacky.rpc.anno.RpcService;
import com.jacky.rpc.consumer.RpcProxyHandler;
import com.jacky.rpc.router.LoadBalancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @Author: jacky
 * @Date:2024/1/27 16:26
 * @Description: 对声明了RpcService的类进行增强
 **/
public class RpcServicePostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
        if (annotation != null) {
            RpcServiceMapUtil.addService(bean.getClass().getInterfaces()[0].getName(), bean);
        }

        return bean;
    }

    public static void main(String[] args) {
        System.out.println(RpcServicePostProcessor.class.getName());
    }
}
