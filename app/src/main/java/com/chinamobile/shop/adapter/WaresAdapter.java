package com.chinamobile.shop.adapter;

import android.content.Context;
import android.net.Uri;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yjj on 2017/1/23.
 */

public class WaresAdapter extends SimpleAdapter<Wares>{


    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_grid_wares);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Wares wares) {
        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
    }
}
