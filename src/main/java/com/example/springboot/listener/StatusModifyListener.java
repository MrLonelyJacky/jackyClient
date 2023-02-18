package com.example.springboot.listener;

import com.netflix.discovery.StatusChangeEvent;

/**
 * @description: 状态更新监听器
 * @author: jacky
 * @create: 2023-02-18 14:26
 **/
public interface StatusModifyListener {

    String getId();

    void notify(StatusModifyEvent statusChangeEvent);
}
