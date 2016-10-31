package com.example.hasee.mynews.pager.viewpager_children_pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.hasee.mynews.activity.MainActivity;
import com.example.hasee.mynews.base.BaseLeftMeauDetailsPager;
import com.example.hasee.mynews.base.BasePager;
import com.example.hasee.mynews.bean.NetCenterPageBean2;
import com.example.hasee.mynews.bean.NewsCenterPagerBean2;
import com.example.hasee.mynews.fragment.LeftMenuFragment;
import com.example.hasee.mynews.pager.newscenter_framlaout_pager.InteractDeatailPager;
import com.example.hasee.mynews.pager.newscenter_framlaout_pager.NewsDeatailPager;
import com.example.hasee.mynews.pager.newscenter_framlaout_pager.PhotosDeatailPager;
import com.example.hasee.mynews.pager.newscenter_framlaout_pager.TopicDeatailPager;
import com.example.hasee.mynews.utils.CacheUtils;
import com.example.hasee.mynews.utils.Constants;
import com.example.hasee.mynews.utils.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzq on 2016/10/17.
 */
public class NewsCenterPager extends BasePager {
    /**
     * 左侧菜单对应数据
     */
    private ArrayList<NewsCenterPagerBean2.NewsCenterPagerData> leftData;
    /**
     * 左侧菜单对应的详情页面集合
     */
    private ArrayList<BaseLeftMeauDetailsPager> menuDetailBasePagers;



    public NewsCenterPager(Context context) {
        super(context);


    }
    @Override
    public void initData() {

        ib_menu.setVisibility(View.VISIBLE);
        LogUtil.e("新闻中心数据加载了....");
        tv_title.setText("新闻");
        TextView textView = new TextView(context);
        textView.setText("新闻中心页面");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        fl_base_content.addView(textView);

        String saveJson = CacheUtils.getString(context,Constants.NEWS_CENTER_URL);//
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        //联网请求
        getDataFromNet();
    }



    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWS_CENTER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                //数据保存起来
                CacheUtils.putString(context,Constants.NEWS_CENTER_URL,result);


                processData( result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("失败=="+ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        menuDetailBasePagers=new ArrayList<>();

        NewsCenterPagerBean2 netCenterPageBean = parseJson(result);
        //NetCenterPageBean2 netCenterPageBean = paraseJsons2(result);
        leftData= (ArrayList<NewsCenterPagerBean2.NewsCenterPagerData>) netCenterPageBean.getData();
        MainActivity mainActivity = (MainActivity) context;
        menuDetailBasePagers.add(new NewsDeatailPager(context,leftData.get(0)));//新闻

        menuDetailBasePagers.add(new TopicDeatailPager(context,leftData.get(0)));
        menuDetailBasePagers.add(new PhotosDeatailPager(context,leftData.get(2)));
        menuDetailBasePagers.add(new InteractDeatailPager(context,leftData.get(2)));


        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(leftData);


    }

    private NetCenterPageBean2 paraseJsons2(String result) {
        NetCenterPageBean2 netCenterPageBean2 = new NetCenterPageBean2();
        try {
            JSONObject  jsonObject = new JSONObject(result);
            int recode = jsonObject.optInt("recode");
            netCenterPageBean2.setRecode(recode);

            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
            if(jsonArrayData!=null) {
                List<NetCenterPageBean2.NewsCenterPagerData> data = new ArrayList<>();
                netCenterPageBean2.setData(data);
                for (int i=0;i<jsonArrayData.length();i++){
                    JSONObject itemData = (JSONObject) jsonArrayData.get(i);
                    NetCenterPageBean2.NewsCenterPagerData newsCenterPagerData = new NetCenterPageBean2.NewsCenterPagerData();
                    int id = itemData.optInt("id");
                    newsCenterPagerData.setId(id);
                    int type = itemData.optInt("type");
                    newsCenterPagerData.setType(type);
                    String title = itemData.optString("title");
                    newsCenterPagerData.setTitle(title);
                    String url = itemData.optString("url");
                    newsCenterPagerData.setUrl(url);
                    String url1 = itemData.optString("url1");
                    newsCenterPagerData.setUrl1(url1);
                    String dayurl = itemData.optString("dayurl");
                    newsCenterPagerData.setDayurl(dayurl);
                    String excurl = itemData.optString("excurl");
                    newsCenterPagerData.setExcurl(excurl);
                    String weekurl = itemData.optString("weekurl");
                    newsCenterPagerData.setWeekurl(weekurl);
                    JSONArray childrenJsonArray = itemData.optJSONArray("children");

                    if(childrenJsonArray!=null) {
                        List<NetCenterPageBean2.NewsCenterPagerData.ChildrenData> children = new ArrayList();
                        newsCenterPagerData.setChildren(children);

                        for (int j = 0; j < childrenJsonArray.length(); j++) {

                            JSONObject chilrenItemData = (JSONObject) childrenJsonArray.get(j);

                            if (chilrenItemData != null) {

                                NetCenterPageBean2.NewsCenterPagerData.ChildrenData childrenData = new NetCenterPageBean2.NewsCenterPagerData.ChildrenData();

                                //添加到集合中
                                children.add(childrenData);
                                //添加数据
                                childrenData.setId(chilrenItemData.optInt("id"));
                                childrenData.setType(chilrenItemData.optInt("type"));
                                childrenData.setTitle(chilrenItemData.optString("title"));
                                childrenData.setUrl(chilrenItemData.optString("url"));
                            }

                        }

                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return netCenterPageBean2;
    }

    private NewsCenterPagerBean2 parseJson(String json) {
        return new Gson().fromJson(json,NewsCenterPagerBean2.class);
    }

    public void swichPager(int position) {
        //1.改变标题
        tv_title.setText(leftData.get(position).getTitle());
        //2.改变内容
        BaseLeftMeauDetailsPager detailBasePager = menuDetailBasePagers.get(position);//NewsDeatailPager,TopicDeatailPager
        View rootView =  detailBasePager.rootView;
        detailBasePager.initData();//初始数据
        //把之前的移除
        fl_base_content.removeAllViews();
        //添加到FrameLayout
        fl_base_content.addView(rootView);


        if (position == 2) {
            ib_swich_list_grid.setVisibility(View.VISIBLE);
            ib_swich_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PhotosDeatailPager photosDeatailPager = (PhotosDeatailPager) menuDetailBasePagers.get(2);
                    photosDeatailPager.swicheListAndGrid(ib_swich_list_grid);

                }
            });
        } else {
            ib_swich_list_grid.setVisibility(View.GONE);
        }

    }




}
