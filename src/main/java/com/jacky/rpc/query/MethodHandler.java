package com.jacky.rpc.query;

public interface MethodHandler {
    Object invoke(Object[] argv) throws Throwable;
}
