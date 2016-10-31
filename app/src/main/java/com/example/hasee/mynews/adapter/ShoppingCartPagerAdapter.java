package com.example.hasee.mynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hasee.mynews.R;
import com.example.hasee.mynews.bean.ShoppingCart;
import com.example.hasee.mynews.utils.CartProvider;
import com.example.hasee.mynews.view.NumberAddSubView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by lzq on 2016/10/27.
 */
public class ShoppingCartPagerAdapter extends RecyclerView.Adapter<ShoppingCartPagerAdapter.ViewHolder>{


    private final Context context;
    private final List<ShoppingCart> datas;
    private final CartProvider cartProvider;
    private CheckBox checkbox_all;
    private TextView tv_total_price;




    public ShoppingCartPagerAdapter(Context context, List<ShoppingCart> list, final CheckBox checkbox_all, TextView tv_total_price) {
        this.context = context;
        this.datas = list;
        cartProvider = new CartProvider(context);
        this.checkbox_all = checkbox_all;
        this.tv_total_price = tv_total_price;
        showTotalPrice();
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //1.状态要变化
                ShoppingCart cart = datas.get(position);
                Log.e("TAG", "点击钱"+position+"zhuangtiawei"+cart.isCheck());
                cart.setIsCheck(!cart.isCheck());//状态取反
                Log.e("TAG", "dianjile"+position+"zhuangtiawei"+cart.isCheck());
                notifyItemChanged(position);//刷新状态
                Log.e("TAG", "notifyItemChanged"+position+"zhuangtiawei"+cart.isCheck());

                //保持状态
                cartProvider.update(cart);

                //2.校验是否全选
                checkAll_none();

                //3.计算总价格
                showTotalPrice();
            }
        });
        //校验是否全选
        checkAll_none();
        //设置CheckBox的点击事件
        checkbox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.得到CheckBox的状态和设置全选和非全选
                checkAll_none(checkbox_all.isChecked());

                //2.显示总价格
                showTotalPrice();

            }
        });


    }

    public void checkAll_none() {
        if(datas != null && datas.size() >0){

            int number = 0;
            for (int i=0;i<datas.size();i++){
                ShoppingCart cart = datas.get(i);
                if(!cart.isCheck()){//设置非全选
                    checkbox_all.setChecked(false);
                }else{
                    //选中
                    number++;
                }
            }

            if(number == datas.size()){
                checkbox_all.setChecked(true);
            }
        }
    }
    public void checkAll_none(boolean isCheck) {
        if(datas != null && datas.size() >0){

            for (int i=0;i<datas.size();i++){
                ShoppingCart cart = datas.get(i);
                cart.setIsCheck(isCheck);//设置某条的状态
                notifyItemChanged(i);//刷新适配器
            }

        }
    }

    public void showTotalPrice() {

        tv_total_price.setText("合计￥" + getTotalPrice());
    }

    public float getTotalPrice() {
        float result = 0;
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                ShoppingCart cart = datas.get(i);//购物车类：是否被选中和多少个
                //是否被勾选
                if(cart.isCheck()){
                    //得到价格，并且和之前的相加
//                    result = result + cart.getCount()*cart.getPrice();

                    result += cart.getCount()*cart.getPrice();
                }
            }
        }
        return result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(context, R.layout.item_shopping_cart_pager,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         final ShoppingCart cart = datas.get(position);
        Log.e("QWE", "position="+position);


        //2.绑定数据

        Glide.with(context)
                .load(cart.getImgUrl())
                .centerCrop()
                .placeholder(R.drawable.news_pic_default)
                .crossFade()
                .into(holder.iv_icon);
        holder.tv_name.setText(cart.getName());
        holder.tv_price.setText("￥"+cart.getPrice());
        holder.number_add_sub_view.setValue(cart.getCount());
        holder.checkbox.setChecked(cart.isCheck());
        holder.number_add_sub_view.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonSubClick(View v, int value) {
                //1.把这个值设置一下
                cart.setCount(value);

                //2.保持到内存中和本地
                cartProvider.update(cart);

                //3.显示总价格
                showTotalPrice();


            }

            @Override
            public void onButtonAddClick(View v, int value) {
                //1.把这个值设置一下
                cart.setCount(value);
                Log.e("QWE", "cart"+cart.getName());
                //2.保持到内存中和本地
                cartProvider.update(cart);

                //3.显示总价格
                showTotalPrice();
            }
        });





    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CheckBox checkbox;
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_price;
        private NumberAddSubView number_add_sub_view;
        private Button btn_sub;
        private Button btn_add;



        public ViewHolder(final View itemView) {
            super(itemView);

            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            number_add_sub_view = (NumberAddSubView) itemView.findViewById(R.id.number_add_sub_view);
            btn_sub = (Button)itemView.findViewById(R.id.btn_sub);
            btn_add = (Button)itemView.findViewById(R.id.btn_add);

            number_add_sub_view.setOnValueChangeListener(new NumberAddSubView.OnValueChangeListener() {
                @Override
                public void isEnable(int value, int minValue, int maxValue) {
                    if(value<=minValue) {
                        btn_sub.setEnabled(false);
                        Log.e("QQWW", "++++++++++");
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(v,getLayoutPosition());
                    }
                }
            });

        }
    }


    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    /**
     * 清除数据
     */
    public void clearData() {
        datas.clear();
        notifyItemRangeRemoved(0,datas.size());
    }

    public void deleteData() {
       /* if (datas != null && datas.size() >0 ){
            for (int i=0;i<datas.size();i++){

                ShoppingCart cart = datas.get(i);//删除选中的
                if(cart.isCheck()){
                    datas.remove(cart);
                    cartProvider.deleteDeta(cart);
                    notifyItemRemoved(i);
                    i--;
                }

            }
        }*/
        for (Iterator iterator = datas.iterator(); iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if(cart.isCheck()){
                int i = datas.indexOf(cart);//根据对象查找它在列表中的位置
                //在内存中移除
                iterator.remove();
                //移除本地和缓存的
                cartProvider.deleteDeta(cart);
                //刷新适配器
                notifyItemRemoved(i);
            }
        }
    }

    /**
     * 添加数据
     * @param count
     * @param list
     */
    public void addData(int count, List<ShoppingCart> list) {
        datas.addAll(count,list);
        notifyItemRangeChanged(count, datas.size());
    }

    /**
     * 得到多少条数据
     * @return
     */
    public int getCount() {
        return  datas.size();
    }

    public void addData(List<ShoppingCart> list) {
        addData(0,list);
    }





}
