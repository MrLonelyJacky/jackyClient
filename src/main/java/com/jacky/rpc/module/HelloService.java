package com.jacky.rpc.module;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "lease-public", url = "http://127.0.0.1:9287/lease-public")
public interface HelloService {

    @PostMapping("/findAnnexList")
    void sayHello();
}
