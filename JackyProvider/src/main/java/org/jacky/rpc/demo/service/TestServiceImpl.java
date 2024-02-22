package org.jacky.rpc.demo.service;


import com.jacky.rpc.anno.RpcService;
import org.demo.rpc.demo.TestService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @Author: jacky
 */
@RpcService
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

    @Override
    public void testParam(String key, String key2) {
        System.out.println("测试多个参数："+key+"_"+key2);
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestServiceImpl service = new TestServiceImpl();
        Method method = service.getClass().getMethod("test2", String.class);
        method.setAccessible(true);

        Object result = method.invoke(service, new Object[]{"aaaa"});
    }
}
