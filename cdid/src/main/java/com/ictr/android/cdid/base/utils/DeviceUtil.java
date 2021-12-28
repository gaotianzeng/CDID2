package com.ictr.android.cdid.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.ictr.android.cdid.MiitHelper;
import com.ictr.android.cdid.impl.CDIDParamTools;
import com.ictr.android.cdid.impl.OAIDCallback;

import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 设备信息工具类
 * Created by maoxf on 2017/01/04.
 */
public class DeviceUtil {

    private static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";

    private DeviceUtil() {
    }

    /**
     * brand
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * model
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * manufacture
     *
     * @return
     */
    public static String getManufacture() {
        return Build.MANUFACTURER;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * sdk int code
     *
     * @return
     */
    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * system version
     *
     * @return
     */
    public static String getSysVersionName() {
        return Build.VERSION.RELEASE;
    }


    /**
     * serial
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getSerial() {
        String serial = Build.SERIAL;
        if (TextUtils.isEmpty(serial)){
            return "";
        }
        if ("unknown".equals(serial.toLowerCase())) {
            return "";
        }
        return serial;
    }

    /**
     * androidID
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * get mac way for net interface
     *
     * @return
     */
    public static String getWlanMac() {
        String mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                mac = convertByteToMac(macBytes);
                if (DEFAULT_MAC_ADDRESS.equals(mac)) {
                    return "";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mac;
    }


    /**
     * get ip
     *
     * @return
     */
    private static String getIP() {
        String hostIp = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.e(LogUtils.LOG_TAG, "getIP--SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }

    /**
     * get mac way for IP
     *
     * @return
     */
    public static String getMacForIP() {
        String mac = "";
        try {
            byte[] b = NetworkInterface.getByInetAddress(InetAddress.getByName(getIP())).getHardwareAddress();
            mac = convertByteToMac(b);
            if (DEFAULT_MAC_ADDRESS.equals(mac)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    /**
     * convert mac byte to mac string
     *
     * @param macByte
     * @return
     */
    private static String convertByteToMac(byte[] macByte) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macByte.length; i++) {
            byte b = macByte[i];
            int value = 0;
            if (b >= 0 && b <= 16) {
                value = b;
                sb.append("0").append(Integer.toHexString(value));
            } else if (b > 16) {
                value = b;
                sb.append(Integer.toHexString(value));
            } else {
                value = 256 + b;
                sb.append(Integer.toHexString(value));
            }
            if (i != macByte.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }


    public static String getIMEI1(Context mContext) {
        String imei1 = "";
        try {
            imei1 = getOperatorBySlot(mContext, "getDeviceIdGemini", 0);
        } catch (GeminiMethodNotFoundException e) {
            try {
                imei1 = getOperatorBySlot(mContext, "getDeviceId", 0);
            } catch (GeminiMethodNotFoundException e1) {
                LogUtils.e(LogUtils.LOG_TAG, "get imei1 null,please check 'read phone state permission'");
            }
        }
        return imei1;
    }

    public static String getIMEI2(Context mContext) {
        String imei2 = "";
        try {
            imei2 = getOperatorBySlot(mContext, "getDeviceIdGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            try {
                imei2 = getOperatorBySlot(mContext, "getDeviceId", 1);
            } catch (GeminiMethodNotFoundException e1) {
                LogUtils.e(LogUtils.LOG_TAG, "get imei2 null,please check 'read phone state permission'");
            }
        }
        return imei2;
    }


    private static String getOperatorBySlot(Context context, String predictedMethodName, int slotID)
            throws GeminiMethodNotFoundException {
        String inumeric = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
        return inumeric;
    }

    /**
     * 获得OAID
     */
    public static void getOAID(final Context context) {
        MiitHelper miitHelper = new MiitHelper(new MiitHelper.OnUpdateOaidListener() {
            @Override
            public void OnIdsAvalid(@NonNull String ids) {
                if (!TextUtils.isEmpty(ids)){
                    CDIDParamTools.saveSpOAIDCurrent(context,ids);
                }
            }
        });
        miitHelper.getDeviceIds(context);
    }
    public static class GeminiMethodNotFoundException extends Exception {

        private static final long serialVersionUID = -3241033488141442594L;

        GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }

}
