package com.example.springboot.feign;

import feign.Feign;

/**
 * @description:
 * @author: jacky
 * @create: 2023-01-17 16:31
 **/
public class RpcFeign {


    public static final class Builder extends Feign.Builder {
        @Override
        public Feign build() {
            return build(null);
        }
    }
}
