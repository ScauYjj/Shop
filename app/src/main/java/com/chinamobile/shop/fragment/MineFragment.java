package com.chinamobile.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinamobile.shop.R;
import com.chinamobile.shop.activity.SecondActivity;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by yjj on 2016/10/26.
 */

public class MineFragment extends Fragment {

    private CircleImageView circleImageView;

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine,container,false);
        circleImageView = (CircleImageView) mView.findViewById(R.id.img_head);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),SecondActivity.class));
            }
        });
        return  mView;
    }
}
