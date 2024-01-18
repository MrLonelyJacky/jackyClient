package com.jacky.rpc.query;

import java.lang.reflect.Method;

/**
 * @description: 默认方法handler
 * @author: jacky
 * @create: 2022-11-23 10:53
 **/
public final class DefaultMethodHandler implements MethodHandler{

    private final Method method;

    public DefaultMethodHandler(Method method) {
        this.method = method;
    }

    @Override
    public Object invoke(Object[] argv) throws Throwable {
        return method.invoke(argv);
    }
}
