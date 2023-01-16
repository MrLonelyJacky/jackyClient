package com.example.springboot.protocal;

import com.example.springboot.util.RedisUtil;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequenceIdGenerator {
    private static final RedisUtil redisUtil =RedisUtil.getInstance();

    public static int nextId() {
        return (int) redisUtil.incr("feignResultId",1);
    }
}
