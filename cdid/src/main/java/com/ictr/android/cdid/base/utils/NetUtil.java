package com.ictr.android.cdid.base.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断网络连接状态的工具类 wifi GPS 3G 4G
 *
 * @author maoxiaofeng
 * @date 2016-07-27
 */
public final class NetUtil {

    /**
     * 判断网络连接是否打开,包括移动数据连接
     *
     * @param context 上下文
     * @return 是否联网
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }

    /**
     * GPS是否打开
     *
     * @param context 上下文
     * @return Gps是否可用
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 检测当前打开的网络类型是否WIFI
     *
     * @param context 上下文
     * @return 是否是Wifi上网
     */
    public static boolean isWiFiState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
            return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * 只是判断WIFI
     *
     * @param context 上下文
     * @return 是否打开Wifi
     */
    public static boolean isWiFiOpen(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            return wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING;
        }
        return false;
    }

    /**
     * 检测当前打开的网络类型是否3G
     *
     * @param context 上下文
     * @return 是否是3G上网
     */
    public static boolean is3G(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
            return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    /**
     * 检测当前开打的网络类型是否4G
     *
     * @param context 上下文
     * @return 是否是4G上网
     */
    public static boolean is4G(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting()) {
                return activeNetInfo.getType() == TelephonyManager.NETWORK_TYPE_LTE;
            }
        }

        return false;
    }


    /**
     * IP地址校验
     *
     * @param ip 待校验是否是IP地址的字符串
     * @return 是否是IP地址
     */
    public static boolean isIP(String ip) {
        Pattern pattern = Pattern
                .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * IP转化成int数字
     *
     * @param addr IP地址
     * @return Integer
     */
    public static int ipToInt(String addr) {
        String[] addrArray = addr.split("\\.");
        int num = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math
                    .pow(256, power)));
        }
        return num;
    }

    /**
     * 枚举网络状态 NET_NO：没有网络 NET_2G:2g网络 NET_3G：3g网络 NET_4G：4g网络 NET_WIFI：wifi
     * NET_UNKNOWN：未知网络
     */
    public enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    /**
     * 判断当前是否网络连接
     *
     * @param context 上下文
     * @return 状态码
     */
    public static NetState isConnected(@NonNull Context context) {
        NetState stateCode = NetState.NET_NO;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo ni = manager.getActiveNetworkInfo();
            if (ni != null && ni.isConnectedOrConnecting()) {
                switch (ni.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        stateCode = NetState.NET_WIFI;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        switch (ni.getSubtype()) {
                            case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                stateCode = NetState.NET_2G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                stateCode = NetState.NET_3G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                stateCode = NetState.NET_4G;
                                break;
                            default:
                                stateCode = NetState.NET_UNKNOWN;
                        }
                        break;
                    default:
                        stateCode = NetState.NET_UNKNOWN;
                }

            }
        }

        return stateCode;
    }

    /**
     * 获取URL中参数 并返回Map
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(@NonNull String url) {
        Map<String, String> map = null;
        if (url.contains("&") && url.contains("=")) {
            map = new HashMap<String, String>();
            String[] arrTemp = url.split("&");
            for (String str : arrTemp) {
                String[] qs = str.split("=");
                map.put(qs[0], qs[1]);
            }
        }
        return map;
    }

}
