package com.jacky.rpc.protocal;

import java.io.Serializable;

/**
 * @Author: jacky
 * @Date:2024/1/18 11:58
 * @Description: 消息
 **/
public class RpcProtocol<T> implements Serializable {

    private MsgHeader header;
    private T body;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
