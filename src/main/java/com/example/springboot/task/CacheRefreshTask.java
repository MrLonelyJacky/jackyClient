package com.example.springboot.task;

import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 心跳
 * @author: jacky
 * @create: 2023-02-20 17:56
 **/
public class CacheRefreshTask extends TimerTask {
    private ThreadPoolExecutor cacheRefreshExecutor;

    public CacheRefreshTask(ThreadPoolExecutor cacheRefreshExecutor) {
        this.cacheRefreshExecutor = cacheRefreshExecutor;
    }

    @Override
    public void run() {
        cacheRefreshExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
