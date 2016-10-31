package com.example.hasee.mynews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzq on 2016/10/16.
 */
public abstract class BaseFragment extends Fragment {
    public Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * 交给子类实现，让子类实现自己特有的效果
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    public abstract  View initView();

    /**
     * 当子类需要绑定数据到ui的时候，重写该方法
     * 1.绑定数据
     * 2.联网请求
     *
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {

    }

}
