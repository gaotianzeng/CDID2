package com.ictr.android.cdid.base.utils;

import android.util.Log;


/**
 * 日志工具类
 * Created by maoxf on 2016/12/28.
 */

public class LogUtils {

    public static String LOG_TAG = "cdid_sdk";
    private static boolean isOpenLog;

    private LogUtils() {
    }

    /**
     * error
     *
     * @param TAG
     * @param logStr
     */
    public static void e(String TAG, String logStr) {
        if (isOpenLog) {
            if (logStr == null) {
                return;
            }
            Log.e(TAG, logStr);
        }
    }

    /**
     * info
     *
     * @param TAG
     * @param logStr
     */
    public static void i(String TAG, String logStr) {
        if (isOpenLog) {
            if (logStr == null) {
                return;
            }
            Log.i(TAG, logStr);
        }
    }

    /**
     * debug
     *
     * @param TAG
     * @param logStr
     */
    public static void d(String TAG, String logStr) {
        if (isOpenLog) {
            if (logStr == null) {
                return;
            }
            Log.d(TAG, logStr);
        }
    }

    /**
     * warn
     *
     * @param TAG
     * @param logStr
     */
    public static void w(String TAG, String logStr) {
        if (isOpenLog) {
            if (logStr == null) {
                return;
            }
            Log.w(TAG, logStr);
        }
    }


    /**
     * 设置是否打开日志
     *
     * @param isOpen
     */
    public static void setIsOpenLog(boolean isOpen) {
        isOpenLog = isOpen;
        if (isOpenLog) {
            Log.i(LOG_TAG, "open cdid log");
        } else {
            Log.i(LOG_TAG, "close cdid log");
        }
    }


}
