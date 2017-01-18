package com.chinamobile.shop;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yjj on 2017/1/13.
 */

public class MyApplication extends Application{

    /**
     *单例模式
     */
    private static MyApplication mInstance = new MyApplication();

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
