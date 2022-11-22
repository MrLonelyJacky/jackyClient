package com.example.springboot.query;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JackyClientConfig {
    @Bean
    @ConditionalOnMissingBean
    public JackyQueryBean jackyQueryBean() {
        JackyQueryBean bean = new JackyQueryBean();
        bean.setCode("jacky_code");
        bean.setName("jacky");
        return bean;
    }
}
