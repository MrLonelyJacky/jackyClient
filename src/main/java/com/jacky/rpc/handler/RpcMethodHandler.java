package com.jacky.rpc.handler;

import feign.Client;
import feign.InvocationHandlerFactory;

/**
 * @description:
 * @author: jacky
 * @create: 2023-01-17 16:33
 **/
public class RpcMethodHandler implements InvocationHandlerFactory.MethodHandler {
    private Client client;

    @Override
    public Object invoke(Object[] objects) throws Throwable {
        return null;
    }
}
