package com.chinamobile.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yjj on 2017/1/19.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> mViews;
    private BaseAdapter.OnItemClickListener mListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        this.mListener = listener;
        mViews = new SparseArray<>();
        itemView.setOnClickListener(this);
    }

    public View getView(int id){
        return findView(id);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public ImageView getImageVeiw(int id){
        return findView(id);
    }

    public Button getButton(int id){
        return findView(id);
    }

    private <T extends View> T findView(int id){
        View view = mViews.get(id);
        if (mViews != null){
            view = itemView.findViewById(id);
            mViews.put(id,view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null){
            mListener.onClick(view,getLayoutPosition());
        }
    }
}
