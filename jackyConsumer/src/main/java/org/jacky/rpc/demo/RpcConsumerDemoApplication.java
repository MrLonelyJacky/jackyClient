package org.jacky.rpc.demo;

import com.jacky.rpc.anno.EnableRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpcClient
public class RpcConsumerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerDemoApplication.class, args);
    }


}
