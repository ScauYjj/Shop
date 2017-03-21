package com.chinamobile.shop.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.MyApplication;
import com.chinamobile.shop.R;
import com.chinamobile.shop.activity.BaseActivity;
import com.chinamobile.shop.activity.LoginActivity;
import com.chinamobile.shop.bean.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * Created by yjj on 2016/10/26.
 */

public class MineFragment extends Fragment {

    private View mView;
    @BindView(R.id.img_head)
    CircleImageView circleImageHead;
    @BindView(R.id.txt_username)
    TextView tvUsername;
    @BindView(R.id.txt_my_orders)
    TextView tvMyOrders;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    /**
     * 点击登陆
     * @param view
     */
    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLogin(View view){
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivityForResult(intent,Constant.REQUEST_CODE);
    }

    /**
     * 用户退出
     * @param view
     */
    @OnClick(R.id.btn_logout)
    public void loginOut(View view){
        MyApplication.getInstance().clearUser();
        showUser(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE && resultCode == RESULT_OK){
            User user = MyApplication.getInstance().getUser();
            showUser(user);
        }
    }

    /**
     * 展示用户信息
     * @param user
     */
    private void showUser(User user){
        if (user != null){
            tvUsername.setText(user.getUsername());
            Picasso.with(getActivity()).load(user.getLogo_url()).into(circleImageHead);
            btnLogout.setVisibility(View.VISIBLE);
            tvUsername.setClickable(false);
            circleImageHead.setClickable(false);
        }else {
            tvUsername.setText(R.string.to_login);
            Picasso.with(getActivity()).load(R.drawable.default_head).into(circleImageHead);
            btnLogout.setVisibility(View.GONE);
            tvUsername.setClickable(true);
            circleImageHead.setClickable(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }
}
