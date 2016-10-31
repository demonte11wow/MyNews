package com.example.hasee.mynews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasee.mynews.base.BasePager;

import java.util.ArrayList;

/**
 * Created by lzq on 2016/10/17.
 */
public class MyBasePagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<BasePager> basePagers;

    public MyBasePagerAdapter(Context context, ArrayList<BasePager> basePagers) {
        this.context = context;
        this.basePagers = basePagers;
    }

    @Override
    public int getCount() {
        return basePagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager pager = basePagers.get(position);
        View view = pager.rootView;
        container.addView(view);

        return view;
    }
}
