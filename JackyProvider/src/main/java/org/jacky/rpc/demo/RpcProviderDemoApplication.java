package org.xhystudy.rpc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xhystudy.rpc.annotation.EnableProviderRpc;

/**
 * @description:
 * @Author: Xhy
 * @gitee: https://gitee.com/XhyQAQ
 * @copyright: B站: https://space.bilibili.com/152686439?spm_id_from=333.1007.0.0
 * @CreateTime: 2023-04-17 15:45
 */
@SpringBootApplication
@EnableProviderRpc
public class RpcProviderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderDemoApplication.class, args);
    }
}
