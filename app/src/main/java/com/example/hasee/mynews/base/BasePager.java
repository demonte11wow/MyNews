package com.example.hasee.mynews.base;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hasee.mynews.R;
import com.example.hasee.mynews.activity.MainActivity;

/**
 * Created by lzq on 2016/10/17.
 */
public class BasePager {
    public Context context;
    public View rootView;
    public TextView tv_title;
    public ImageButton ib_menu;
    public ImageButton ib_swich_list_grid;
    public Button btn_cart;

    public FrameLayout fl_base_content;


    public BasePager(Context context){
        this.context = context;
        this.rootView=initView();

    }

    private  View initView() {
        View view = View.inflate(context, R.layout.basepager,null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_base_content= (FrameLayout) view.findViewById(R.id.fl_base_content);
        ib_swich_list_grid= (ImageButton) view.findViewById(R.id.ib_swich_list_grid);

        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity  mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        btn_cart = (Button) view.findViewById(R.id.btn_cart);
        return view;
    }
    public void  initData(){

    }
}
