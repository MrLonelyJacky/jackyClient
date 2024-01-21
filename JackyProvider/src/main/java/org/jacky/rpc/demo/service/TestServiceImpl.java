package org.jacky.rpc.demo.service;


import org.demo.rpc.demo.TestService;

/**
 * @description:
 * @Author: jacky
 */

public class TestServiceImpl implements TestService {

    @Override
    public void test(String key) {
        System.out.println(1/0);
        System.out.println("服务提供1 test 测试成功  :" + key);
    }

    @Override
    public void test2(String key) {
        System.out.println("服务提供1 test2 测试成功  :" + key);
    }


}
