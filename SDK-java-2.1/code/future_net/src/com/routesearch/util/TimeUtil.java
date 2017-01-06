package com.routesearch.util;

/**
 * Created by liuhang on 2017/1/5.
 * 获取时间工具类
 */
public class TimeUtil {
    private static long start = System.currentTimeMillis();//以毫秒为单位,返回当前时间
    private static long last;

    //更新时间,打下时间戳
    public static void updateTime() {
        last = System.currentTimeMillis();
    }

    //返回距离上次更新时间的时间差,ms单位
    public static long getTimeDelay() {
        long nowTime = System.currentTimeMillis();
        return nowTime - last;
    }

    //返回距离整个算法开始的时间
    public static long getUsedTime() {
        long nowTime = System.currentTimeMillis();
        return nowTime - start;
    }

}
