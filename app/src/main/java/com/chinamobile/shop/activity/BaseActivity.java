package com.chinamobile.shop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chinamobile.shop.MyApplication;
import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.User;
import com.chinamobile.shop.utils.Activitycollector;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by yjj on 2017/1/13.
 */

public class BaseActivity extends AppCompatActivity{

    private boolean connected = false;//判断网络是否已经连接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 显示SnackBar
     * @param view
     * @param text
     */
    public void showSnackBar(View view,@Nullable String text){
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示SnackBar
     * @param view
     * @param resId
     */
    public void showSnackBar(View view,int resId){
        Snackbar.make(view,resId,Snackbar.LENGTH_SHORT).show();
    }

//    /**
//     * 跳转页面
//     * @param activity
//     */
//    public void startToActivity(Class activity){
//        Intent intent = new Intent();
//        intent.setClass(this,activity);
//        startActivity(intent);
//    }

    /**
     * 判断网络是否已经连接上
     */
    public void checkNet() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        connected = info != null && info.isConnected();
    }


    public void startToActivity(Class activity) {
        Intent intent = new Intent();
        intent.setClass(this,activity);
        startActivity(intent);
        //设置过场动画
        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public void finish() {
        super.finish();
        //设置过场动画
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 判断跳转的页面是否需要登陆
     * @param intent
     * @param isNeedLogin
     */
    public void startActivity(Intent intent,boolean isNeedLogin){
        if (isNeedLogin){
            User user = MyApplication.getInstance().getUser();
            if (user != null){
                super.startActivity(intent);
            }else {
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else {
            super.startActivity(intent);
        }
    }
}
