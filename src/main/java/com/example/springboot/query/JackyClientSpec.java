package com.example.springboot.query;

import org.springframework.cloud.context.named.NamedContextFactory;

public class JackyClientSpec implements NamedContextFactory.Specification{
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Class<?>[] getConfiguration() {
        return new Class[0];
    }
}
