package com.chinamobile.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yjj on 2017/1/19.
 */

public class HWAdapter extends SimpleAdapter<Wares> implements View.OnClickListener {

    private Context mContext;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_hot_wares);
        this.mContext = context;
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(wares.getImgUrl());

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice().toString());

        viewHolder.getButton(R.id.hot_button_buy).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hot_button_buy:
                Toast.makeText(mContext,"立即购买",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
