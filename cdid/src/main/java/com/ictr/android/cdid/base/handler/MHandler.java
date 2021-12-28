package com.ictr.android.cdid.base.handler;

import android.os.Handler;
import android.os.Message;

/**
 * Handler
 * Created by maoxf on 2018/1/22.
 */

public class MHandler extends Handler {

    private HandlerCallback mCallBack;

    public MHandler(HandlerCallback callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mCallBack.onCallback(msg);
    }
}
