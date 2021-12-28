package com.ictr.android.cdid.base.net;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 网络请求参数的数据类
 * Created by maoxf on 2017/3/7.
 */

public class RequestParams {

    public Map<String, Object> mapList;

    public RequestParams() {
        if (mapList == null) {
            mapList = new LinkedHashMap<>();
        } else {
            mapList.clear();
        }
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public synchronized void add(String key, Object value) {
        if (mapList != null) {
            mapList.put(key, value);
        }
    }

}
