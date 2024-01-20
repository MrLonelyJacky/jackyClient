package org.xhystudy.rpc.demo.service;

import org.xhystudy.rpc.demo.TestService;
import org.xhystudy.rpc.annotation.RpcService;

/**
 * @description:
 * @Author: Xhy
 * @gitee: https://gitee.com/XhyQAQ
 * @copyright: B站: https://space.bilibili.com/152686439?spm_id_from=333.1007.0.0
 * @CreateTime: 2023-04-12 11:15
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


}
