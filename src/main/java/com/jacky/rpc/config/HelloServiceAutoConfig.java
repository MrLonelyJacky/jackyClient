package com.jacky.rpc.config;

import com.jacky.rpc.query.JackyClientContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.example.springboot"})
public class HelloServiceAutoConfig {

    @Bean
    public JackyClientContext jackyClientContext() {
        return new JackyClientContext();
    }
}
