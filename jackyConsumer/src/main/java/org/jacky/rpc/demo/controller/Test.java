package org.jacky.rpc.demo.controller;

import com.jacky.rpc.anno.RpcAutowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.xhystudy.rpc.demo.Test2Service;
import org.xhystudy.rpc.demo.TestService;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @description:
 * @Author: jacky
 */
@RestController
public class Test {

    @RpcAutowired
    TestService testService;

    @RpcAutowired
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
