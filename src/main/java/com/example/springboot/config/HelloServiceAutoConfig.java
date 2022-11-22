package com.example.springboot.config;

import com.example.springboot.query.JackyClientContext;
import com.example.springboot.query.JackyQueryBean;
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
