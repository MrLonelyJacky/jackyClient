package com.jacky.rpc.router;

import com.alibaba.nacos.api.PropertyKeyConst;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.*;

/**
 * @Author: jacky
 * @Date:2024/1/21 13:00
 * @Description:
 **/
@Component
@ConfigurationProperties("spring.cloud.nacos.discovery")
public class RpcNacosProperties {
    /**
     * nacos discovery server address.
     */
    private String serverAddr = "localhost:8848";

    /**
     * the nacos authentication username.
     */
    private String username;

    /**
     * the nacos authentication password.
     */
    private String password;

    /**
     * the domain name of a service, through which the server address can be dynamically
     * obtained.
     */
    private String endpoint;

    /**
     * namespace, separation registry of different environments.
     */
    private String namespace ="public";

    /**
     * watch delay,duration to pull new service from nacos server.
     */
    private long watchDelay = 30000;

    /**
     * nacos naming log file name.
     */
    private String logName;

    /**
     * cluster name for nacos .
     */
    private String clusterName = "DEFAULT";

    /**
     * group name for nacos.
     */
    private String group = "DEFAULT_GROUP";
    /**
     * if you just want to subscribe, but don't want to register your service, set it to
     * false.
     */
    private boolean registerEnabled = true;

    /**
     * The ip address your want to register for your service instance, needn't to set it
     * if the auto detect ip works well.
     */
    private String ip;

    /**
     * which network interface's ip you want to register.
     */
    private String networkInterface = "";

    /**
     * The port your want to register for your service instance, needn't to set it if the
     * auto detect port works well.
     */
    private int port = -1;

    /**
     * weight for service instance, the larger the value, the larger the weight.
     */
    private float weight = 1;

    /**
     * whether your service is a https service.
     */
    private boolean secure = false;

    /**
     * access key for namespace.
     */
    private String accessKey;

    /**
     * secret key for namespace.
     */
    private String secretKey;

    /**
     * Heart beat interval. Time unit: second.
     */
    private Integer heartBeatInterval;

    /**
     * Heart beat timeout. Time unit: second.
     */
    private Integer heartBeatTimeout;

    /**
     * Ip delete timeout. Time unit: second.
     */
    private Integer ipDeleteTimeout;
    /**
     * naming load from local cache at application start. true is load.
     */
    private String namingLoadCacheAtStart = "false";

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public long getWatchDelay() {
        return watchDelay;
    }

    public void setWatchDelay(long watchDelay) {
        this.watchDelay = watchDelay;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isRegisterEnabled() {
        return registerEnabled;
    }

    public void setRegisterEnabled(boolean registerEnabled) {
        this.registerEnabled = registerEnabled;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getHeartBeatInterval() {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(Integer heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    public Integer getHeartBeatTimeout() {
        return heartBeatTimeout;
    }

    public void setHeartBeatTimeout(Integer heartBeatTimeout) {
        this.heartBeatTimeout = heartBeatTimeout;
    }

    public Integer getIpDeleteTimeout() {
        return ipDeleteTimeout;
    }

    public void setIpDeleteTimeout(Integer ipDeleteTimeout) {
        this.ipDeleteTimeout = ipDeleteTimeout;
    }

    public String getNamingLoadCacheAtStart() {
        return namingLoadCacheAtStart;
    }

    public void setNamingLoadCacheAtStart(String namingLoadCacheAtStart) {
        this.namingLoadCacheAtStart = namingLoadCacheAtStart;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Properties getNacosProperties() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(USERNAME, Objects.toString(username, ""));
        properties.put(PASSWORD, Objects.toString(password, ""));
        properties.put(NAMESPACE, namespace);
        //properties.put("com.alibaba.nacos.naming.log.filename", logName);

        /*if (endpoint.contains(":")) {
            int index = endpoint.indexOf(":");
            properties.put(ENDPOINT, endpoint.substring(0, index));
            properties.put(ENDPOINT_PORT, endpoint.substring(index + 1));
        } else {
            properties.put(ENDPOINT, endpoint);
        }

        properties.put(ACCESS_KEY, accessKey);
        properties.put(SECRET_KEY, secretKey);*/
        properties.put(CLUSTER_NAME, clusterName);
        properties.put(NAMING_LOAD_CACHE_AT_START, namingLoadCacheAtStart);

        return properties;
    }
}
