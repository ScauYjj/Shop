package com.chinamobile.shop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinamobile.shop.R;
import com.chinamobile.shop.adapter.DividerItemDecortion;
import com.chinamobile.shop.adapter.HomeCatgoryAdapter;
import com.chinamobile.shop.bean.Banner;
import com.chinamobile.shop.bean.Campaign;
import com.chinamobile.shop.bean.HomeCampaign;
import com.chinamobile.shop.http.BaseCallback;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.chinamobile.shop.Constant;
import com.chinamobile.shop.widget.ShopRecyclerView;
import com.chinamobile.shop.widget.ShopToolbar;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private View view;
    private ShopToolbar mToolbar;
    private SliderLayout mSlider;
    private PagerIndicator indicator;
    private ShopRecyclerView mRecyclerView;
    private MaterialRefreshLayout mRefreshLayout;

    private HomeCatgoryAdapter mAdapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private List<Banner> mBanner;
    private List<HomeCampaign> mHomeCategoris;

    private final static int UP_DATE_IMAGES_SUCCESS = 0;
    private final static int UP_DATE_IMAGES_FAILED =1;
    private final static int UP_DATE_HOME_CAMPAGIN = 2;
    private final static int REQUEST_FAILED =3;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UP_DATE_IMAGES_SUCCESS:
                    initSlider();
                    break;
                case UP_DATE_IMAGES_FAILED:
                    Toast.makeText(getContext(),"广告加载失败",Toast.LENGTH_SHORT).show();
                    break;
                case UP_DATE_HOME_CAMPAGIN:
                    initReycyclerView();
                    mRefreshLayout.finishRefreshing();
                    break;
                case REQUEST_FAILED:
                    Toast.makeText(getContext(),"网络请求失败",Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshing();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        requestImage();
        initSlider();
        requestData();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initView() {
        mToolbar = (ShopToolbar) view.findViewById(R.id.toolbar);
        mRecyclerView = (ShopRecyclerView) view.findViewById(R.id.recycler);
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                requestImage();
                requestData();
            }
        });

        indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        mSlider = (SliderLayout) view.findViewById(R.id.slider);

        //自定义Indicator
        mSlider.setCustomIndicator(indicator);

        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mSlider.setDuration(3000);
    }

    /**
     * 请求HomeCampaign数据
     */
    private void requestData() {
        okHttpHelper.get(Constant.API.HOME_COMPAGIN_URL, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                mHomeCategoris = homeCampaigns;
                Message msg = handler.obtainMessage(UP_DATE_HOME_CAMPAGIN);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage(REQUEST_FAILED);
                handler.sendMessage(msg);
            }

            @Override
            public void onTokenError(Response response, int code) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }
        });



    }

    private void initReycyclerView(){

        if (mAdapter == null){
            mAdapter = new HomeCatgoryAdapter(mHomeCategoris,getActivity());

            mAdapter.setOnCampaignCllickListener(new HomeCatgoryAdapter.OnCampaignCllickListener() {
                @Override
                public void onClick(View view, Campaign campaign) {
                    Toast.makeText(getActivity(),""+campaign.getTitle(),Toast.LENGTH_SHORT).show();
                }
            });

            mRecyclerView.setAdapter(mAdapter);

            //设置分割线
            mRecyclerView.addItemDecoration(new DividerItemDecortion());

            //禁止RecyclerView上下滚动
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }else {
            mAdapter.clearData();
            mAdapter.addData(mHomeCategoris);
            mAdapter.notifyItemRangeChanged(0,mAdapter.getDatas().size());
        }

    }

    /**
     * 网络请求广告条
     */
    private void requestImage(){
        String url = Constant.API.BANNER+"?type=1";
        okHttpHelper.get(url, new SportCallback<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, Object o) {
                if (mBanner != null){
                    mBanner.clear();
                }
                mBanner = (List<Banner>) o;
                Message msg  = handler.obtainMessage(UP_DATE_IMAGES_SUCCESS);
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Message msg  = handler.obtainMessage(UP_DATE_IMAGES_FAILED);
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 初始化slider
     */
    private void initSlider() {
        mSlider.removeAllSliders();
        if (mBanner != null) {
            for (Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSlider.addSlider(textSliderView);
            }
            //设置默认的Indicator
            //  mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

            /*mSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });*/
        }
    }

    @Override
    public void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }
}
