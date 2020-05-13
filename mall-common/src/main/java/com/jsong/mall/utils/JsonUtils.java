package com.jsong.mall.utils;


import com.google.gson.Gson;

/**
 * 2020/5/13 16:47
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public class JsonUtils {

    private static Gson gson = new Gson();

    public static String write(Object o){
        return gson.toJson(o);
    }

    public static <T> T from(String json, Class<T> clazz){
        return gson.fromJson(json,clazz);
    }
}
