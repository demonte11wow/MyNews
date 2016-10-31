package com.example.hasee.mynews.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.example.hasee.mynews.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ActivityTransitionToActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_transition_to);


        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Uri uri = getIntent().getData();

        Picasso.with(this)
                .load(uri)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
//
//                        attacher.cleanup();//保持原样
                        attacher.update();//更新成原生状态
                    }

                    @Override
                    public void onError() {
                    }
                });
    }
}
