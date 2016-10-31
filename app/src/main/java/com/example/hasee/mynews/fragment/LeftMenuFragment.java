package com.example.hasee.mynews.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.activity.MainActivity;
import com.example.hasee.mynews.base.BaseFragment;
import com.example.hasee.mynews.bean.NewsCenterPagerBean2;
import com.example.hasee.mynews.pager.viewpager_children_pager.NewsCenterPager;
import com.example.hasee.mynews.utils.DensityUtil;
import com.example.hasee.mynews.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by lzq on 2016/10/16.
 */
public class LeftMenuFragment extends BaseFragment {
    private ArrayList<NewsCenterPagerBean2.NewsCenterPagerData>leftData;
    private int selectPosition;
    private MyListViewAdapter myListViewAdapter;
    private ListView listView;

    @Override
    public View initView() {
         listView = new ListView(context);



        listView.setPadding(0, DensityUtil.dip2px(context,40f),0,0);
        listView.setDividerHeight(0);
        //按下某个设置为没有效果
        listView.setSelector(android.R.color.transparent);
        //屏蔽ListView在低版本的手机上，整个会变灰
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setOnItemClickListener(new MyOnItemClickListener());


        return listView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单的数据被初始化了...");

    }


        public void setData(ArrayList<NewsCenterPagerBean2.NewsCenterPagerData> data) {
            this.leftData = data;
            Log.e("TAGG", "leftData="+leftData);
//        for(int i=0;i<leftdata.size();i++){
//            LogUtil.e(leftdata.get(i).getTitle());
//        }

            myListViewAdapter = new MyListViewAdapter();
            //设置适配器
            listView.setAdapter(myListViewAdapter);

            swichPager(selectPosition);

        }


    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.getSlidingMenu().toggle();
            //1.点击的时候，设置被点击的高亮
            selectPosition = position;
            myListViewAdapter.notifyDataSetChanged();//getCount()-->getView();

            //3.点击的时候
            swichPager(selectPosition);
        }
    }

    private void swichPager(int selectPosition) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContantFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.swichPager(selectPosition);

    }

    public class MyListViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return leftData.size();
        }

        @Override
        public Object getItem(int position) {
            return leftData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.e("TAGG", "getView");


            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenufragment,null);

            textView.setText(leftData.get(position).getTitle());
            Log.e("TAGG", "leftdata.get(position).getTitle()"+leftData.get(position).getTitle());

//            if(position ==selectPosition){
//                textView.setEnabled(true);
//            }else{
//                textView.setEnabled(false);
//            }
            textView.setEnabled(position ==selectPosition);
            return textView;
        }
    }
}
