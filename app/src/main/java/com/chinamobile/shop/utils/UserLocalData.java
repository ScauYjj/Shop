package com.chinamobile.shop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.bean.User;

/**
 * Created by yjj on 2017/3/20.
 */

public class UserLocalData {

    public static void putUser(Context context, User user){
        String user_json = JSONUtil.toJSON(user);
        PreferencesUtils.putString(context, Constant.USER_JSON,user_json);
    }

    public static void putToken(Context context,String token){
        PreferencesUtils.putString(context,Constant.TOKEN,token);
    }

    public static User getUser(Context context){
        String user_json = PreferencesUtils.getString(context,Constant.USER_JSON);
        if (!TextUtils.isEmpty(user_json)){
            return JSONUtil.fromJson(user_json,User.class);
        }
        return null;
    }

    public static String getToken(Context context){
        return PreferencesUtils.getString(context,Constant.TOKEN);
    }

    public static void clearUser(Context context){


        PreferencesUtils.putString(context, Constant.USER_JSON,"");

    }

    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Constant.TOKEN,"");
    }

}
