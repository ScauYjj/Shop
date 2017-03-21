package com.chinamobile.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.R;
import com.chinamobile.shop.activity.NewOrderActivity;
import com.chinamobile.shop.adapter.ShoppingCartAdapter;
import com.chinamobile.shop.adapter.WrapContentLinearLayoutManager;
import com.chinamobile.shop.adapter.decoration.DividerItemDecoration;
import com.chinamobile.shop.bean.ShoppingCart;
import com.chinamobile.shop.bean.User;
import com.chinamobile.shop.http.OkHttpHelper;
import com.chinamobile.shop.http.SportCallback;
import com.chinamobile.shop.utils.CartProvider;
import com.chinamobile.shop.utils.ToastUtils;
import com.chinamobile.shop.widget.ShopToolbar;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by yjj on 2016/10/26.
 */

public class CartFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private ShopToolbar mToolbar;
    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;
    private Button mBtnOrder;
    private Button mBtnDel;
    private ShoppingCartAdapter mAdapter;
    private CartProvider cartProvider;
    private MaterialRefreshLayout materialRefreshLayout;

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_COMPLATED = 2;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart,container,false);
        return mView;
    }

    @Override
    public void init() {
        initView();
        showData();
    }

    private void initView() {
        cartProvider = CartProvider.getmInstance(getContext());
        mToolbar = (ShopToolbar) mView.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_all);
        mTextTotal = (TextView) mView.findViewById(R.id.txt_total);
        mBtnOrder = (Button) mView.findViewById(R.id.btn_order);
        mBtnDel = (Button) mView.findViewById(R.id.btn_del);

        mToolbar.setRightText(getString(R.string.edit));
        mToolbar.getRightText().setOnClickListener(this);
        mToolbar.getRightText().setTag(ACTION_EDIT);

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.delCart();
            }
        });

        materialRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.cart_swipe_reflesh_layout);

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refData();
            }
        });

    }

    /**
     * 展示购物车数据
     */
    private void showData() {
        List<ShoppingCart> mDatas = cartProvider.getAll();
        mAdapter = new ShoppingCartAdapter(getContext(), mDatas, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 刷新购物车数据
     */
    public void refData() {
        mAdapter.clear();
        List<ShoppingCart> mDatas = cartProvider.getAll();
        mAdapter.addData(mDatas);
        mAdapter.showTotalPrice();
        materialRefreshLayout.finishRefreshing();
    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();
        if (ACTION_EDIT == action) {
            showDelController();
        } else if (ACTION_COMPLATED == action) {
            hideDelController();
        }
    }

    private void showDelController() {
        mToolbar.setRightText(getString(R.string.completed));
        mBtnDel.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.GONE);
        mTextTotal.setVisibility(View.GONE);
        mToolbar.getRightText().setTag(ACTION_COMPLATED);

        mAdapter.checkAllOrNull(false);
        mCheckBox.setChecked(false);
    }

    private void hideDelController() {
        mToolbar.setRightText(getString(R.string.edit));
        mBtnDel.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mTextTotal.setVisibility(View.VISIBLE);
        mToolbar.getRightText().setTag(ACTION_EDIT);

        mAdapter.checkAllOrNull(true);
        mCheckBox.setChecked(true);
        mAdapter.showTotalPrice();
    }

    /**
     * 去结算
     */
    @OnClick(R.id.btn_order)
    public void toOrder(View view){
        ToastUtils.show(getActivity(),"请先登录");
        Intent intent = new Intent(getActivity(), NewOrderActivity.class);
        startActivity(intent,true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refData();
    }
}
