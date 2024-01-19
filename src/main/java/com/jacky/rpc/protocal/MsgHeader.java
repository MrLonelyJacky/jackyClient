package com.jacky.rpc.protocal;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: jacky
 * @Date:2024/1/18 11:58
 * @Description: 消息头
 **/
@Data
public class MsgHeader implements Serializable {

    private short magic; // 魔数
    private byte version; // 协议版本号
    private byte msgType; // 数据类型
    private byte status; // 状态
    private long requestId; // 请求 ID
    private int serializationLen;
    private byte[] serializations;
    private int msgLen; // 数据长度

}
