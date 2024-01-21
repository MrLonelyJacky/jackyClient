package com.jacky.rpc.config;

import com.jacky.rpc.router.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jacky
 * @Date:2024/1/19 19:12
 * @Description:
 **/
@Configuration
public class JackyClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancer loadBalancer(ServerListGet serverListGet) {
        return new RoundingBalancer(serverListGet);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcNacosProperties rpcNacosProperties() {
        return new RpcNacosProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerListGet serverListGet(RpcNacosProperties rpcNacosProperties){
        return new NacosServerListGet(rpcNacosProperties);
    }
}
