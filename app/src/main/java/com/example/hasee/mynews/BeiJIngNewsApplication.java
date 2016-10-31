package com.example.hasee.mynews;

import android.app.Application;

import com.example.hasee.mynews.volley.VolleyManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lzq on 2016/10/16.
 */
public class BeiJIngNewsApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        VolleyManager.init(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
