package com.chinamobile.shop.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.chinamobile.shop.R;

public class LunchActivity extends BaseActivity {

    private ImageView imgLunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_lunch);
        imgLunch = (ImageView) findViewById(R.id.img_lunch);
        startAnimation(imgLunch);
    }

    private void startAnimation(View view){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgLunch,"scaleX",1f,1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgLunch,"scaleY",1f,1.1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imgLunch,"alpha",0f,1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator).with(scaleX).with(scaleY);
        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //1.判断是否登录

                //2.需要跳转
                startToActivity(MainActivity.class);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
