package com.jacky.rpc.query;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @description:
 * @author: jacky
 * @create: 2022-11-23 11:44
 **/
public class JackyClientInvocationHandler implements InvocationHandler {

    private final Map<Method, MethodHandler> dispatch;

    public JackyClientInvocationHandler(Map<Method, MethodHandler> dispatch) {
        this.dispatch = dispatch;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }

        return dispatch.get(method).invoke(args);
    }
}
