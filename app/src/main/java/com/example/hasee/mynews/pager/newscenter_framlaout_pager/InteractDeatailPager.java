package com.example.hasee.mynews.pager.newscenter_framlaout_pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.hasee.mynews.R;
import com.example.hasee.mynews.base.BaseLeftMeauDetailsPager;
import com.example.hasee.mynews.bean.NewsCenterPagerBean2;
import com.example.hasee.mynews.bean.PhotosDetailPagerBean;
import com.example.hasee.mynews.utils.BitmapCacheUtils;
import com.example.hasee.mynews.utils.CacheUtils;
import com.example.hasee.mynews.utils.Constants;
import com.example.hasee.mynews.utils.NetCacheUtils;
import com.example.hasee.mynews.volley.VolleyManager;
import com.example.refresh_library.LogUtil;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by lzq on 2016/10/17.
 */
public class InteractDeatailPager extends BaseLeftMeauDetailsPager {

    private final NewsCenterPagerBean2.NewsCenterPagerData newsCenterPagerData;
    @ViewInject(R.id.listview)
    private ListView listview;
    @ViewInject(R.id.gridview)
    private GridView gridview;
    private String photosUrl;
    private List<PhotosDetailPagerBean.DataEntity.NewsEntity> news;
    /**
     * 三级缓存工具类
     */
    private BitmapCacheUtils bitmapCacheUtils;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS:
                    int position = msg.arg1;
                    LogUtil.e("请求图片成功==" + position);
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (listview != null && listview.isShown()) {
                        ImageView imageView = (ImageView) listview.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }

                    }

                    break;
                case NetCacheUtils.FAIL:
                    position = msg.arg1;
                    LogUtil.e("请求图片失败==" + position);
                    break;
            }
        }
    };


    public InteractDeatailPager(Context context, NewsCenterPagerBean2.NewsCenterPagerData newsCenterPagerData) {
        super(context);
        this.newsCenterPagerData = newsCenterPagerData;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photo_detail_pagter, null);
        x.view().inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        photosUrl = Constants.BASE_URL + newsCenterPagerData.getUrl();

        String saveJson = CacheUtils.getString(context, photosUrl);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        StringRequest stringQues = new StringRequest(Request.Method.GET, photosUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("互动请求数据成功==" + s);
                //解析json数据
                CacheUtils.putString(context, photosUrl, s);
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("互动请求数据失败==" + volleyError.getMessage());
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                    parsed = new String(response.data);
                }

                return super.parseNetworkResponse(response);
            }
        };
        VolleyManager.addRequest(stringQues, "photos");
    }

    private void processData(String json) {

        PhotosDetailPagerBean bean = new Gson().fromJson(json, PhotosDetailPagerBean.class);
        news = bean.getData().getNews();
        if (news != null && news.size() > 0) {

            //设置适配器
            listview.setAdapter(new PhotosDetailPagerAdapter());
        }
    }

    class PhotosDetailPagerAdapter extends BaseAdapter {


      /*  private DisplayImageOptions options;

        public PhotosDetailPagerAdapter(){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.pic_item_list_default)
                    .showImageForEmptyUri(R.drawable.pic_item_list_default)
                    .showImageOnFail(R.drawable.pic_item_list_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(20))//矩形圆角图片
                    .build();
        }*/

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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_photos_detail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            PhotosDetailPagerBean.DataEntity.NewsEntity item = news.get(position);
            viewHolder.tv_title.setText(item.getTitle());

           /* //1.自定义方式三级缓存方式请求图片
            viewHolder.iv_icon.setTag(position);
//            loaderImager(viewHolder,Constants.BASE_URL+item.getListimage());
            Bitmap bitmap = bitmapCacheUtils.getBitmap(Constants.BASE_URL + item.getListimage(), position);
            if (bitmap != null) {//来自内存或者本地
                viewHolder.iv_icon.setImageBitmap(bitmap);
            }
*/
            //2.使用picasso请求图片


            /*Picasso.with(context)
                   .load(Constants.BASE_URL + item.getListimage())
                   .placeholder(R.drawable.pic_item_list_default)
                  .error(R.drawable.pic_item_list_default)
                    .into(viewHolder.iv_icon);*/

            //3、使用Glide请求图片
            Glide
                    .with(context)
                    .load(Constants.BASE_URL + item.getListimage())
                    .centerCrop()
                    .placeholder(R.drawable.pic_item_list_default)
                    .crossFade()
                    .into(viewHolder.iv_icon);

            //4.使用ImageLoader加载网络图片
            //com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(Constants.BASE_URL + item.getListimage(), viewHolder.iv_icon, options);


            return convertView;
        }
    }

    /**
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    /**
     * true:显示ListView,但隐藏GridView
     * false:显示GridView，但隐藏ListView
     */
    private boolean isListview = true;

    public void swicheListAndGrid(ImageButton ib_swich_list_grid) {
        if (isListview) {
            isListview = false;
            //显示GridView
            gridview.setVisibility(View.VISIBLE);
            gridview.setAdapter(new PhotosDetailPagerAdapter());
            //隐藏ListView
            listview.setVisibility(View.GONE);

            //按钮状态 =ListView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            isListview = true;
            //显示ListView
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(new PhotosDetailPagerAdapter());
            //隐藏GridView
            gridview.setVisibility(View.GONE);

            //按钮显示-GridView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }

    }
}
