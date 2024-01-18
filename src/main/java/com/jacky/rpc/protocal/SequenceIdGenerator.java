package com.jacky.rpc.protocal;

public abstract class SequenceIdGenerator {
    private static final RedisUtil redisUtil =RedisUtil.getInstance();

    public static int nextId() {
        return (int) redisUtil.incr("feignResultId",1);
    }
}
