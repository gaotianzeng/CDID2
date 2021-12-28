package com.ictr.android.cdid.base.handler;

import android.os.Message;

/**
 * Handler 回调
 * Created by maoxf on 2018/1/22.
 */

public interface HandlerCallback {
    abstract void onCallback(Message msg);
}
