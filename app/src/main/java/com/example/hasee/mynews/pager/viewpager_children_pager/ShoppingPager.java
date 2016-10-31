package com.example.hasee.mynews.pager.viewpager_children_pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.hasee.mynews.R;
import com.example.hasee.mynews.adapter.ShoppingPagerAdpater;
import com.example.hasee.mynews.base.BasePager;
import com.example.hasee.mynews.bean.ShoppingPagerBean;
import com.example.hasee.mynews.utils.CacheUtils;
import com.example.hasee.mynews.utils.Constants;
import com.example.hasee.mynews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by lzq on 2016/10/17.
 */
public class ShoppingPager extends BasePager {
    private MaterialRefreshLayout refresh;
    private RecyclerView recyclerview;
    private ProgressBar progressbar;



    private Boolean isRefresh ;
    private String url;
    /**
     * 每页要求10个数据
     */
    private int pageSize = 10;
    /**
     * 第几页
     */
    private int curPage = 1;

    /*
    总的多少页
     */
    private int totalPager;
    /**
     * 商城热卖的数据集合
     */




    private ShoppingPagerAdpater adpater;


    private List<ShoppingPagerBean.Wares> list;
    public ShoppingPager(Context context) {
        super(context);
    }
    @Override
    public void initData() {

        View view = View.inflate(context, R.layout.shopping_pager,null);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);



        LogUtil.e("商城页面数据加载了....");
        tv_title.setText("商城");

        fl_base_content.removeAllViews();

        //子页面的视图和FrameLayout结合在一起，形成一个新的页面
        fl_base_content.addView(view);

        //设置下拉刷新和上拉刷新的监听
        initRefreshLayout();
        getDataFromNet();
    }

    private void initRefreshLayout() {
        refresh.setMaterialRefreshListener(new MyMaterialRefreshListener());
    }

    private void getDataFromNet() {
        curPage = 1;

        url = Constants.WARES_HOT_URL+"pageSize="+pageSize+"&curPage="+curPage;
        String saveJson = CacheUtils.getString(context, Constants.WARES_HOT_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                org.xutils.common.util.LogUtil.e("TabDetailPager加载更多联网请求成功==" + result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
              LogUtil.e("TabDetailPager加载更多联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
               LogUtil.e("onFinished==");
            }
        });



    }

    private void processData(String saveJson) {
        ShoppingPagerBean bean = new Gson().fromJson(saveJson,ShoppingPagerBean.class);
        curPage=bean.getCurrentPage();
        totalPager=bean.getTotalPage();
        pageSize=bean.getPageSize();
        list = bean.getList();
        if(list!=null&&list.size()>0) {
            LogUtil.e("curPage==" + curPage + ",pageSize==" + pageSize + ",totalPager==" + totalPager + ",name==" + bean.getList().get(0).getName());
            adpater =new ShoppingPagerAdpater(context,list);
            recyclerview.setAdapter(adpater);
            recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        }
        progressbar.setVisibility(View.GONE);
    }

    private class MyMaterialRefreshListener extends MaterialRefreshListener {
        /**
         * 下拉刷新
         */
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            refreshData();


        }

        /**
         * 上拉刷新（加载更多）
         * @param materialRefreshLayout
         */
        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            loadMoreData();
        }
    }

    private void loadMoreData() {
        isRefresh=false;

        if(curPage < totalPager){
            curPage +=1;
            //联网请求
            url = Constants.WARES_HOT_URL + "pageSize=" + pageSize + "&curPage=" + curPage;

            RequestParams params = new RequestParams(url);
            params.setConnectTimeout(4000);

            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    org.xutils.common.util.LogUtil.e("TabDetailPager加载更多联网请求成功==" + result);

                  processDownRefreshOrLoadMore(result);

                    refresh.finishRefreshLoadMore();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e("TabDetailPager加载更多联网请求失败==" + ex.getMessage());
                    refresh.finishRefreshLoadMore();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    LogUtil.e("onCancelled==" + cex.getMessage());
                }

                @Override
                public void onFinished() {
                    LogUtil.e("onFinished==");
                }
            });

        }else{
            //没有更多页面
            refresh.finishRefreshLoadMore();
            Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
        }


    }



    private void refreshData() {
        isRefresh=true;
        curPage = 1;

        url = Constants.WARES_HOT_URL + "pageSize=" + pageSize + "&curPage=" + curPage;


        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                org.xutils.common.util.LogUtil.e("下拉刷新联网请求成功==" + result);
                processDownRefreshOrLoadMore(result);



               refresh.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("TabDetailPager下拉刷新联网请求失败==" + ex.getMessage());
                refresh.finishRefresh();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });




    }

    private void processDownRefreshOrLoadMore(String saveJson) {



        ShoppingPagerBean bean = new Gson().fromJson(saveJson,ShoppingPagerBean.class);
        curPage=bean.getCurrentPage();
        totalPager=bean.getTotalPage();
        pageSize=bean.getPageSize();
        list = bean.getList();
        if(list!=null&&list.size()>0) {
            LogUtil.e("curPage==" + curPage + ",pageSize==" + pageSize + ",totalPager==" + totalPager + ",name==" + bean.getList().get(0).getName());


            if(isRefresh) {
                adpater.clearData();
                adpater.addData(list);
            }else {
                adpater.addData(adpater.getCount(),list);
            }



        }
        progressbar.setVisibility(View.GONE);

    }



}
