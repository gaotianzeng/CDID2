package com.ictr.android.cdid.impl;

import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ictr.android.cdid.base.handler.HandlerCallback;
import com.ictr.android.cdid.base.handler.MHandler;
import com.ictr.android.cdid.base.net.HttpRequest;
import com.ictr.android.cdid.base.net.RequestResultCallBack;
import com.ictr.android.cdid.base.utils.DeviceUtil;
import com.ictr.android.cdid.base.utils.LogUtils;
import com.ictr.android.cdid.base.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * CDID 实现类
 *
 * @author maoxf
 * @date 2018/5/15
 */
public class CDIDImpl extends CDID implements ICDID {
//    private final String CDID_POST_INTERFACE_MOTHOD = "/cdid/saveANRegister";
    private final String CDID_POST_INTERFACE_MOTHOD = "/CTRIDDemo/services/cdid/saveANRegister";
    private final int NET_ERROR = -1001;
    private final int WHAT_SUCCESS = 100;
    private final int WHAT_ERROR = 101;
    private String serviceUrl;
    private CDIDCallback callback;
    private MHandler mHandler = new MHandler(new HandlerCallback() {
        @Override
        public void onCallback(Message msg) {
            switch (msg.what) {
                case WHAT_SUCCESS:
                    String cdid = (String) msg.obj;
                    if (callback != null) {
                        callback.onSuccess(cdid);
                    }
                    break;
                case WHAT_ERROR:
                    int code = (int) msg.obj;
                    if (callback != null) {
                        callback.onError(code);
                    }
                    break;
                default:
                    break;
            }
        }
    });

    public CDIDImpl(@NonNull Context context) {
        super(context.getApplicationContext());
        setCDIDParams();
    }



    public void setCDIDServices(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }


    @Override
    public void setCDIDParams() {
        setCdId(CDIDParamTools.getSpCDID(getContext()));
        setImei1(DeviceUtil.getIMEI1(getContext()));
        setImei2(DeviceUtil.getIMEI2(getContext()));
        setSerial(DeviceUtil.getSerial());
        setAndroidId(DeviceUtil.getAndroidId(getContext()));
        setUuid(CDIDParamTools.getUUID(getContext()));
        //因天增测试wlan、移动网状态下，去获取wlan mac会有不同情况。则先判断当前有网且是移动网时传 移动mac；否则传wlan mac
        if (NetUtil.isNetworkAvailable(getContext()) && !NetUtil.isWiFiState(getContext())) {
            setMac1("");
            setMac2(DeviceUtil.getMacForIP());
        } else {
            setMac1(DeviceUtil.getWlanMac());
            setMac2("");
        }
        setOaid(CDIDParamTools.getSpOAIDCurrent(getContext()));

    }

    /**
     * check cdid params whether to change
     */
    public boolean checkCDIDParams() {
        boolean isChange = false;
        if (TextUtils.isEmpty(getCdId())) {
            LogUtils.i(LogUtils.LOG_TAG, "cdid is empty");
            isChange = true;
        } else {
            LogUtils.i(LogUtils.LOG_TAG, "cdid:" + getCdId());
        }
        if (!TextUtils.isEmpty(getImei1()) && !CDIDParamTools.getSpIMEI1(getContext()).equals(getImei1())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "imei1 is changed");
        } else {
            setImei1(CDIDParamTools.getSpIMEI1(getContext()));
        }
        if (!TextUtils.isEmpty(getImei2()) && !CDIDParamTools.getSpIMEI2(getContext()).equals(getImei2())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "imei2 is changed");
        } else {
            setImei2(CDIDParamTools.getSpIMEI2(getContext()));
        }
        if (!TextUtils.isEmpty(getAndroidId()) && !CDIDParamTools.getSpAndroidid(getContext()).equals(getAndroidId())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "androidId is changed");
        } else {
            setAndroidId(CDIDParamTools.getSpAndroidid(getContext()));
        }
        if (!TextUtils.isEmpty(getSerial()) && !CDIDParamTools.getSpSerial(getContext()).equals(getSerial())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "serial is changed");
        } else {
            setSerial(CDIDParamTools.getSpSerial(getContext()));
        }
        if (!TextUtils.isEmpty(getMac1()) && !CDIDParamTools.getSpMac1(getContext()).equals(getMac1())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "mac1 is changed");
        } else {
            setMac1(CDIDParamTools.getSpMac1(getContext()));
        }
        if (!TextUtils.isEmpty(getMac2()) && !CDIDParamTools.getSpMac2(getContext()).equals(getMac2())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "mac2 is changed");
        } else {
            setMac2(CDIDParamTools.getSpMac2(getContext()));
        }
        if (!TextUtils.isEmpty(getOaid()) && !CDIDParamTools.getSpOAID(getContext()).equals(getOaid())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "oaid is changed");
        } else {
            setOaid(CDIDParamTools.getSpOAID(getContext()));
        }
        if (!TextUtils.isEmpty(getUuid()) && !CDIDParamTools.getSpUUID(getContext()).equals(getUuid())) {
            isChange = true;
            LogUtils.i(LogUtils.LOG_TAG, "uuid is changed");
        } else {
            setUuid(CDIDParamTools.getSpUUID(getContext()));
        }
        return isChange;
    }

    public void setCallback(CDIDCallback callback) {
        this.callback = callback;
    }

    @Override
    public void requestCDID() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cdId", getCdId());
                    jsonObject.put("imei1", getImei1());
                    jsonObject.put("imei2", getImei2());
                    jsonObject.put("serial", getSerial());
                    jsonObject.put("androidId", getAndroidId());
                    jsonObject.put("mac1", getMac1());   // 无线MAC地址
                    jsonObject.put("mac2", getMac2());  // 移动网MAC地址
                    jsonObject.put("release", Build.VERSION.RELEASE);
                    jsonObject.put("oaid", getOaid());  // OAID
                    jsonObject.put("uuid", getUuid());  // UUID
                } catch (JSONException e) {
                    LogUtils.e(LogUtils.LOG_TAG, e.toString());
                }
                LogUtils.i(LogUtils.LOG_TAG, "submit json:" + jsonObject.toString());
                HttpRequest.postForJson(serviceUrl + CDID_POST_INTERFACE_MOTHOD, jsonObject, new RequestResultCallBack() {
                    @Override
                    public void onSuccess(String resultJson) {
                        LogUtils.i(LogUtils.LOG_TAG, "submit success:" + resultJson);
                        try {
                            JSONObject jsonObject = new JSONObject(resultJson);
                            int result = jsonObject.getInt("result");
                            switch (result) {
                                case 100:
                                case 101:
                                case 102:
                                case 103:
                                    String cdId = jsonObject.getString("cdId");
                                    setCdId(cdId);
                                    CDIDParamTools.saveSpCDID(getContext(), getCdId());
                                    CDIDParamTools.saveSpIMEI1(getContext(), getImei1());
                                    CDIDParamTools.saveSpIMEI2(getContext(), getImei2());
                                    CDIDParamTools.saveSpSerial(getContext(), getSerial());
                                    CDIDParamTools.saveSpAndroidid(getContext(), getAndroidId());
                                    CDIDParamTools.saveSpMac1(getContext(), getMac1());
                                    CDIDParamTools.saveSpMac2(getContext(), getMac2());
                                    CDIDParamTools.saveSpOAID(getContext(), getOaid());
                                    CDIDParamTools.saveSpUUID(getContext(), getUuid());
                                    Message.obtain(mHandler, WHAT_SUCCESS, cdId).sendToTarget();
                                    break;
                                default:
                                    Message.obtain(mHandler, WHAT_ERROR, result).sendToTarget();
                                    break;
                            }
                        } catch (JSONException e) {
                            LogUtils.e(LogUtils.LOG_TAG, "parse result json error：" + e.toString());
                            Message.obtain(mHandler, WHAT_ERROR, NET_ERROR).sendToTarget();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorStr) {
                        LogUtils.e(LogUtils.LOG_TAG, "error:" + "(" + errorCode + ")" + errorStr);
                        Message.obtain(mHandler, WHAT_ERROR, NET_ERROR).sendToTarget();
                    }
                });
            }
        }).start();

    }
}
