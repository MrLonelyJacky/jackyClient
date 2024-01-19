package com.jacky.rpc.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jacky
 * @Date:2024/1/19 19:12
 * @Description:
 **/
@Configuration
public class LoadBalanceConfig {

    @Bean
    @ConditionalOnMissingClass("com.netflix.loadbalancer.ILoadBalancer")
    public ILoadBalancer loadBalancer() {
        return new ZoneAwareLoadBalancer<>();
    }
}
