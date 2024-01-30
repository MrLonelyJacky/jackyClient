package com.jacky.rpc.consumer;

import com.jacky.rpc.anno.RpcAutowired;
import com.jacky.rpc.provider.ProviderStarter;
import com.jacky.rpc.provider.RpcServicePostProcessor;
import com.jacky.rpc.router.LoadBalancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @Author: jacky
 * @Date:2024/1/18 10:56
 * @Description:
 **/

public class RpcProviderBeanRegistry implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcServicePostProcessor.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("rpcServicePostProcessor", beanDefinition);
        BeanDefinitionBuilder providerBuilder = BeanDefinitionBuilder.genericBeanDefinition(ProviderStarter.class);
        AbstractBeanDefinition providerBeanDefinition = providerBuilder.getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("providerStarter", providerBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
