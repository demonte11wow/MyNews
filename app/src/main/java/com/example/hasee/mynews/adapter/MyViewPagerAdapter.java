package com.example.hasee.mynews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by lzq on 2016/10/15.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ImageView> imageViews;
    public MyViewPagerAdapter(Context context,ArrayList<ImageView> imageViews){
        this.context=context;
        this.imageViews=imageViews;
    }
    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object==view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        container.addView(imageView);
        return imageView;
    }


}
