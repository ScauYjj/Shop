package com.chinamobile.shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Page;
import com.chinamobile.shop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yjj on 2017/1/17.
 */

public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder>{

    private List<Wares> mDatas;

    private Context mContext;
    private LayoutInflater mInflater;

    public HotWaresAdapter(List<Wares> wares, Context context){
        mContext = context;
        mDatas = wares;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.template_hot_wares,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wares wares = mDatas.get(position);

        /**
         * Uri 与 String的区别？
         */
        holder.draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.textTitle.setText(wares.getName());
        holder.textPrice.setText("￥"+wares.getPrice());
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size()>0){
            return mDatas.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView draweeView;
        TextView textTitle;
        TextView textPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textPrice = (TextView) itemView.findViewById(R.id.text_price);
        }
    }

    public List<Wares> getDatas(){
        return mDatas;
    }

    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    public void addData(List<Wares> datas){
        addData(0,datas);
    }

    public void addData(int position,List<Wares> datas){
      if (datas !=null && datas.size()>0){
          mDatas.addAll(datas);
          notifyItemRangeChanged(position,mDatas.size());
      }
    }
}
