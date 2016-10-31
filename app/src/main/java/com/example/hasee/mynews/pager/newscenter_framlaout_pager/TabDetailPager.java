package com.example.hasee.mynews.pager.newscenter_framlaout_pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.activity.NewsDetailActivity;
import com.example.hasee.mynews.base.BaseLeftMeauDetailsPager;
import com.example.hasee.mynews.bean.NewsCenterPagerBean2;
import com.example.hasee.mynews.bean.TabDetailPagerBean;
import com.example.hasee.mynews.utils.CacheUtils;
import com.example.hasee.mynews.utils.Constants;
import com.example.hasee.mynews.utils.DensityUtil;
import com.example.refresh_library.RefreshListView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzq on 2016/10/18.
 */


public class TabDetailPager extends BaseLeftMeauDetailsPager {

    private MyRunable myRunable;


    private final ImageOptions imageOptions;
    /**
     * 新闻列表的数据
     */
    private List<TabDetailPagerBean.DataEntity.NewsEntity> news;

    private TabDetailPagerListAdapter adapter;
    private TabDetailPagerBean.DataEntity.NewsEntity newsEntity;
    private int prePosition;

    //@ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    //@ViewInject(R.id.tv_title)
    private TextView tv_title;
    //@ViewInject(R.id.ll_point_group)
    private LinearLayout ll_point_group;
    //@ViewInject(R.id.listview)
    private RefreshListView listView;
    private  NewsCenterPagerBean2.NewsCenterPagerData.ChildrenData childrenBean;
    private ArrayList<TabDetailPagerBean.DataEntity.TopnewsEntity> topnews;
    private String url;

    private String moreUrl;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;

    private boolean isDragging = false;

    public TabDetailPager(Context context, NewsCenterPagerBean2.NewsCenterPagerData.ChildrenData childrenBean) {
        super(context);
        this.childrenBean=childrenBean;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(context,80), DensityUtil.dip2px(context,100))
                .setRadius(DensityUtil.dip2px(context,5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(false) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager,null);
        View topnews = View.inflate(context, R.layout.topnews, null);
        //x.view().inject(TabDetailPager.this, topnews);
        //x.view().inject(TabDetailPager.this,view);
        viewPager = (ViewPager) topnews.findViewById(R.id.viewpager);

        listView= (RefreshListView) view.findViewById(R.id.listview);
        tv_title = (TextView) topnews.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) topnews.findViewById(R.id.ll_point_group);

        //以头的方式添加顶部轮播图
        listView.addHeaderView(topnews);
        listView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullDownRefresh() {

                getDataFromNet(url);
            }

            @Override
            public void onLoadeMore() {
                if (TextUtils.isEmpty(moreUrl)){
                    //没有更多
                    Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                    listView.onFinishRefrsh(false);
                }else {
                    //加载更多
                    getModeDataFromNet();

                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TabDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position-2);
                String getId = CacheUtils.getString(context,"id");
                if(!getId.contains(newsEntity+"")) {
                    CacheUtils.putString(context,"id",getId+newsEntity.getId()+",");
                }
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.setData(Uri.parse(Constants.BASE_URL+newsEntity.getUrl()));
                context.startActivity(intent);

            }
        });


        //viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        x.view().inject(TabDetailPager.this,view);



        return view;


    }

    @Override
    public void initData() {
        super.initData();
         url = Constants.BASE_URL+childrenBean.getUrl();
        LogUtil.e(url);


        String saveJson = CacheUtils.getString(context,url);//
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromNet(url);

    }

    private void getDataFromNet(String url) {

        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("TabDetailPager联网请求成功==" + result);
                Log.e("TTTTT", "onSuccess");
                listView.onFinishRefrsh(true);
                //数据保存起来
                CacheUtils.putString(context,Constants.BASE_URL+childrenBean.getUrl(),result);

                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                listView.onFinishRefrsh(false);
                Log.e("TTTTT", "onError");
                LogUtil.e("TabDetailPager联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        TabDetailPagerBean pagerBean = paraseJson(json);
        LogUtil.e(pagerBean.getData().getNews().get(1).getTitle());

        moreUrl = pagerBean.getData().getMore();//""

        if (TextUtils.isEmpty(moreUrl)) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + pagerBean.getData().getMore();
        }

        if(!isLoadMore){
            topnews= (ArrayList<TabDetailPagerBean.DataEntity.TopnewsEntity>) pagerBean.getData().getTopnews();


            if(topnews != null && topnews.size() >0) {

                viewPager.setAdapter(new TabDetailPagerAdapter());
                tv_title.setText(topnews.get(prePosition).getTitle());
                viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
                ll_point_group.removeAllViews();

                for (int i = 0; i < topnews.size(); i++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setBackgroundResource(R.drawable.point_selector);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5));

                    if (i != 0) {
                        imageView.setEnabled(false);
                        params.leftMargin = DensityUtil.dip2px(context, 5);
                    } else {
                        imageView.setEnabled(true);
                    }
                    imageView.setLayoutParams(params);
                    ll_point_group.addView(imageView);


                }
                news = pagerBean.getData().getNews();
                if (news != null && news.size() > 0) {
                    adapter = new TabDetailPagerListAdapter();
                    listView.setAdapter(adapter);

                }
    //            ll_point_group.getChildAt(prePosition).setEnabled(true);
            }

        }else {
            isLoadMore = false;
            //把得到的更多数据加载到原来的集合中
            news.addAll(pagerBean.getData().getNews());
            adapter.notifyDataSetChanged();
        }




      /*  int item= Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%topnews.size();
        viewPager.setCurrentItem(item);*/






       if(myRunable==null) {
           int item= Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%topnews.size();
           viewPager.setCurrentItem(item);
           myRunable=new MyRunable();
       }
       viewPager.removeCallbacks(myRunable);
       viewPager.postDelayed(myRunable,3000);



    }
    private TabDetailPagerBean paraseJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }

    public void getModeDataFromNet() {

            RequestParams params = new RequestParams(moreUrl);
            params.setConnectTimeout(4000);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("TabDetailPager加载更多联网请求成功==" + result);
                    isLoadMore = true;
                    processData(result);
                    listView.onFinishRefrsh(false);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e("TabDetailPager加载更多联网请求失败==" + ex.getMessage());
                    listView.onFinishRefrsh(false);
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

    private class TabDetailPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e("ASDF", "instantiateItem");

            final ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.pic_item_list_default);

            x.image().bind(imageView,Constants.BASE_URL+topnews.get(position%topnews.size()).getTopimage());
            container.addView(imageView);
            imageView.setTag(position%topnews.size());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int position = (int) v.getTag();

                    TabDetailPagerBean.DataEntity.TopnewsEntity topnewsEntity = topnews.get(position);
                    Intent intent = new Intent(context,NewsDetailActivity.class);
                    intent.setData(Uri.parse(Constants.BASE_URL+topnewsEntity.getUrl()));
                    context.startActivity(intent);



                }
            });
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            LogUtil.e("按下ACTION_DOWN");
                          viewPager.removeCallbacks(myRunable);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            LogUtil.e("离开ACTION_UP");
                            viewPager.removeCallbacks(myRunable);
                            viewPager.postDelayed(myRunable,3000);
                           // viewPager.postDelayed(new MyRunable(),3000);
                           //handler.postDelayed(new MyRunable(), 3000);

                            break;
                    }
                    return false;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position % topnews.size();
            //把之前高亮的点设置为默认
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //把当前的位置对应的点设置高亮
            ll_point_group.getChildAt(realPosition).setEnabled(true);

            prePosition = realPosition;
        }


        @Override
        public void onPageSelected(int position) {

            int realPosition = position % topnews.size();
            tv_title.setText(topnews.get(realPosition).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
           /* if(state ==ViewPager.SCROLL_STATE_DRAGGING){
                isDragging =true;
                Log.e("DDDD", "draging");
                handler.removeCallbacksAndMessages(null);

            }else if(state ==ViewPager.SCROLL_STATE_SETTLING){

                Log.e("DDDD", "SCROLL_STATE_SETTLING");
               *//* handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunable(),3000);*//*
            }else if(state ==ViewPager.SCROLL_STATE_IDLE){
                Log.e("DDDD", "SCROLL_STATE_IDLE");
               if(isDragging) {
                   isDragging=false;
                   handler.removeCallbacksAndMessages(null);
                   handler.postDelayed(new MyRunable(),3000);
               }

            }*/
            if(state==ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging=true;
                 viewPager.removeCallbacks(myRunable);
                Log.e("TAG", "ViewPager拖拽中----");
            }else if(state==ViewPager.SCROLL_STATE_IDLE&&isDragging) {
                Log.e("OOOA", "这个地方进不来");
                isDragging=false;
                viewPager.removeCallbacks(myRunable);
                viewPager.postDelayed(myRunable,3000);


                Log.e("TAG", "ViewPager静止中----");
            }else if(state==ViewPager.SCROLL_STATE_SETTLING&&isDragging) {

                    isDragging=false;
                    viewPager.removeCallbacks(myRunable);
                    viewPager.postDelayed(myRunable,3000);
                    Log.e("TAG", "ViewPager滑动----");
            }



        }
    }

    private class TabDetailPagerListAdapter  extends BaseAdapter{
        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler = null;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_tabdetail_pager,null);
                viewHodler = new ViewHodler();
                viewHodler.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHodler.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHodler.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHodler);
            }else{
                viewHodler = (ViewHodler) convertView.getTag();
            }

            //根据位置得到对应的数据
            TabDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
            viewHodler.tv_title.setText(newsEntity.getTitle());
            viewHodler.tv_time.setText(newsEntity.getPubdate());
            String saveId = CacheUtils.getString(context,"id");
            if(saveId.contains(news.get(position).getId()+"")) {
                viewHodler.tv_title.setTextColor(Color.GRAY);
            }else {
                viewHodler.tv_title.setTextColor(Color.BLACK );
            }

            //请求图片
            x.image().bind(viewHodler.iv_icon,Constants.BASE_URL+newsEntity.getListimage(),imageOptions);


            return convertView;
        }
    }

    static class ViewHodler {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

   /* private class InternalHandler  extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = (viewPager.getCurrentItem()+1);
            viewPager.setCurrentItem(item);
            handler.removeCallbacksAndMessages(null);


        }
    }*/

    private class MyRunable implements Runnable {

        @Override
        public void run() {
            int item = (viewPager.getCurrentItem()+1);
            viewPager.setCurrentItem(item);
            viewPager.postDelayed(myRunable,3000);
        }
    }
}
