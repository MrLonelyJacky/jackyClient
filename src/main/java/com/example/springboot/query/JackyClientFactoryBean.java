package com.example.springboot.query;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class JackyClientFactoryBean implements FactoryBean<Object>, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        JackyClientContext contextBean = applicationContext.getBean(JackyClientContext.class);
        contextBean.getInstance();
        Map<Method, MethodHandler> methodToHandler = new LinkedHashMap<>();

        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
