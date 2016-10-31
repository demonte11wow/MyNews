package com.atguigu.recyclerviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {


    private Button btnList;
    private Button btnGrid;
    private Button btnFlow;
    private Button btnAdd;
    private Button btnDelete;
    private RecyclerView recyclerView;

    private ArrayList<String> datas;
    private RecyclerViewAdapter adapter;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-10-25 15:38:39 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_main);
        btnList = (Button)findViewById( R.id.btn_list );
        btnGrid = (Button)findViewById( R.id.btn_grid );
        btnFlow = (Button)findViewById( R.id.btn_flow );
        btnAdd = (Button)findViewById( R.id.btn_add );
        btnDelete = (Button)findViewById( R.id.btn_delete );
        recyclerView = (RecyclerView)findViewById( R.id.recyclerView );

        btnList.setOnClickListener( this );
        btnGrid.setOnClickListener( this );
        btnFlow.setOnClickListener( this );
        btnAdd.setOnClickListener( this );
        btnDelete.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-10-25 15:38:39 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnList ) {
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
            // Handle clicks for btnList
        } else if ( v == btnGrid ) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3,GridLayoutManager.VERTICAL,false));
            // Handle clicks for btnGrid
        } else if ( v == btnFlow ) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
            // Handle clicks for btnFlow
        } else if ( v == btnAdd ) {
            // Handle clicks for btnAdd
            adapter.addData(0,"News Data");
            //定位到第0条位置
            recyclerView.scrollToPosition(0);
        } else if ( v == btnDelete ) {
            // Handle clicks for btnDelete
            adapter.removeData(0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        //准备数据
        datas = new ArrayList<>();
        for (int i=0;i<50;i++){
            datas.add("Data_"+i);
        }
        //设置适配器
        adapter = new RecyclerViewAdapter(this,datas);
        recyclerView.setAdapter(adapter);

        //设置LayoutManger
        //ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
        //GridView
//        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3,GridLayoutManager.VERTICAL,false));
        //瀑布流（flow）
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        //自定义分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL_LIST));


        //设置点击某条的监听
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Toast.makeText(MainActivity.this, "data==" + data, Toast.LENGTH_SHORT).show();

            }
        });

        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());



    }
}
