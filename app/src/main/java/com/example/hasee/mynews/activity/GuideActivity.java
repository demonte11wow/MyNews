package com.example.hasee.mynews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.adapter.MyViewPagerAdapter;
import com.example.hasee.mynews.utils.DensityUtil;
import com.example.hasee.mynews.utils.LogUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private ViewPager viewPager;
    private LinearLayout ll_point_group;
    private Button btn_start_main;
    private ImageView iv_red_point;
    private ArrayList<ImageView> imageViews;
    /**
     两点间的间距
     */
    private int leftMargin;

    private  int widthDpi ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViews();

        initData();
        viewPager.setAdapter(new MyViewPagerAdapter(this,imageViews));
        setListener();


    }

    private void setListener() {
        //求间距
        //构造方法-->测量（measure-onMeasure）-->layout(onLayout)-->draw(onDraw)

        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //监听ViewPager页面滑动的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        btn_start_main.setOnClickListener(new MyOnClickListener());
    }


    private void findViews() {
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        ll_point_group = (LinearLayout)findViewById(R.id.ll_point_group);
        btn_start_main = (Button)findViewById(R.id.btn_start_main);
        iv_red_point = (ImageView)findViewById(R.id.iv_red_point);
    }

    private void initData() {
        //设置适配器--准备数据
        int ids[] = new int[]{
                R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3
        };
        widthDpi = DensityUtil.dip2px(this,10);
        imageViews=new ArrayList<>();
        for (int i=0;i<ids.length;i++ ){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(ids[i]);
            imageViews.add(imageView);

            ImageView imagePoint = new ImageView(this);
            imagePoint.setImageResource(R.drawable.point_gray);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this,10),DensityUtil.dip2px(this,10));

            if(i!=0) {
                params.leftMargin = widthDpi;

            }

            imagePoint.setLayoutParams(params);
            ll_point_group.addView(imagePoint);

        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离 = ViewPager页面的百分比* 间距

//            float leftMargin = positionOffset * leftMarg;
            //坐标 = 起始位置 + 红点移动的距离；

           /* float left = (position + positionOffset )* leftMargin;
            RelativeLayout.LayoutParams paramgs = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            paramgs.leftMargin = (int) left;
            iv_red_point.setLayoutParams(paramgs);*/
            float left =(position+positionOffset)*leftMargin;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin= (int) left;
            iv_red_point.setLayoutParams(params);

            LogUtil.e("position=="+position+",positionOffset=="+positionOffset+",positionOffsetPixels=="+positionOffsetPixels);


        }

        @Override
        public void onPageSelected(int position) {
            if(position ==imageViews.size()-1){//滑动到最后一个页面显示按钮
                //让按钮显示
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                //按钮隐藏
                btn_start_main.setVisibility(View.GONE);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{

        @Override
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            leftMargin=ll_point_group.getChildAt(1).getLeft()-ll_point_group.getChildAt(0).getLeft();
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
