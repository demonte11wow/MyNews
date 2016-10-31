package com.example.hasee.mynews.pager.newscenter_framlaout_pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.activity.MainActivity;
import com.example.hasee.mynews.base.BaseLeftMeauDetailsPager;
import com.example.hasee.mynews.bean.NewsCenterPagerBean2;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by lzq on 2016/10/17.
 */
public class NewsDeatailPager extends BaseLeftMeauDetailsPager {

    /**
     * 新闻详情页面的数据
     */
    private ArrayList<NewsCenterPagerBean2.NewsCenterPagerData.ChildrenData> childrenData;
    /**
     * 新闻详情页面的UI集合
     */
    private ArrayList<BaseLeftMeauDetailsPager> detailBasePagers;
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;

    public NewsDeatailPager(Context context,NewsCenterPagerBean2.NewsCenterPagerData dataBean) {
        super(context);

        childrenData= (ArrayList<NewsCenterPagerBean2.NewsCenterPagerData.ChildrenData>) dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_details_pager, null);
        x.view().inject(this, view);

        return view;

    }
    @Event(value = R.id.ib_next)
    private void tabNext(View view){
        //有点击事件
        viewpager.setCurrentItem(viewpager.getCurrentItem()+1);
    }


    @Override
    public void initData() {
        super.initData();
        detailBasePagers = new ArrayList<>();
        for (int i=0;i<childrenData.size();i++){
            //12个页签页面
            detailBasePagers.add(new TabDetailPager(context,childrenData.get(i)));
        }
        viewpager.setAdapter(new MyNewsDetailPagerAdapter());
        //TabPageIndicator和ViewPager关联，关联要在ViewPager设置适配器之后
       indicator.setViewPager(viewpager);
        //关联后，监听页面的改变由TabPageIndicator
        indicator.setOnPageChangeListener(new MyOnPageChangeListener());

    }




    private class MyNewsDetailPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
                return childrenData.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return detailBasePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseLeftMeauDetailsPager tabDetailPager =  detailBasePagers.get(position);// TabDetailPager
            View rootView =  tabDetailPager.rootView;
            tabDetailPager.initData();//初始化数据
            container.addView(rootView);
            return  rootView;
//            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                //北京-SlidingMenu可以滑动
                setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                //其他-SlidingMenu不可以滑动
                setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void setTouchModeAbove(int touchmodeNone) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeNone);
    }

}
