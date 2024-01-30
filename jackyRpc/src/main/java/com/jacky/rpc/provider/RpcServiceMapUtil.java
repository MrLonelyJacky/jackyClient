package com.jacky.rpc.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jacky
 * @Date:2024/1/27 16:28
 * @Description:
 **/
public class RpcServiceMapUtil {

    private static Map<String, Object> rpcServiceMap = new HashMap<>();

    public static void addService(String cName, Object o) {
        rpcServiceMap.put(cName, o);
    }

    public static Object getService(String cName) {
        return rpcServiceMap.get(cName);
    }

}
