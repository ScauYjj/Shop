package com.chinamobile.shop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinamobile.shop.R;
import com.chinamobile.shop.activity.MainActivity;
import com.chinamobile.shop.adapter.HotWaresAdapter;
import com.chinamobile.shop.bean.Page;
import com.chinamobile.shop.bean.Wares;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.chinamobile.shop.widget.Constant;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.io.Serializable;
import java.util.List;

import okhttp3.Response;


/**
 * Created by yjj on 2016/10/26.
 */

public class HotFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout mRefreshLayout;
    private MainActivity activity;
/*    @BindView(R.id.drawee_view)
    SimpleDraweeView mDraweeView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;*/

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private List<Wares>mDatas;
    private HotWaresAdapter mAdapter;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFRESH =1;
    private  static final int STATE_MORE=2;

    private int state = STATE_NORMAL;

    private int curPage =1;
    private int pageSize =10;
    private int totalPage =1;

    public static final int GET_DATA_SUCCEESS = 0;
    public static final int GET_DATA_FAILED = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCEESS:
                    showData();
                    //activity.showSnackBar(mRefreshLayout,"刷新成功");
                    break;
                case GET_DATA_FAILED:
                    activity.showSnackBar(mRefreshLayout,"加载失败");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hot,container,false);
        //ButterKnife.bind(this.getActivity());
        activity = (MainActivity) getActivity();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler);
        mRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.refresh);
        getData();
        initRefreshLayout();
        return  mView;
    }

    /**
     * 发送网络请求请求数据
     */
    private void getData(){
        String url = Constant.API.HOT_WARES_URL +"?curPage="+curPage+"&pageSize="+pageSize;
        okHttpHelper.get(url, new SportCallback<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Object o) {
                Page<Wares> waresPage = (Page<Wares>) o;
                mDatas = waresPage.getList();
                curPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                handler.sendEmptyMessage(GET_DATA_SUCCEESS);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                handler.sendEmptyMessage(GET_DATA_FAILED);
            }
        });
    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            //下拉刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                reflashData();
            }

            //下拉加载
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //若当前page <= totalPage 则不再继续加载
                if (curPage <= totalPage){
                    loadMore();
                }else {
                    mRefreshLayout.finishRefreshLoadMore();
                    activity.showSnackBar(mRefreshLayout,"已加载完毕");
                }
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMore() {
        curPage = ++curPage;
        state = STATE_MORE;
        getData();
    }

    /**
     * 刷新数据
     */
    private void reflashData(){
        curPage = 1;
        state = STATE_REFRESH;
        getData();
    }


    /**
     * 根据不同状态展现数据
     */
    private void showData() {
        switch (state){
            case STATE_NORMAL:
                mAdapter = new HotWaresAdapter(mDatas,getContext());
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                break;
            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.addData(mDatas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefreshing();
                break;
            case STATE_MORE:
                int position = mAdapter.getDatas().size();
                mAdapter.addData(position,mDatas);
                mRecyclerView.scrollToPosition(position);
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }
}
