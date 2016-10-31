package com.example.hasee.mynews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lzq on 2016/10/15.
 */
public class CacheUtils {
    /**
     * 缓存文本数据
     *
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context,String key,String values){
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putString(key,values).commit();
    }
    /**
     * 得到缓存文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getString(key,"");

    }
    /**
     * 保存boolean类型
     * @param context
     * @param key
     * @param values
     */
    public static void putBoolean(Context context,String key,Boolean values){
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,values).commit();
    }
    /**
     * 得到保持的boolean类型
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key){
     SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        LogUtil.e(sp.getBoolean(key,false)+"");
        return sp.getBoolean(key,false);

    }

    public static void putInt(Context context,String key,int values){
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putInt(key,values).commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        LogUtil.e(sp.getInt(key,2)+"");
        return sp.getInt(key,2);
    }
}
