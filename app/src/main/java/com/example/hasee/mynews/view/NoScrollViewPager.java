package com.example.hasee.mynews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lzq on 2016/10/17.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
