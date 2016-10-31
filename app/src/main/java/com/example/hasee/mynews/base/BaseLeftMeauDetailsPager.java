package com.example.hasee.mynews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by lzq on 2016/10/17.
 */
public abstract class BaseLeftMeauDetailsPager {
    public final Context context;

    /**
     * 代表每个页面的视图
     */
    public View rootView;

    public BaseLeftMeauDetailsPager(Context context){
        this.context = context;//接受上下文要放在第一行代码里
        rootView  = initView();

    }

    /**
     * 强制子类实现该方法，实现自己特有的ui效果
     */
    public abstract View initView();

    /**
     * 由子类重写该方法，子视图和FrameLayout结合成一个页面；绑定数据
     */
    public void initData(){

    }

}
