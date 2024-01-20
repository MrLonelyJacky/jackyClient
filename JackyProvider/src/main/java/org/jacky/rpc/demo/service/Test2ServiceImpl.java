package org.xhystudy.rpc.demo.service;

import org.xhystudy.rpc.demo.Test2Service;
import org.xhystudy.rpc.annotation.RpcService;

/**
 * @description:
 * @Author: Xhy
 * @gitee: https://gitee.com/XhyQAQ
 * @copyright: B站: https://space.bilibili.com/152686439
 * @CreateTime: 2023-05-09 10:45
 */
@RpcService
public class Test2ServiceImpl implements Test2Service {

    @Override
    public String test(String key) {
        System.out.println("服务提供1 test2 测试成功 :" + key);
        return key;
    }
}
