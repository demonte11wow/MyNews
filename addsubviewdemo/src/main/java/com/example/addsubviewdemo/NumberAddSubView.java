package com.example.addsubviewdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lzq on 2016/10/27.
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {



    /**
     * 默认值
     */
    private int value = 1;

    /**
     * 最小值
     */
    private int minValue = 1;

    /**
     * 最大值（库存）
     */
    private int maxValue = 1;


    private  final  Context context;
    private Button btn_sub;
    private TextView tv_value;
    private Button btn_add;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_value.setText(value+"");
        if(onValueChangeListener!=null) {
            onValueChangeListener.isEnable(value,minValue,maxValue);
        }

    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public NumberAddSubView(Context context) {
        this(context,null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;

        View view = View.inflate(context,R.layout.add_sub_number_view,NumberAddSubView.this);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        tv_value = (TextView) findViewById(R.id.tv_value);
        btn_add = (Button) findViewById(R.id.btn_add);

        btn_sub.setEnabled(false);
        btn_sub.setOnClickListener(this);
        btn_add.setOnClickListener(this);


        if(attrs!=null) {
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context,attrs,R.styleable.NumberAddSubView);
            int value = typedArray.getIndex(R.styleable.NumberAddSubView_value);
            if(value>0) {
                setValue(value);
            }
            int minValue =  typedArray.getInt(R.styleable.NumberAddSubView_minValue,0);
            if(minValue >0){
                setMinValue(minValue);
            }

            int maxValue =  typedArray.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if(maxValue >0){
                Log.e("AZX", "maxValue"+maxValue);
                setMaxValue(maxValue);
            }
            Drawable number_background =  typedArray.getDrawable(R.styleable.NumberAddSubView_number_background);

            if(number_background!=null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    this.setBackground(number_background);
                }
            }



            Drawable sub_background = typedArray.
                    getDrawable(R.styleable.NumberAddSubView_sub_background);
            if(sub_background != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_sub.setBackground(sub_background);
                }
            }

            Drawable add_background = typedArray.
                    getDrawable(R.styleable.NumberAddSubView_add_background);
            if(add_background != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_add.setBackground(add_background);
                }
            }


        }

        /*if(attrs != null){
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context,attrs,R.styleable.NumberAddSubView);

            int value =  typedArray.getInt(R.styleable.NumberAddSubView_value, 0);
            if(value >0){
                setValue(value);
            }

            int minValue =  typedArray.getInt(R.styleable.NumberAddSubView_minValue,0);
            if(minValue >0){
                setMinValue(minValue);
            }

            int maxValue =  typedArray.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if(maxValue >0){
                setMaxValue(maxValue);
            }


            Drawable number_background = typedArray.
                    getDrawable(R.styleable.NumberAddSubView_number_background);
            if(number_background != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    this.setBackground(number_background);
                }
            }


            Drawable sub_background = typedArray.
                    getDrawable(R.styleable.NumberAddSubView_sub_background);
            if(sub_background != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_sub.setBackground(sub_background);
                }
            }

            Drawable add_background = typedArray.
                    getDrawable(R.styleable.NumberAddSubView_add_background);
            if(add_background != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_add.setBackground(add_background);
                }
            }
        }*/


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sub:
//                Toast.makeText(context,"删除",Toast.LENGTH_SHORT).show();
                subNumber();

                if(listener!=null) {
                    listener.onButtonSubClick(v,value);
                }
                break;
            case R.id.btn_add:
//                Toast.makeText(context,"添加",Toast.LENGTH_SHORT).show();
                addNumber();
                if(listener!=null) {
                    listener.onButtonAddClick(v,value);
                }
                break;
        }
    }

    /**
     * 添加产品   数量
     */
    private void addNumber() {
        if(value < maxValue){
            value++;

        }

        setValue(value);
    }

    private void subNumber() {

            if(value > minValue){
                value --;

            }

            setValue(value);


    }
    interface OnButtonClickListener{
        void onButtonSubClick(View view,int value);
          void onButtonAddClick(View view,int value);
    }
    private OnButtonClickListener listener;



    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.listener=listener;
    }



    interface OnValueChangeListener{
         void isEnable(int value,int minValue,int maxValue);

    }
    private OnValueChangeListener onValueChangeListener;


    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener){
        this.onValueChangeListener= onValueChangeListener;
    }
}
