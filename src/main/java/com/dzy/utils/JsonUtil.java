package com.dzy.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * json工具类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/10  13:16
 */
public class JsonUtil {

    /**
     * 将json字符串转为java对象
     *
     * @param json json字符串
     * @return java对象列表
     */
    public static List<String> convertJsonToList(String json) {
        //json字符串转java对象列表
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

}
