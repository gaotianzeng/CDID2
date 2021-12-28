package com.ictr.android.cdid.config;

import com.ictr.android.cdid.base.utils.LogUtils;

/**
 * cdid base config
 * Created by maoxf on 2018/5/16.
 */
public class CDIDConfig {

    private CDIDConfig() {
    }

    /**
     * set log open state,call in activity onCreate() or application onCreate();
     */
    public static void isOpenLog(boolean isOpenLog) {
        LogUtils.setIsOpenLog(isOpenLog);
    }

}
