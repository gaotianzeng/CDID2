package com.ictr.android.cdid.impl;

/**
 * Created by maoxf on 2018/5/15.
 */
public interface CDIDCallback {
    void onSuccess(String cdid);

    void onError(int errorCode);

}
