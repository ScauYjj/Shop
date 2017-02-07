package com.chinamobile.shop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.R;
import com.chinamobile.shop.adapter.BaseAdapter;
import com.chinamobile.shop.adapter.CategoryAdapter;
import com.chinamobile.shop.adapter.WaresAdapter;
import com.chinamobile.shop.adapter.WrapContentGridLayoutManager;
import com.chinamobile.shop.adapter.decoration.DividerGridItemDecoration;
import com.chinamobile.shop.adapter.decoration.DividerItemDecoration;
import com.chinamobile.shop.bean.Banner;
import com.chinamobile.shop.bean.Category;
import com.chinamobile.shop.bean.Page;
import com.chinamobile.shop.bean.Wares;
import com.chinamobile.shop.http.BaseCallback;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by yjj on 2016/10/26.
 */

public class CategoryFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerImage;
    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerWare;
    private SliderLayout mSliderLayout;
   // private PagerIndicator mIndicator;
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private List<Category> categories;
    private List<Banner> mBanner;
    private List<Wares>mDatas;
    private long categoryId;
    private int curPage = 1;
    private int pageSize = 10;
    private int totalCount = 1;
    private int totalPage = 1;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFRESH =1;
    private  static final int STATE_MORE=2;

    private int state = STATE_NORMAL;

    /**
     * 获取category成功
     */
    public static final int GET_CATEGORY_SUCCEESS = 0;

    /**
     * 获取category失败
     */
    public static final int GET_CATEGORY_FAILED = 1;

    /**
     * 获取广告条成功
     */
    private static final int REQUEST_IMAGES_SUCCESS = 2;

    /**
     * 获取广告条失败
     */
    private static final int REQUEST_IMAGES_FAILED = 3;

    /**
     * 获取Wares成功
     */
    public static final int GET_WARES_SUCCEESS = 4;

    /**
     * 获取Wares失败
     */
    public static final int GET_WARES_FAILED = 5;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_CATEGORY_SUCCEESS:
                    showCategoryData();
                    break;
                case REQUEST_IMAGES_SUCCESS:
                    showSliderViews();
                    break;
                case GET_WARES_SUCCEESS:
                    showWaresData();
                    break;
                case GET_WARES_FAILED:

                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category,container,false);
        initView();
        requestBannserData();
        requestCategoryData();
        return  mView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRecyclerImage = (RecyclerView) mView.findViewById(R.id.recyclerview_category);
        mRecyclerWare = (RecyclerView) mView.findViewById(R.id.recyclerview_wares);
        mSliderLayout = (SliderLayout) mView.findViewById(R.id.slider);
        mRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.refresh_layout);
        //mIndicator = (PagerIndicator) mView.findViewById(R.id.custom_indicator);

        //默认的Indicator
         mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        //自定义Indicator
        //mSliderLayout.setCustomIndicator(mIndicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Foreground2Background);
        mSliderLayout.setDuration(3000);

        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                reflashData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage <= totalPage){
                    loadMore();
                }else {
                    mRefreshLayout.finishRefreshLoadMore();
                    Toast.makeText(getContext(),"已加载完毕",Toast.LENGTH_SHORT).show();
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
        requestWaresData(categoryId);
    }

    /**
     * 刷新数据
     */
    private void reflashData(){
        curPage = 1;
        state = STATE_REFRESH;
        requestWaresData(categoryId);
    }

    /**
     * 网络请求广告条
     */
    private void requestBannserData(){
        String url = Constant.API.BANNER+"?type=1";
        mHttpHelper.get(url, new SportCallback<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, Object o) {
                if (mBanner != null){
                    mBanner.clear();
                }
                mBanner = (List<Banner>) o;
                Message msg  = handler.obtainMessage(REQUEST_IMAGES_SUCCESS);
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Message msg  = handler.obtainMessage(REQUEST_IMAGES_FAILED);
                handler.sendMessage(msg);
            }
        });
    }

    /**
     *网络请求分类的数据
     */
    private void requestCategoryData(){
        mHttpHelper.get(Constant.API.CATEGORY_LIST, new SportCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, Object o) {
                categories = (List<Category>) o;
                //1.第一种写法
                /*Message msg = Message.obtain();
                msg.what = GET_CATEGORY_SUCCEESS;*/

                //2.第二种写法
                Message msg = handler.obtainMessage(GET_CATEGORY_SUCCEESS);
                handler.sendMessage(msg);

                if(categories !=null && categories.size()>0){
                    categoryId = categories.get(0).getId();
                    state = STATE_NORMAL;
                    requestWaresData(categoryId);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 网络请求内容
     */
    private void requestWaresData(long categoryId) {

        String url = Constant.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+curPage+"&pageSize="+pageSize;

        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                mDatas = waresPage.getList();
                curPage = waresPage.getCurrentPage();
                totalCount= waresPage.getTotalCount();
                totalPage = totalCount/pageSize + (totalCount % pageSize ==0 ?0 : 1);
                Message msg = handler.obtainMessage(GET_WARES_SUCCEESS);
                handler.sendMessage(msg);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    /**
     * 显示分类
     */
    private void showCategoryData(){
        mCategoryAdapter = new CategoryAdapter(getContext(),categories);
        mRecyclerImage.setAdapter(mCategoryAdapter);
        mRecyclerImage.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerImage.setItemAnimator(new DefaultItemAnimator());
        mRecyclerImage.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                categoryId = category.getId();
                curPage = 1;
                state = STATE_NORMAL;
                requestWaresData(categoryId);
            }
        });
    }

    /**
     * slider显示广告
     */
    private void showSliderViews() {
        mSliderLayout.removeAllSliders();
        if (mBanner != null) {
            for (Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }
    }

    /**
     * 显示Wares
     */
    private void showWaresData() {
        switch (state){
            case STATE_NORMAL:
              if (mWaresAdapter == null){
                  mWaresAdapter = new WaresAdapter(getContext(),mDatas);
                  mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                      @Override
                      public void onClick(View view, int position) {
                          Toast.makeText(getContext(),""+mDatas.get(position).getName().toString(),Toast.LENGTH_SHORT).show();
                      }
                  });
                  mRecyclerWare.setAdapter(mWaresAdapter);
                  mRecyclerWare.setLayoutManager(new WrapContentGridLayoutManager(getContext(),2));
                  mRecyclerWare.setItemAnimator(new DefaultItemAnimator());
                  mRecyclerWare.addItemDecoration(new DividerGridItemDecoration(getContext()));
              }else {
                  mWaresAdapter.clearData();
                  mWaresAdapter.addData(mDatas);
                  mRecyclerWare.scrollToPosition(0);
              }
                break;
            case STATE_REFRESH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(mDatas);
                mRecyclerWare.scrollToPosition(0);
                mRefreshLayout.finishRefreshing();
                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                break;
            case STATE_MORE:
                int position = mWaresAdapter.getDatas().size();
                mWaresAdapter.addData(position,mDatas);
                //将请求网络返回的数据清空，再将所有数据添加并显示
                mDatas.clear();
                mDatas.addAll(mWaresAdapter.getDatas());
                mRecyclerWare.scrollToPosition(position);
                mRefreshLayout.finishRefreshLoadMore();
                break;
            default:
                break;
        }

    }

}
