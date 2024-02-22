package org.jacky.rpc.demo.service;


import com.jacky.rpc.anno.RpcService;
import org.demo.rpc.demo.TestService;

/**
 * @description:
 * @Author: jacky
 */
@RpcService
public class TestServiceImpl implements TestService {

    @Override
    public void test(String key) {
        //和服务提供1不一样这里不抛异常
        System.out.println("服务提供2 test 测试成功  :" + key);
    }

    @Override
    public void test2(String key) {
        System.out.println("服务提供2 test2 测试成功  :" + key);
    }

    @Override
    public void testParam(String key, String key2) {
        System.out.println("服务提供2 测试多个参数："+key+"_"+key2);
    }


}
