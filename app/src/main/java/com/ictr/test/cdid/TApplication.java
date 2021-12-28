package com.ictr.test.cdid;

import android.app.Application;
import android.content.Context;

import com.ictr.android.cdid.CDIDSdk;


/**
 * Created by maoxf on 2018/5/15.
 */
public class TApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CDIDSdk.initCDID(base);
    }
}
