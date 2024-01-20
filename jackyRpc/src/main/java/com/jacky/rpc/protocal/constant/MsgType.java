package com.jacky.rpc.protocal.constant;

/**
 * @description: 消息类型
 * @Author: jacky
 */
public enum MsgType {

    REQUEST,
    RESPONSE,
    HEARTBEAT;

    public static MsgType findByType(int type) {

        return MsgType.values()[type];
    }
}
