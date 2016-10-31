package com.example.addsubviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btn_sub;
    private Button btn_add;

    private NumberAddSubView number_add_sub_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number_add_sub_view= (NumberAddSubView) findViewById(R.id.number_add_sub_view);
        btn_sub = (Button)findViewById(R.id.btn_sub);
        btn_add = (Button)findViewById(R.id.btn_add);

        number_add_sub_view.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonSubClick(View view, int value) {
                Toast.makeText(MainActivity.this, "减value=="+value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonAddClick(View view, int value) {
                Toast.makeText(MainActivity.this, "加value=="+value, Toast.LENGTH_SHORT).show();
            }
        });
        number_add_sub_view.setOnValueChangeListener(new NumberAddSubView.OnValueChangeListener() {


            @Override
            public void isEnable(int value, int minValue, int maxValue) {
                if(value<=minValue) {
                    btn_sub.setEnabled(false);
                }else {
                    btn_sub.setEnabled(true);
                }



                if(value>=maxValue) {
                    btn_add.setEnabled(false);
                }else {
                    btn_add.setEnabled(true);
                }
            }
        });

    }
}
