package com.chinamobile.shop.http;

import android.content.Context;
import android.widget.Toast;

import com.chinamobile.shop.activity.MainActivity;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by yjj on 2017/1/16.
 */

public abstract class SportCallback<T> extends BaseCallback{

    private SpotsDialog dialog;
    private Context mContext;

    public SportCallback(Context context){
        dialog = new SpotsDialog(context,"拼命加载中...");
        this.mContext = context;
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    public void setLoadMessage(int resId){
        dialog.setMessage(mContext.getString(resId));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        hideDialog();
    }

    @Override
    public void onResponse(Response response) {
        hideDialog();
    }

    public void showDialog(){
        dialog.show();
    }

    public void hideDialog(){
        if (dialog != null){
            dialog.dismiss();
        }
    }
    private void setMessage(String str){
        dialog.setMessage(str);
    }
}
