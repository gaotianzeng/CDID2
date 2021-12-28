package com.ictr.android.cdid.base.net;

import android.support.annotation.NonNull;

import com.ictr.android.cdid.base.utils.LogUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Http请求
 * Created by maoxf on 2017/3/7.
 */

public class HttpRequest {

    /**
     * -1 标识URL链接错误
     */
    private static int errorCode1 = -1;
    /**
     * -2 标识网络连接失败
     */
    private static int errorCode2 = -2;

    /**
     * -3 没有请求参数异常
     */
    private static int errorCode3 = -3;
    /**
     * -4 网络连接异常
     */
    private static int errorCode4 = -4;


    /**
     * 带参数的POST请求
     *
     * @param urlStr         请求链接
     * @param resultCallBack 回调
     */
    public static void postParams(@NonNull String urlStr, @NonNull RequestParams params, @NonNull RequestResultCallBack resultCallBack) {

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);//允许对外传输数据
            //设置请求属性
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "UTF-8");

            //添加参数
            StringBuffer stringBuffer;
            Map<String, Object> mapList = params.mapList;
            if (mapList != null && mapList.size() > 0) {
                stringBuffer = new StringBuffer();
                for (Map.Entry<String, Object> enty : mapList.entrySet()) {
                    try {
                        stringBuffer.append(enty.getKey()).append("=").append(enty.getValue()).append("&");
                    } catch (Exception e) {
                        resultCallBack.onError(errorCode3, e.toString());
                        return;
                    }
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            } else {
                resultCallBack.onError(errorCode3, "WTRequestParams can't be empty");
                return;
            }

            //建立输入流，向指向的URL传入参数
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(stringBuffer.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            //获得响应状态
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                StringBuilder sb = new StringBuilder();
                String readLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = bufferedReader.readLine()) != null) {
                    sb.append(readLine);
                }
                bufferedReader.close();
                resultCallBack.onSuccess(sb.toString());
            } else {
                resultCallBack.onError(responseCode, "连接异常");
            }
        } catch (IOException e) {
            resultCallBack.onError(errorCode2, e.getMessage());
        }


    }

    /**
     * 不带参数的POST请求
     *
     * @param urlStr         请求链接
     * @param resultCallBack 回调
     */
    public static void postNoParams(@NonNull String urlStr, @NonNull RequestResultCallBack resultCallBack) {

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(false);//允许对外传输数据
            //设置请求属性
            connection.setRequestProperty("Content-Type", "application/form-data");
            connection.setRequestProperty("Charset", "UTF-8");

            int responseCode = connection.getResponseCode();
            //获得响应状态
            if (HttpURLConnection.HTTP_OK == responseCode) {
                StringBuilder sb = new StringBuilder();
                String readLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = bufferedReader.readLine()) != null) {
                    sb.append(readLine);
                }
                bufferedReader.close();
                resultCallBack.onSuccess(sb.toString());
            } else {
                resultCallBack.onError(responseCode, "连接异常");
            }
        } catch (Exception e) {
            resultCallBack.onError(errorCode2, e.getMessage());
        }


    }

    /**
     * 带Json字符串的POST请求
     *
     * @param urlStr         请求链接
     * @param jsonObject     请求参数
     * @param resultCallBack 回调
     */
    public static void postForJson(@NonNull String urlStr, @NonNull JSONObject jsonObject, @NonNull RequestResultCallBack resultCallBack) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);//允许对外传输数据
            //设置请求属性
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "application/json");//内容类型为json
//            connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            connection.setRequestProperty("Charset", "UTF-8");
            //建立输入流，向指向的URL传入参数
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            //获得响应状态
            if (HttpURLConnection.HTTP_OK == responseCode) {
                StringBuilder sb = new StringBuilder();
                String readLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = bufferedReader.readLine()) != null) {
                    sb.append(readLine);
                }
                bufferedReader.close();
                resultCallBack.onSuccess(sb.toString());
            } else {
                resultCallBack.onError(errorCode4, "net error");
            }
        } catch (Exception e) {
            resultCallBack.onError(errorCode4, "net error");
        }
    }

    /**
     * 带参数的Get请求
     *
     * @param urlStr         请求链接
     * @param params         请求参数
     * @param resultCallBack 回调
     */
    public static void getForParams(@NonNull String urlStr, @NonNull RequestParams params, @NonNull RequestResultCallBack resultCallBack) {

        //添加参数
        StringBuilder urlStrGet = new StringBuilder();
        urlStrGet.append(urlStr).append("?");
        Map<String, Object> mapList = params.mapList;
        if (mapList != null && mapList.size() > 0) {
            for (Map.Entry<String, Object> enty : mapList.entrySet()) {
                try {
                    urlStrGet.append(enty.getKey()).append("=").append(URLEncoder.encode(enty.getValue() + "", "utf-8"));
                    urlStrGet.append("&");
                } catch (UnsupportedEncodingException e) {
                    resultCallBack.onError(errorCode1, e.getMessage());
                }
            }
            urlStrGet.deleteCharAt(urlStrGet.length() - 1);
        } else {
            resultCallBack.onError(errorCode3, "TMRequestParams can't be empty");
            return;
        }

        LogUtils.i(LogUtils.LOG_TAG, urlStrGet.toString());
        try {
            URL url = new URL(urlStrGet.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Charset", "UTF-8");
            //建立输入流，向指向的URL传入参数
//            connection.connect();
            int responseCode = connection.getResponseCode();
            //获得响应状态
            if (HttpURLConnection.HTTP_OK == responseCode) {
                StringBuilder sb = new StringBuilder();
                String readLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = bufferedReader.readLine()) != null) {
                    sb.append(readLine);
                }
                bufferedReader.close();
                resultCallBack.onSuccess(sb.toString());
            } else {
                resultCallBack.onError(responseCode, "连接异常");
            }
        } catch (Exception e) {
            resultCallBack.onError(errorCode2, e.getMessage());
        }
    }


    /**
     * 不带参数的GET请求
     *
     * @param urlStr         请求链接
     * @param resultCallBack 回调
     */
    public static void getNoParams(@NonNull String urlStr, @NonNull RequestResultCallBack resultCallBack) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Charset", "UTF-8");
            int responseCode = connection.getResponseCode();
            //获得响应状态
            if (HttpURLConnection.HTTP_OK == responseCode) {
                StringBuilder sb = new StringBuilder();
                String readLine;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = bufferedReader.readLine()) != null) {
                    sb.append(readLine);
                }
                bufferedReader.close();
                resultCallBack.onSuccess(sb.toString());
            } else {
                resultCallBack.onError(responseCode, "连接异常");
            }
        } catch (Exception e) {
            resultCallBack.onError(errorCode2, e.getMessage());
        }


    }

}
