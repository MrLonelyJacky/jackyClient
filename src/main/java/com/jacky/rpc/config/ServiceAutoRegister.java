package com.jacky.rpc.config;

import com.jacky.rpc.listener.ApplicationMulticaster;
import com.jacky.rpc.listener.StatusModifyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: 启动时 发布注册事件 这里没有使用
 * @author: jacky
 * @create: 2023-02-18 15:19
 **/
public class ServiceAutoRegister implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAutoRegister.class);
    private AtomicBoolean running = new AtomicBoolean(false);

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public void start() {
        //发布注册事件
        ApplicationMulticaster.publishEvent(new StatusModifyEvent());
        running.set(true);
    }

    @Override
    public void stop() {
        logger.info("autoService stop......");
        running.set(false);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public int getPhase() {
        return 0;
    }




}
