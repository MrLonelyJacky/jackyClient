package com.jacky.rpc.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: jacky
 * @Date:2024/1/18 18:12
 * @Description:
 **/
@Data
public class ServiceMeta implements Serializable {

    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;

    private String serviceIp;


}
