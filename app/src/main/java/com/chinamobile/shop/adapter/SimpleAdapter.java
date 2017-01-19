package com.chinamobile.shop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by yjj on 2017/1/19.
 */

public abstract class SimpleAdapter<T>extends BaseAdapter<T, BaseViewHolder>{

    public SimpleAdapter(Context context, List<T> datas, int layoutId) {
        super(context, datas, layoutId);
    }

}
