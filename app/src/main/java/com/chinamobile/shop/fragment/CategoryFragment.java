package com.chinamobile.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.shop.R;
import com.chinamobile.shop.http.OkHttpHelper;


/**
 * Created by yjj on 2016/10/26.
 */

public class CategoryFragment extends Fragment {

    private View mView;

    private OkHttpHelper okHttpHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category,container,false);
        return  mView;
    }

    private void requestCategoryData(){

    }
}
