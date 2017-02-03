package com.chinamobile.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yjj on 2017/1/19.
 */

public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<T> mDatas;
    private int mLayoutId;
    private OnItemClickListener listener;

    public BaseAdapter(Context context, int layoutId){
      this(context,null,layoutId);
    }

    public BaseAdapter(Context context, List<T> datas, int layoutId){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
        this.mLayoutId = layoutId;
    }

    protected abstract void bindData(BaseViewHolder viewHolder,T t);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutId,null,false);
        return new BaseViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = mDatas.get(position);
        bindData(holder,t);
    }

    @Override
    public int getItemCount() {
        if(mDatas==null || mDatas.size()<=0)
            return 0;
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onClick(View view,int position);
    }


    public T getItem(int position) {
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }

    public void clear(){
        int itemCount = mDatas.size();
        mDatas.clear();
        this.notifyItemRangeRemoved(0,itemCount);
    }

    public List<T> getDatas(){
        return mDatas;
    }

    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    public void addData(List<T> datas){
        addData(0,datas);
    }

    public void addData(int position,List<T> datas){
        if (datas !=null && datas.size()>0){
            mDatas.addAll(datas);
            notifyItemRangeChanged(position,mDatas.size());
        }
    }

}
