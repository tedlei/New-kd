package com.sam.scaner.utils;

import com.google.gson.Gson;


public class JSON {


    // 将对象转换成json字符串
    public static String toJSONString(Object obj){

        return new Gson().toJson(obj);
    }

    // 将json数据转换成pojo对象
    public static <T> T parseObject(String json,Class<T> beanType){
        return new Gson().fromJson(json,beanType);
    }



}
