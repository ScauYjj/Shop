package com.chinamobile.shop.http;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
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

    private static OkHttpClient okHttpClient;
    private Gson mGson;

    private OkHttpHelper(){
        okHttpClient = new OkHttpClient();
        mGson = new Gson();
    }

    public static OkHttpHelper getInstance(){
        return new OkHttpHelper();
    }

    /**
     * 发送get请求
     * @param url
     * @param callback
     */
    public void get(String url,BaseCallback callback){
        Request request = buildRequest(url,null,HttpRequestType.GET);
        doRequest(request,callback);
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
            builder.get();
        }else if (type == HttpRequestType.POST){
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
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
                }else {
                    callback.onError(response,response.code(),null);
                }
            }
        });
    }
}
