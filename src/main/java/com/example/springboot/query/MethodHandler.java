package com.example.springboot.query;

public interface MethodHandler {
    Object invoke(Object[] argv) throws Throwable;
}
