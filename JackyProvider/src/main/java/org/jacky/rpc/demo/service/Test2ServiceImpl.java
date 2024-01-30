package org.jacky.rpc.demo.service;


import com.jacky.rpc.anno.RpcService;
import org.demo.rpc.demo.Test2Service;

/**
 * @description:
 * @Author: Xhy
 *
 */
@RpcService
public class Test2ServiceImpl implements Test2Service {

    @Override
    public String test(String key) {
        System.out.println("服务提供1 test2 测试成功 :" + key);
        return key;
    }
}
