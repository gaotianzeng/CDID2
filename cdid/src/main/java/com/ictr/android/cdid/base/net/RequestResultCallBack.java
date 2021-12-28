package com.ictr.android.cdid.base.net;

/**
 * 请求结果回调
 * Created by maoxf on 2017/3/7.
 */

public abstract class RequestResultCallBack {

    public abstract void onSuccess(String result);

    public abstract void onError(int errorCode, String errorStr);

}
