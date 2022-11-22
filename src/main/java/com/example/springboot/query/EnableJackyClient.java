package com.example.springboot.query;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(JackyClientsRegistrar.class)
public @interface EnableJackyClient {
    String[] basePackages();
}
