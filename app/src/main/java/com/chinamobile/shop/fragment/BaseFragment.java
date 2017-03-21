package com.chinamobile.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.shop.MyApplication;
import com.chinamobile.shop.activity.LoginActivity;
import com.chinamobile.shop.bean.User;

import butterknife.ButterKnife;

/**
 * Created by yjj on 2017/3/21.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater,container,savedInstanceState);
        ButterKnife.bind(this,view);
        initToolBar();
        init();
        return view;
    }

    public void  initToolBar(){

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

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
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else {
            super.startActivity(intent);
        }
    }
}
