package com.ictr.android.cdid;

import android.content.Context;
import android.support.annotation.NonNull;


import com.ictr.android.cdid.base.utils.DeviceUtil;
import com.ictr.android.cdid.impl.CDIDCallback;
import com.ictr.android.cdid.impl.CDIDImpl;
import com.ictr.android.cdid.impl.CDIDParamTools;


/**
 * CDID SDK control
 * Created by maoxf on 2018/5/9.
 */
public class CDIDSdk {


    public CDIDSdk() {
    }

    /**
     * if sp no exist CDID, request CDID from net
     *
     * @param context
     * @param serviceUrl 对应项目对应的服务器地址
     * @param callback
     */
    public static void getCDID(@NonNull Context context, @NonNull final String serviceUrl, final CDIDCallback callback) {
        final CDIDImpl cdidImpl = new CDIDImpl(context);
        if (cdidImpl.checkCDIDParams()) {
            cdidImpl.setCDIDServices(serviceUrl);
            cdidImpl.setCallback(callback);
            cdidImpl.requestCDID();
        } else {
            callback.onSuccess(cdidImpl.getCdId());
        }
    }

    /**
     * 初始化，在应用的application的attachBaseContext或onCreate中调用
     * 建议在attachBaseContext中
     *
     * @param context
     */
    public static void initCDID(final Context context) {
        //JLibrary.InitEntry(context);
        DeviceUtil.getOAID(context);
    }

    /**
     * 获得OAID
     */
    public static String getOAID(Context context) {
        return CDIDParamTools.getSpOAIDCurrent(context);
    }

}
