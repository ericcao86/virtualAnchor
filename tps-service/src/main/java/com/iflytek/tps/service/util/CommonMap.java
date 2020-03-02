package com.iflytek.tps.service.util;

import java.util.concurrent.ConcurrentHashMap;

public class CommonMap {

    private static ConcurrentHashMap taskMap = new ConcurrentHashMap();
    private static ConcurrentHashMap taskIdMap = new ConcurrentHashMap();

    public static ConcurrentHashMap getTaskMap() {
        return taskMap;
    }

    public static void setTaskMap(ConcurrentHashMap taskMap) {
        CommonMap.taskMap = taskMap;
    }

    public static ConcurrentHashMap getTaskIdMap() {
        return taskIdMap;
    }

    public static void setTaskIdMap(ConcurrentHashMap taskIdMap) {
        CommonMap.taskIdMap = taskIdMap;
    }
}
