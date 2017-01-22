package com.chinamobile.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Campaign;
import com.chinamobile.shop.bean.HomeCampaign;
import com.chinamobile.shop.utils.ImageRequestManager;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ivan on 15/9/30.
 */
public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder> {

    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;

    private LayoutInflater mInflater;
    private Context mContext;

    private List<HomeCampaign> mDatas;

    private OnCampaignCllickListener listener;

    public HomeCatgoryAdapter(List<HomeCampaign> datas,Context context){
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        mInflater = LayoutInflater.from(viewGroup.getContext());
        if(type == VIEW_TYPE_R) {
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2, viewGroup, false));
        }else {
            return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,viewGroup,false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        HomeCampaign category = mDatas.get(i);
        viewHolder.textTitle.setText(category.getTitle());

        /*Glide.with(mContext).load(category.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Glide.with(mContext).load(category.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Glide.with(mContext).load(category.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);*/

        ImageRequestManager.getRequest().display(mContext,viewHolder.imageViewBig,category.getCpOne().getImgUrl());
        ImageRequestManager.getRequest().display(mContext,viewHolder.imageViewSmallTop,category.getCpTwo().getImgUrl());
        ImageRequestManager.getRequest().display(mContext,viewHolder.imageViewSmallBottom,category.getCpThree().getImgUrl());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(position % 2==0){
            return  VIEW_TYPE_R;
        }else {
            return VIEW_TYPE_L;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Logger.e(mDatas.toString());
            HomeCampaign campaign = mDatas.get(getLayoutPosition());
            if (listener != null){
                switch (view.getId()){
                    case R.id.imgview_big:
                        listener.onClick(view,campaign.getCpOne());
                        break;
                    case R.id.imgview_small_top:
                        listener.onClick(view,campaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom:
                        listener.onClick(view,campaign.getCpThree());
                        break;
                }
            }
        }
    }

    public  void setOnCampaignCllickListener(OnCampaignCllickListener listener){
        this.listener = listener;
    }

    public interface OnCampaignCllickListener{
       void onClick(View view, Campaign campaign);
    }

    public List<HomeCampaign> getDatas(){
        return mDatas;
    }

    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    public void addData(List<HomeCampaign> datas){
        addData(0,datas);
    }

    public void addData(int position,List<HomeCampaign> datas){
        if (datas !=null && datas.size()>0){
            mDatas.addAll(datas);
            notifyItemRangeChanged(position,mDatas.size());
        }
    }
}
