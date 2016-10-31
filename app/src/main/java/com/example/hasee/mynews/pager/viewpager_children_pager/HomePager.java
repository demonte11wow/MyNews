package com.example.hasee.mynews.pager.viewpager_children_pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.hasee.mynews.base.BasePager;
import com.example.hasee.mynews.utils.LogUtil;

/**
 * Created by lzq on 2016/10/17.
 */
public class HomePager extends BasePager {
    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {

        LogUtil.e("主页数据加载了....");
        tv_title.setText("主页");
        TextView textView = new TextView(context);
        textView.setText("主页面");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        fl_base_content.addView(textView);
    }
}
