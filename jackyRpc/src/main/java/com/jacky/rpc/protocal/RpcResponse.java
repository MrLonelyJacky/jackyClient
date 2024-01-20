package com.jacky.rpc.protocal;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 响应结果实体
 * @Author: jacky
 * @Date: 2024/1/19 11:58
 */
@Data
public class RpcResponse implements Serializable {

    private Object data;
    private Class dataClass;
    private String message;
    private Exception exception;
}
