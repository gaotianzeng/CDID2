package com.ictr.android.cdid.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ictr.android.cdid.base.utils.SpUtils;

import java.util.UUID;

/**
 * cdid params sp save tools
 * Created by maoxf on 2018/6/13.
 */
public class CDIDParamTools {

    private static final String SP_NAME_CID = "sp_name_cid";
    private static final String SP_KEY_CDID = "key_cdid";
    private static final String SP_KEY_IMEI1 = "key_imei1";
    private static final String SP_KEY_IMEI2 = "key_imei2";
    private static final String SP_KEY_SERIAL = "key_serial";
    private static final String SP_KEY_ANDROIDID = "key_androidId";
    private static final String SP_KEY_MAC1 = "key_mac1";
    private static final String SP_KEY_MAC2 = "key_mac2";
    private static final String SP_KEY_OAID = "key_oaid";
    private static final String SP_KEY_OAID_CURRENT = "key_oaid_current";
    private static final String SP_KEY_UUID = "key_uuid";
    private CDIDParamTools() {
    }

    protected static String getSpUUID(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_UUID, "");
    }

    /**
     * 从SP中获取UUID，如果不存在的话，则重新生成一个
     */
    protected static String getUUID(@NonNull Context context) {
        String spUUID = getSpUUID(context);
        if (TextUtils.isEmpty(spUUID)) {
            spUUID = UUID.randomUUID().toString();
        }
        return spUUID;
    }
    protected static void saveSpUUID(@NonNull Context context, String uuid) {
        if (!TextUtils.isEmpty(uuid)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_UUID, uuid);
        }
    }

    protected static String getSpCDID(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_CDID, "");
    }

    protected static void saveSpCDID(@NonNull Context context, String cdid) {
        if (!TextUtils.isEmpty(cdid)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_CDID, cdid);
        }
    }

    protected static String getSpIMEI1(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_IMEI1, "");
    }

    protected static void saveSpIMEI1(@NonNull Context context, String imei1) {
        if (!TextUtils.isEmpty(imei1)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_IMEI1, imei1);
        }
    }

    protected static String getSpIMEI2(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_IMEI2, "");
    }

    protected static void saveSpIMEI2(@NonNull Context context, String imei2) {
        if (!TextUtils.isEmpty(imei2)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_IMEI2, imei2);
        }
    }

    protected static String getSpSerial(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_SERIAL, "");
    }

    protected static void saveSpSerial(@NonNull Context context, String serial) {
        if (!TextUtils.isEmpty(serial)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_SERIAL, serial);
        }
    }

    protected static String getSpAndroidid(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_ANDROIDID, "");
    }

    protected static void saveSpAndroidid(@NonNull Context context, String androidid) {
        if (!TextUtils.isEmpty(androidid)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_ANDROIDID, androidid);
        }
    }

    protected static String getSpMac1(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_MAC1, "");
    }

    protected static void saveSpMac1(@NonNull Context context, String mac1) {
        if (!TextUtils.isEmpty(mac1)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_MAC1, mac1);
        }
    }

    protected static String getSpOAID(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_OAID, "");
    }

    protected static void saveSpOAID(@NonNull Context context, String oaid) {
        if (!TextUtils.isEmpty(oaid)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_OAID, oaid);
        }
    }
    public static String getSpOAIDCurrent(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_OAID_CURRENT, "");
    }

    public static void saveSpOAIDCurrent(@NonNull Context context, String oaid) {
        if (!TextUtils.isEmpty(oaid)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_OAID_CURRENT, oaid);
        }
    }
    protected static String getSpMac2(@NonNull Context context) {
        return SpUtils.getString(context, SP_NAME_CID, SP_KEY_MAC2, "");
    }

    protected static void saveSpMac2(@NonNull Context context, String mac2) {
        if (!TextUtils.isEmpty(mac2)) {
            SpUtils.saveString(context, SP_NAME_CID, SP_KEY_MAC2, mac2);
        }
    }


}
