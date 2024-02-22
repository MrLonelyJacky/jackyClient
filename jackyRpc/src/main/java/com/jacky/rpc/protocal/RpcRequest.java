package com.jacky.rpc.protocal;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: jacky
 * @Date:2024/1/18 11:58
 * @Description: 消息请求体
 **/
@Data
public class RpcRequest implements Serializable {

    private String version;
    private String className;
    private String methodName;
    private Object data;
    //private Class dataClass;
    private Class<?>[] parameterTypes;
    private Map<String,Object> serviceAttachments;
    private Map<String,Object> clientAttachments;

}
