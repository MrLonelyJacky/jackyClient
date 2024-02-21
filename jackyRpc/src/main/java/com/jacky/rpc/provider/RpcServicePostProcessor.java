package com.jacky.rpc.provider;

import com.jacky.rpc.anno.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

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


}
