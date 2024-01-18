package com.jacky.rpc.query;

import org.springframework.cloud.context.named.NamedContextFactory;

public class JackyClientContext extends NamedContextFactory<JackyClientSpec> {
    public JackyClientContext() {
        super(JackyClientConfig.class, "jacky", "jacky.client.name");
    }
}
