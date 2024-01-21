package com.jacky.rpc.router;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.jacky.rpc.common.ServiceMeta;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jacky
 * @Date:2024/1/20 21:33
 * @Description:
 **/
public class NacosServerListGet implements ServerListGet {
    private NamingService namingService;
    private RpcNacosProperties rpcNacosProperties;

    public NacosServerListGet(RpcNacosProperties rpcNacosProperties) {
        this.rpcNacosProperties = rpcNacosProperties;
    }

    @Override
    public List<ServiceMeta> getServerList(String serviceId) {
        if (null == namingService) {
            try {
                namingService = NacosFactory.createNamingService(rpcNacosProperties.getNacosProperties());
            } catch (Exception e) {
                throw new RuntimeException("create namingService fail....", e);
            }
        }


        try {
            List<Instance> instances = namingService.selectInstances(serviceId, rpcNacosProperties.getGroup(), true);
            return instancesToServerList(instances);
        } catch (NacosException e) {
            throw new IllegalStateException(
                    "Can not get service instances from nacos, serviceId=" + serviceId,
                    e);
        }

    }

    private List<ServiceMeta> instancesToServerList(List<Instance> instances) {
        List<ServiceMeta> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(instances)) {
            return result;
        }
        for (Instance instance : instances) {
            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceAddr(instance.toInetAddr());
            serviceMeta.setServiceIp(instance.getIp());
            serviceMeta.setServicePort(instance.getPort());
            serviceMeta.setServiceName(instance.getServiceName());
            result.add(serviceMeta);
        }

        return result;
    }
}
