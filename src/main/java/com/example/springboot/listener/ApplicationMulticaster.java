package com.example.springboot.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 广播器
 * @author: jacky
 * @create: 2023-02-18 14:39
 **/
public class ApplicationMulticaster {


    private static List<StatusModifyListener> modifyListeners = new ArrayList<>();


    public static  void addStatusModifyListener(StatusModifyListener statusModifyListener){
        modifyListeners.add(statusModifyListener);
    }

    public static void publishEvent(StatusModifyEvent modifyEvent){
        modifyListeners.forEach(item->item.notify(modifyEvent));
    }
}
