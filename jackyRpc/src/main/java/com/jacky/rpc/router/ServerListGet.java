package com.jacky.rpc.router;

import com.jacky.rpc.common.ServiceMeta;

import java.util.List;

/**
 * @Author: jacky
 * @Date:2024/1/20 18:04
 * @Description: 获取服务列表
 **/
public interface ServerListGet {
    List<ServiceMeta> getServerList(String serviceId);
}
