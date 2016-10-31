package com.example.refresh_library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzq on 2016/10/19.
 */


public class RefreshListView extends ListView {

    private LinearLayout headView;
    private ImageView iv_red_arrow;
    private ProgressBar progressbar;
    private View pullDwonView;
    private TextView tv_status;
    private TextView tv_time;
    private float headerHeight;


    /**
     下拉刷新状态
     */
    private static final  int PULL_DOWN_REFRESH = 0;


    /**
     手松刷新状态
     */
    private static final  int RELEASE_REFRESH = 1;


    /**
     正在刷新状态
     */
    private static final  int REFRESHING = 2;



    /**
     当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;


    private Animation downAnimation;
    private Animation upAnimation;
    private View footerView;
    private int footerViewHeight;
    private boolean isLoadMore =false;

    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initAnimation() {
        downAnimation = new RotateAnimation(-180,360,RotateAnimation.RELATIVE_TO_SELF ,0.5f,RotateAnimation.RELATIVE_TO_SELF ,0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);

        upAnimation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF ,0.5f,RotateAnimation.RELATIVE_TO_SELF ,0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
    }

    private void initView(Context context) {
        headView = (LinearLayout) View.inflate(context, R.layout.header_refresh,null);
        iv_red_arrow = (ImageView)headView.findViewById(R.id.iv_red_arrow);
        progressbar = (ProgressBar)headView.findViewById(R.id.progressbar);
        pullDwonView = headView.findViewById(R.id.ll_pull_down);
        tv_status = (TextView)headView.findViewById(R.id.tv_status);
        tv_time = (TextView)headView.findViewById(R.id.tv_time);

        pullDwonView.measure(0,0);
        headerHeight=pullDwonView.getMeasuredHeight();
        //      View.setPaddingTop(0,-控件的高，0,0);//完成隐藏
//        View.setPaddingTop(0,0，0,0);//完成显示
//        View.setPaddingTop(0,控件的高，0,0);//控件两倍显示
        pullDwonView.setPadding(0, (int) -headerHeight,0,0);


        addHeaderView(headView);
        //addFooterView(footerView);
    }
    private void initFooterView(Context context) {
        footerView = View.inflate(context,R.layout.footview_refresh,null);

        //默认是隐藏

//        View.setPaddingTop(0,-控件的高，0,0);//完成隐藏
//        View.setPaddingTop(0,0，0,0);//完成显示
//        View.setPaddingTop(0,控件的高，0,0);//控件两倍显示
        footerView.measure(0,0);
        footerViewHeight =  footerView.getMeasuredHeight();
        footerView.setPadding(0,-footerViewHeight,0,0);


        addFooterView(footerView);
        setOnScrollListener(new MyOnScrollListener());
    }
    private float startY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                 float distance = endY-startY;
                if(currentStatus==REFRESHING) {
                    break;
                }
                
                
                if(distance>0) {
                    float padingTop =-headerHeight+distance;
                    
                    if(padingTop>headerHeight*0.5&&currentStatus!=RELEASE_REFRESH) {
                        currentStatus = RELEASE_REFRESH;
                        LogUtil.e("手松刷新....");

                        refreshView(currentStatus);



                    }else  if(padingTop<headerHeight*0.5&&currentStatus!=PULL_DOWN_REFRESH) {
                        currentStatus=PULL_DOWN_REFRESH;
                        LogUtil.e("下拉刷新....");

                        refreshView(currentStatus);
                    }
                    pullDwonView.setPadding(0, (int) (padingTop),0,0);
                }
                
                break;
            case MotionEvent.ACTION_UP:
                if(currentStatus==PULL_DOWN_REFRESH) {
                    pullDwonView.setPadding(0, (int) -headerHeight,0,0);
                }else if(currentStatus==RELEASE_REFRESH) {
                    currentStatus=REFRESHING;
                    pullDwonView.setPadding(0,0,0,0);
                    if(onRefreshListener!=null) {
                        onRefreshListener.onPullDownRefresh();

                    }
                    refreshView(currentStatus);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshView(int currentStatus) {



        switch (currentStatus){
            case PULL_DOWN_REFRESH:
                tv_status.setText("下拉刷新...");
                iv_red_arrow.setVisibility(VISIBLE);
                progressbar.setVisibility(GONE);
               iv_red_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH:
                tv_status.setText("松手刷新...");
                iv_red_arrow.startAnimation(upAnimation);

                break;
            case REFRESHING:

                iv_red_arrow.clearAnimation();
                iv_red_arrow.setVisibility(GONE);
                tv_status.setText("正在刷新...");
                progressbar.setVisibility(VISIBLE);
                break;
        }
    }


    public void onFinishRefrsh(boolean success) {


        if(isLoadMore) {
            isLoadMore = false;
            footerView.setPadding(0,-footerViewHeight,0,0);//设置隐藏

        }else {
            currentStatus = PULL_DOWN_REFRESH;
            pullDwonView.setPadding(0, (int) -headerHeight, 0, 0);//把下拉刷新控件隐藏
            progressbar.setVisibility(GONE);
            iv_red_arrow.setVisibility(VISIBLE);
            tv_status.setText("下拉刷新...");
            if (success) {
                tv_time.setText("上次更新时间:" + getSystemTime());
            }
        }

    }

    /**
     * 得到当前的时间
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }


    //
    public OnRefreshListener onRefreshListener;
    // 刷新的监听器
    public interface OnRefreshListener {
        //当下拉刷新的时候回调这个方法
        public void onPullDownRefresh();
        public void onLoadeMore();
    }

    //设置刷新的监听：下拉刷新和上拉刷新（加载更多）
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener=onRefreshListener;
    }


    private class MyOnScrollListener implements OnScrollListener {
        /**
         * ListView当状态变化的时候回调
         * 静止-->手指滑动
         * 手指滑动-->惯性滚动
         * 惯性滚动-->静止
         * @param view
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState==OnScrollListener.SCROLL_STATE_IDLE || scrollState==OnScrollListener.SCROLL_STATE_FLING){
                //并且是滑动到最后一条的时候
                //当滑动到最后一个可见并且等于集合中最后一条位置的时候
                if(getLastVisiblePosition()==getAdapter().getCount()-1){

                    //1.状态修改加载更多
                    isLoadMore = true;

                    //2.显示加载更多控件
                    footerView.setPadding(10,10,10,10);

                    //3.回调接口
                    if(onRefreshListener != null){
                        onRefreshListener.onLoadeMore();
                    }

                }
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}
