package com.example.springboot.query;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class JackyClientFactoryBean implements FactoryBean<Object>, ApplicationContextAware {
    private ApplicationContext applicationContext;


    private Class<?> type;

    private String name;

    private String url;

    private String contextId;

    private String path;

    @Override
    public Object getObject() throws Exception {
        JackyClientContext contextBean = applicationContext.getBean(JackyClientContext.class);
        //contextBean.getInstance();
        Map<Method, MethodHandler> methodToHandler = new LinkedHashMap<>();
        Method[] methods = this.type.getMethods();
        for (Method method : methods) {
            if (method.getDeclaringClass()==Object.class ){
                //Object 方法
                continue;
            }else {
                methodToHandler.put(method,new DefaultMethodHandler(method));
            }
        }

        Proxy.newProxyInstance()
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
