package com.example.hasee.mynews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.hasee.mynews.activity.GuideActivity;
import com.example.hasee.mynews.activity.MainActivity;
import com.example.hasee.mynews.utils.CacheUtils;
import com.example.hasee.mynews.utils.LogUtil;

public class SplashActivity extends Activity {

    private RelativeLayout rl_splahs_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splahs_root = (RelativeLayout) findViewById(R.id.rl_splahs_root);

        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(1500);
        aa.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1500);
        scaleAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(aa);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);

        rl_splahs_root.startAnimation(animationSet);


        animationSet.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            boolean startMain = CacheUtils.getBoolean(SplashActivity.this,"atguigu");
            LogUtil.e(startMain+"");
            if(startMain) {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                CacheUtils.putBoolean(SplashActivity.this,"atguigu",true);
                Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();

            }


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
