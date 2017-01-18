package com.chinamobile.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.chinamobile.shop.R;

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

    /**
     * 跳转页面
     * @param activity
     */
    public void startToActivity(Class activity){
        Intent intent = new Intent();
        intent.setClass(this,activity);
        startActivity(intent);
    }

    /**
     * //判断网络是否已经连接上
     */
    public void checkNet() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        connected = info != null && info.isConnected();
    }
}
