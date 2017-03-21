package com.chinamobile.shop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.TabHost;

import com.chinamobile.shop.bean.User;
import com.chinamobile.shop.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.smssdk.SMSSDK;

/**
 * Created by yjj on 2017/1/13.
 */

public class MyApplication extends Application{

    private User user;

    /**
     *单例模式
     */
    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Fresco.initialize(this);
        initUser();
    }

    private void  initUser(){
        this.user = UserLocalData.getUser(this);
    }

    public User getUser(){
        return user;
    }

    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }

    private  Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent(){
       return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }
}
