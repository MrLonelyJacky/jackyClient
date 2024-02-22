package org.jacky.rpc.demo.controller;

import com.jacky.rpc.anno.RpcAutowired;
import org.demo.rpc.demo.Test2Service;
import org.demo.rpc.demo.TestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description:
 * @Author: jacky
 */
@RestController
public class Test {

    @RpcAutowired(serviceName = "lease-provider")
    TestService testService;

    @RpcAutowired(serviceName = "lease-provider")
    Test2Service test2Service;

    /**
     * 轮询
     * 会触发故障转移,提供方模拟异常
     * @param key
     * @return
     */
    @RequestMapping("test/{key}")
    public String test(@PathVariable String key){
        testService.test(key);
        return "test1 ok";
    }

    @RequestMapping("test/{key}/{key2}")
    public String testParams(@PathVariable String key,@PathVariable String key2){
        testService.testParam(key,key2);
        return "test1 ok";
    }

    /**
     * 一致性哈希
     * @param key
     * @return
     */
    @RequestMapping("test2/{key}")
    public String test2(@PathVariable String key){

        return test2Service.test(key);
    }

    /**
     * 轮询,无如何异常
     * @param key
     * @return
     */
    @RequestMapping("test3/{key}")
    public String test3(@PathVariable String key){
        testService.test2(key);
        return "test2 ok";
    }

}
