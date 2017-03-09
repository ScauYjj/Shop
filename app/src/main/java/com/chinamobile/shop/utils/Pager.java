package com.chinamobile.shop.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.GetChars;
import android.view.View;
import android.widget.Toast;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.adapter.BaseAdapter;
import com.chinamobile.shop.adapter.HWAdapter;
import com.chinamobile.shop.bean.Page;
import com.chinamobile.shop.bean.Wares;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by yjj on 2017/2/22.
 */

public class Pager {

    private static Builder builder;

    private OkHttpHelper okHttpHelper;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFRESH =1;
    private  static final int STATE_MORE=2;

    private int state = STATE_NORMAL;

    public static final int GET_DATA_SUCCEESS = 0;
    public static final int GET_DATA_FAILED = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCEESS:
//                    showData();
                    //activity.showSnackBar(mRefreshLayout,"刷新成功");
                    break;
                case GET_DATA_FAILED:
//                    activity.showSnackBar(mRefreshLayout,"加载失败");
                    break;
            }
        }
    };
    private Pager(){
        okHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            //下拉刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                reflashData();
            }

            //下拉加载
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //若当前page <= totalPage 则不再继续加载
                if (builder.pageIndex <= builder.totalPage){
                    loadMore();
                }else {
                    builder.mRefreshLayout.finishRefreshLoadMore();
                    Toast.makeText(builder.mContext,"已加载完毕",Toast.LENGTH_SHORT).show();
                    //activity.showSnackBar(mRefreshLayout,"已加载完毕");
                }
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMore() {
        builder.pageIndex = ++builder.pageIndex;
        state = STATE_MORE;
        requestData();
    }

    /**
     * 刷新数据
     */
    private void reflashData(){
        builder.pageIndex = 1;
        state = STATE_REFRESH;
        requestData();
    }

    public void request(){
        requestData();
    }

    public void putParams(String key,Object value){
        builder.params.put(key,value);
    }


    /**
     * 发送网络请求请求数据
     */
    private void requestData(){

        okHttpHelper.get(buildUrl(), new RequestCallBack(builder.mContext) {

        });
    }

    private String buildUrl(){
        return builder.url + "?"+builder.buildeUrlParams();
    }


    /**
     * 根据不同状态展现数据
     */
    private  <T>void showData(List<T> datas,int totalPage,int totalCount) {
        switch (state){
            case STATE_NORMAL:
                if (builder.onPageListener != null){
                builder.onPageListener.load(datas,totalPage,totalCount);
            }
                break;
            case STATE_REFRESH:
                if (builder.onPageListener != null){
                    builder.onPageListener.reflesh(datas,totalPage,totalCount);
                }
                builder.mRefreshLayout.finishRefreshing();
                break;
            case STATE_MORE:
                if (builder.onPageListener != null){
                    builder.onPageListener.reflesh(datas,totalPage,totalCount);
                }
                builder.mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static class Builder{

        private Context mContext;
        private Type type;
        private String url;
        private MaterialRefreshLayout mRefreshLayout;
        private boolean canLoadMore;
        private OnPageListener onPageListener;

        private int totalPage =1;
        private int pageIndex = 1;
        private int pageSize = 10;

        private HashMap<String , Object> params = new HashMap<>(5);

        public Builder setUrl(String url){
            this.url = url;
            return builder;
        }

        public Builder putParams(String key,Object value){
            params.put(key,value);
            return builder;
        }

        public Builder setPageSize(int pageSize){
            this.pageSize = pageSize;
            return builder;
        }

        public Builder setRefleshLayout(MaterialRefreshLayout refleshLayout){
            this.mRefreshLayout = refleshLayout;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore;
            return builder;
        }

        public Pager builder(Context context, Type type){

            this.mContext = context;

            valida();

            return new Pager();
        }

        public Builder setOnPageListener(OnPageListener onPageListener){
            this.onPageListener = onPageListener;
            return builder;
        }

        private String buildeUrlParams(){

            HashMap<String ,Object> map = builder.params;

            map.put("curPage",builder.pageIndex);
            map.put("pageSize",builder.pageSize);

            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String,Object> entry: map.entrySet()){
                sb.append(entry.getKey() + "'="+entry.getValue()) ;
                sb.append("&");
            }
            String s = sb.toString();
            if (s.endsWith("&")){
                s = s.substring(1,s.length()-1);
            }
            return s;
        }

        private void valida(){
            if (this.mContext == null){
                throw  new RuntimeException("cantent can not be null");
            }

            if (this.url == null || "".equals(this.url)){
                throw  new RuntimeException("cantent can not be null");
            }

            if (this.mRefreshLayout == null){
                throw  new RuntimeException("cantent can not be null");
            }
        }
    }


    public  interface OnPageListener<T>{

        void load(List<T> datas,int totalPage,int totalCount);

        void reflesh (List<T> datas,int totalPage,int totalCount);

        void loadMore(List<T> datas,int totalPage,int totalCount);
    }

    class RequestCallBack<T> extends SportCallback<Page<T>>{

        public RequestCallBack(Context context) {
            super(context);

            super.mType = builder.type;
        }

        @Override
        public void onSuccess(Response response, Object o) {
            Page<T> page = (Page<T>) o;
            builder.pageIndex = page.getCurrentPage();
            builder.totalPage = page.getTotalPage();


            showData(page.getList(),builder.totalPage,page.getTotalCount());

            Message msg =handler.obtainMessage(GET_DATA_SUCCEESS);
            handler.sendMessage(msg);
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Message msg =handler.obtainMessage(GET_DATA_FAILED);
            handler.sendMessage(msg);
        }
    }
}
