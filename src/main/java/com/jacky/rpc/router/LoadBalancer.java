package com.jacky.rpc.router;

import com.jacky.rpc.common.ServiceMeta;

/**
 * @Author: jacky
 * @Date:2024/1/18 20:57
 * @Description: 负载均衡器
 **/
public interface LoadBalancer {

    ServiceMeta choose(String serviceId);
}
