package com.example.hasee.mynews.fragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.activity.MainActivity;
import com.example.hasee.mynews.adapter.MyBasePagerAdapter;
import com.example.hasee.mynews.base.BaseFragment;
import com.example.hasee.mynews.base.BasePager;
import com.example.hasee.mynews.pager.viewpager_children_pager.HomePager;
import com.example.hasee.mynews.pager.viewpager_children_pager.NewsCenterPager;
import com.example.hasee.mynews.pager.viewpager_children_pager.SetingsPager;
import com.example.hasee.mynews.pager.viewpager_children_pager.ShoppingCartPager;
import com.example.hasee.mynews.pager.viewpager_children_pager.ShoppingPager;
import com.example.hasee.mynews.utils.LogUtil;
import com.example.hasee.mynews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by lzq on 2016/10/16.
 */
public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewPager;
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("主页面的视图被初始化了...");
        View view = View.inflate(context, R.layout.fragment_content,null);
//        rg_main = (RadioGroup) view.findViewById(R.id.rg_main);
        //把View注入到xUtils中
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("主页面的数据被初始化了...");
        rg_main.check(R.id.rb_home);

        basePagers = new ArrayList<BasePager>();
        basePagers.add(new HomePager(context));
        basePagers.add(new NewsCenterPager(context));
        basePagers.add(new ShoppingPager(context));
        basePagers.add(new ShoppingCartPager(context));
        basePagers.add(new SetingsPager(context));
        viewPager.setAdapter(new MyBasePagerAdapter(context,basePagers));

        //监听RadioGroup状态的变化
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());



    }
    /**
     * 得到新闻中心
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {

        return (NewsCenterPager) basePagers.get(1);
    }


    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
          /* if(checkedId==R.id.rb_news) {
               setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
           }
            else {
               setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
           }*/
            switch (checkedId) {
                case R.id.rb_home://主页面
                    //不可以侧滑
                    viewPager.setCurrentItem(0, false);


                    break;
                case R.id.rb_news://新闻中心
                    //可以侧滑
                    viewPager.setCurrentItem(1, false);
                    setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    Log.e("TAGG", "rb_news被点击了");
                    break;
                case R.id.rb_shopping://商城
                    viewPager.setCurrentItem(2, false);
                    Log.e("TAGG", "rb_shopping被点击了");
                    break;
                case R.id.rb_shopping_cart://购物车
                    viewPager.setCurrentItem(3, false);
                    Log.e("TAGG", "rb_shopping_cart被点击了");
                    break;
                case R.id.rb_setting://设置
                    viewPager.setCurrentItem(4, false);
                    Log.e("TAGG", "rb_setting被点击了");
                    break;
            }

        }
    }

    private void setTouchModeAbove(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeFullscreen);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();
           // rg_main.check(position);
            Log.e("TAGG", "check(position)=="+position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
