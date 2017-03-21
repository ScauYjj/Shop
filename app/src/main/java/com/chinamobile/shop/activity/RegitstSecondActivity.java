package com.chinamobile.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.MyApplication;
import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.User;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.chinamobile.shop.msg.LoginRespMsg;
import com.chinamobile.shop.utils.CountTimeView;
import com.chinamobile.shop.utils.DESUtil;
import com.chinamobile.shop.utils.ToastUtils;
import com.chinamobile.shop.widget.ClearEditText;
import com.chinamobile.shop.widget.ShopToolbar;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

public class RegitstSecondActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    ShopToolbar mToolbar;
    @BindView(R.id.txtTip)
    TextView tvTip;
    @BindView(R.id.edittxt_code)
    ClearEditText etCode;
    @BindView(R.id.btn_reSend)
    Button btnReSend;

    private String phone;
    private String pwd;
    private String countryCode;

    private SpotsDialog dialog;

    private CountTimeView countTimerView;

    private OkHttpHelper okHttpHelper  = OkHttpHelper.getInstance();

    private  SMSEvenHanlder evenHanlder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ButterKnife.bind(this);
        initToolbar();
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);



        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        Logger.d(text);
        tvTip.setText(Html.fromHtml(text));



        CountTimeView timerView = new CountTimeView(btnReSend);
        timerView.start();




        SMSSDK.initSDK(this, Constant.MOB_APP_KEY,Constant.MOB_APP_SECRET);

        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this);
    }

    /**
     * 初始化Toollbar
     */
    private void initToolbar() {
        mToolbar.setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mToolbar.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitCode();
            }
        });
    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view){

        SMSSDK.getVerificationCode("+"+countryCode, phone);
        countTimerView = new CountTimeView(btnReSend,R.string.smssdk_resend_identify_code);
        countTimerView.start();

//        dialog.setMessage("正在重新获取验证码");
//        dialog.setTitle("正在重新获取验证码");
        dialog.show();
    }

    /** 分割电话号码 */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }


    private  void submitCode(){

        String vCode = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode,phone,vCode);
        dialog.show();
    }

    private void doReg(){

        Map<String,String> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Constant.DES_KEY, pwd));

        okHttpHelper.post(Constant.API.REG, params, new SportCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, Object o) {
                LoginRespMsg<User> userLoginRespMsg = (LoginRespMsg<User>) o;
                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();

                if(userLoginRespMsg.getStatus()==LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(RegitstSecondActivity.this,"注册失败:"+userLoginRespMsg.getMessage());
                    return;
                }
                MyApplication application =MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegitstSecondActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    class SMSEvenHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {


//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");

//                            ToastUtils.show(RegSecondActivity.this,"验证成功："+phone+",country:"+country);


                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                            ;
                        }
                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }
}
