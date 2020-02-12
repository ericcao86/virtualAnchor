package com.iflytek.tps.service.util;

import java.util.concurrent.ConcurrentHashMap;

public class CommonMap {

    private static ConcurrentHashMap taskMap = new ConcurrentHashMap();

    public static ConcurrentHashMap getTaskMap() {
        return taskMap;
    }

    public static void setTaskMap(ConcurrentHashMap taskMap) {
        CommonMap.taskMap = taskMap;
    }
}
