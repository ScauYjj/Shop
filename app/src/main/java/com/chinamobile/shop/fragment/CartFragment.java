package com.chinamobile.shop.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chinamobile.shop.R;
import com.chinamobile.shop.adapter.ShoppingCartAdapter;
import com.chinamobile.shop.adapter.decoration.DividerItemDecoration;
import com.chinamobile.shop.bean.ShoppingCart;
import com.chinamobile.shop.utils.CartProvider;
import com.chinamobile.shop.widget.ShopToolbar;

import java.util.List;

/**
 * Created by yjj on 2016/10/26.
 */

public class CartFragment extends Fragment {

    private View mView;
    private ShopToolbar mToolbar;
    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;
    private Button mBtnOrder;
    private Button mBtnDel;
    private ShoppingCartAdapter mAdapter;
    private CartProvider cartProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart,container,false);
        initView();
        showData();
        return  mView;
    }

    private void initView() {
        cartProvider = new CartProvider(getContext());
        mToolbar = (ShopToolbar) mView.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_all);
        mTextTotal = (TextView) mView.findViewById(R.id.txt_total);
        mBtnOrder = (Button) mView.findViewById(R.id.btn_order);
        mBtnDel = (Button) mView.findViewById(R.id.btn_del);
    }

    /**
     * 展示购物车数据
     */
    private void showData(){
        List<ShoppingCart> mDatas = cartProvider.getAll();
        mAdapter = new ShoppingCartAdapter(getContext(),mDatas,mCheckBox,mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 刷新购物车数据
     */
    public void refData(){
        mAdapter.clear();
        List<ShoppingCart> mDatas = cartProvider.getAll();
        mAdapter.addData(mDatas);
        mAdapter.showTotalPrice();
    }
}
