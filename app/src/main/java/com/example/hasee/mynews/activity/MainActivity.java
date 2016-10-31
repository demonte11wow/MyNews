package com.example.hasee.mynews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.fragment.ContentFragment;
import com.example.hasee.mynews.fragment.LeftMenuFragment;
import com.example.hasee.mynews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String LEFTMENU_TAG = "leftmenu_tag";
    private static final String CONTENT_TAG = "content_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);//framlayout

        setBehindContentView(R.layout.leftmenu);//framlayout

        SlidingMenu slidingMenu = getSlidingMenu();

        //4.设置支持滑动的模式：全屏滑动，边缘滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE );
        //5.设置页面模式：左侧菜单+主页面；左侧菜单+主页面+右侧菜单； 主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        //6.设置主页面占的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_leftmenu,new LeftMenuFragment(),LEFTMENU_TAG);
        transaction.replace(R.id.fl_main_content,new ContentFragment(),CONTENT_TAG);
        //4.提交事务
        transaction.commit();
    }
    public LeftMenuFragment getLeftMenuFragment(){
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }
    public ContentFragment getContantFragment(){
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONTENT_TAG);
    }
}
