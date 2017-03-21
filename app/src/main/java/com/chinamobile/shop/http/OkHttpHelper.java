package com.chinamobile.shop.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.chinamobile.shop.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yjj on 2017/1/15.
 */

public class OkHttpHelper {

    public static final int TOKEN_MISSING=401;// token 丢失
    public static final int TOKEN_ERROR=402; // token 错误
    public static final int TOKEN_EXPIRE=403; // token 过期

    public static final String TAG="OkHttpHelper";

    private static OkHttpClient okHttpClient;
    private Gson mGson;
    private Handler mHandler;

    private OkHttpHelper(){
        okHttpClient = new OkHttpClient();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance(){
        return new OkHttpHelper();
    }

    /**
     * 发送get请求
     * @param url
     * @param callback
     * @param params
     */
    public void get(String url,Map<String,String> params,BaseCallback callback){
        Request request = buildRequest(url,params,HttpRequestType.GET);
        doRequest(request,callback);
    }

    /**
     * 发送get请求
     * @param url
     * @param callback
     */
    public void get(String url,BaseCallback callback){
       get(url,null,callback);
    }

    /**
     * 发送post请求
     * @param url
     * @param params
     * @param callback
     */
    public void post(String url,Map<String,String> params,BaseCallback callback){
        Request request =  buildRequest(url,params,HttpRequestType.POST);
        doRequest(request,callback);
    }

    /**
     * 构建Requset对象
     * @param url
     * @param params
     * @param type
     * @return
     */
    private Request buildRequest(String url,Map<String,String>params,HttpRequestType type){
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (type == HttpRequestType.GET){
//            url = url + "?token="+MyApplication.getInstance().getToken();
            url = buildUrlParams(url,params);
            builder.url(url);
            builder.get();
        }else if (type == HttpRequestType.POST){
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private String buildUrlParams(String url,Map<String,String>params){
        if (params == null)
            params = new HashMap<>(1);

            String token = MyApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
                params.put("token",token);

                StringBuffer sb = new StringBuffer();
                for (Map.Entry<String,String> entry: params.entrySet()){
                    sb.append(entry.getKey()+"="+entry.getValue());
                    sb.append("&");
                }
                String s = sb.toString();
                if (s.endsWith("&")){
                    s = s.substring(0,s.length() - 1);
                }

                if (url.indexOf("?")>0){
                    url  = url + "&" + s;
                }else {
                    url = url + "?" + s;
                }
            return url;
    }

    /**
     * 构建RequestBody
     * @param params
     * @return
     */
    private RequestBody buildFormData(Map<String ,String> params){

        FormBody.Builder builder = new FormBody.Builder();
        //遍历解析
        if (params != null){
            for (Map.Entry<String ,String> entry: params.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
            String token = MyApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token)){
                builder.add("token",token);
            }
        }
        return builder.build();
    }

    /**
     * 枚举请求的类型
     */
    enum HttpRequestType{
        GET,POST
    }

    /**
     * 发送网络请求
     * @param request
     * @param callback
     */
    public void doRequest(final Request request, final BaseCallback callback){

        callback.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response);
                if (response.isSuccessful()){
                    String resutlStr = response.body().string();
                    //判断如果返回类型是String则不需要Gson转换
                    if (callback.mType == String.class){
                        callback.onSuccess(response,resutlStr);
                    }else {
                       try{
                           Object object = mGson.fromJson(resutlStr,callback.mType);
                           callback.onSuccess(response,object);
                       }catch (JsonParseException e){
                           callback.onError(response,response.code(),e);
                       }
                    }
                }
                else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE || response.code() == TOKEN_MISSING){
                    callbackTokenError(callback,response);
                }
                else{
                    callback.onError(response,response.code(),null);
                }
            }
        });
    }

    private void callbackTokenError(final BaseCallback callback,final Response response){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response,response.code());
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback,final Response response,final Object obj){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response,obj);
            }
        });
    }

    private void callbackError(final  BaseCallback callback , final Response response, final Exception e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }

//    private void callbackFailure(final  BaseCallback callback , final Request request, final IOException e ){
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                callback.onFailure(request,e);
//            }
//        });
//    }


    private void callbackResponse(final  BaseCallback callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }
}
