package com.chinamobile.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.MyApplication;
import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.User;
import com.chinamobile.shop.http.BaseCallback;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.chinamobile.shop.msg.LoginRespMsg;
import com.chinamobile.shop.utils.DESUtil;
import com.chinamobile.shop.utils.ToastUtils;
import com.chinamobile.shop.widget.ShopToolbar;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_toolbar)
    ShopToolbar mToolbar;
    @BindView(R.id.login_progress)
    ProgressBar progress;
    @BindView(R.id.lgoin_name)
    AutoCompleteTextView editName;
    @BindView(R.id.login_password)
    EditText editPassword;
    @BindView(R.id.text_regist)
    TextView tvRegist;
    @BindView(R.id.text_find_password)
    TextView tvFindPassword;
    @BindView(R.id.button_login)
    Button btnLogin;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @OnClick(R.id.text_find_password)
    public void toRegist(View view){
        startActivity(new Intent(this,RegistActivity.class));
    }

    @OnClick(R.id.button_login)
    public void login(View view){
        String name = editName.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            ToastUtils.show(this,"请输入手机号");
            return;
        }

        String password = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)){
            ToastUtils.show(this,"请输入密码");
            return;
        }

        Map<String,String> params = new HashMap<>(2);
        params.put("phone",name);
        params.put("password", DESUtil.encode(Constant.DES_KEY,password));
        okHttpHelper.post(Constant.API.LOGIN, params, new SportCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, Object o) {
                LoginRespMsg<User> userLoginRespMsg = (LoginRespMsg<User>) o;
                if (userLoginRespMsg.getMessage().equals("账户或者密码错误")){
                    showSnackBar(btnLogin,"账户或者密码错误");
//                    ToastUtils.show(LoginActivity.this,"账户或者密码错误");
                }else {
                    MyApplication application = (MyApplication) getApplication();
                    application.putUser(userLoginRespMsg.getData(),userLoginRespMsg.getToken());
                    if (application.getIntent() == null){
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        application.jumpToTargetActivity(LoginActivity.this);
                        finish();
                    }

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

