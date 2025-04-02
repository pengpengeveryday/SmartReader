package com.peng.un.utils;

import android.util.Log;

public class ALog {
    private static final String TAG = "SmartReader";

    /**
     * 打印调试级别的日志
     * @param msg 日志信息
     */
    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    /**
     * 打印错误级别的日志
     * @param msg 日志信息
     */
    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    /**
     * 打印信息级别的日志
     * @param msg 日志信息
     */
    public static void i(String msg) {
        Log.i(TAG, msg);
    }
}
