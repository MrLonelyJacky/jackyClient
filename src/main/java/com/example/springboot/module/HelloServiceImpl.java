package com.example.springboot.module;

public class HelloServiceImpl implements HelloService{
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
