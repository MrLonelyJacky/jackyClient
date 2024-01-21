package com.jacky.rpc.router;

import com.jacky.rpc.common.ServiceMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jacky
 * @Date:2024/1/20 18:01
 * @Description: 轮询的负载均衡器，仿照的ribbon
 **/
public class RoundingBalancer implements LoadBalancer {

    private ServerListGet serverListGet;
    private ConcurrentHashMap<String, AtomicInteger> nextServerCyclicCounterMap = new ConcurrentHashMap<>();
    private final Object putCounterLock = new Object();

    public RoundingBalancer(ServerListGet serverListGet) {
        this.serverListGet = serverListGet;
    }


    @Override
    public ServiceMeta choose(String serviceId) {

        //double check 保证线程安全
        if (nextServerCyclicCounterMap.get(serviceId) == null) {
            synchronized (putCounterLock) {
                if (nextServerCyclicCounterMap.get(serviceId) == null) {
                    nextServerCyclicCounterMap.put(serviceId, new AtomicInteger(0));
                }
            }
        }
        List<ServiceMeta> serviceMetas = getReacheableServers(serviceId);
        int nextServerIndex = incrementAndGetModulo(serviceMetas.size(), nextServerCyclicCounterMap.get(serviceId));
        ServiceMeta server = serviceMetas.get(nextServerIndex);
        return server;
    }

    private List<ServiceMeta> getReacheableServers(String serviceId) {
        //目前写死nacos  todo 改为spi的方式
        return serverListGet.getServerList(serviceId);
    }

    /**
     * 通过cas来获取下标 目的是线程安全
     * @param modulo
     * @param nextServerCyclicCounter
     * @return
     */

    private int incrementAndGetModulo(int modulo, AtomicInteger nextServerCyclicCounter) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }
}
