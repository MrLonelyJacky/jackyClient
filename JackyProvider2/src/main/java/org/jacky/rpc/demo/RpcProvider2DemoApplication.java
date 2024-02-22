package org.jacky.rpc.demo;

import com.jacky.rpc.anno.EnableRpcProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableRpcProvider
public class RpcProvider2DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcProvider2DemoApplication.class, args);
    }
}
