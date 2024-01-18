package com.jacky.rpc.client;

import com.jacky.rpc.task.CacheRefreshTask;
import com.jacky.rpc.task.HeartBeatTask;

import java.util.concurrent.*;

/**
 * @description: 客户端启动成功时 完成注册、心跳检测、刷新注册列表功能
 * @author: jacky
 * @create: 2023-02-18 11:20
 **/
public class DiscoveryClient {
    private  ScheduledExecutorService scheduler;

    /**
     * heartBeat
     */
    private ThreadPoolExecutor heartbeatExecutor;
    private ThreadPoolExecutor cacheRefreshExecutor;



    public DiscoveryClient() {

        /**
         * 生成一个事件
         */

        scheduler = Executors.newScheduledThreadPool(2);

        heartbeatExecutor = new ThreadPoolExecutor(1,2,0,TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        cacheRefreshExecutor = new ThreadPoolExecutor(1,2,0,TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        scheduler.schedule(new HeartBeatTask(),30, TimeUnit.SECONDS);
        scheduler.schedule(new CacheRefreshTask(cacheRefreshExecutor),30, TimeUnit.SECONDS);

    }
}
