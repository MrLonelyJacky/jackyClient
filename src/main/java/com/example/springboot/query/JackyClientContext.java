package com.example.springboot.query;

import org.springframework.cloud.context.named.NamedContextFactory;

public class JackyClientContext extends NamedContextFactory<JackyClientSpec> {
    public JackyClientContext() {
        super(JackyClientConfig.class, "jacky", "jacky.client.name");
    }
}
