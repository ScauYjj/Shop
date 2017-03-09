package com.chinamobile.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chinamobile.shop.R;
import com.chinamobile.shop.activity.MainActivity;
import com.chinamobile.shop.bean.ShoppingCart;
import com.chinamobile.shop.bean.Wares;
import com.chinamobile.shop.utils.CartProvider;
import com.chinamobile.shop.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yjj on 2017/1/19.
 */

public class HWAdapter extends SimpleAdapter<Wares>{

    private Context mContext;
    private CartProvider cartProvider;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_hot_wares);
        this.mContext = context;
        cartProvider = CartProvider.getmInstance(context);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, final Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(wares.getImgUrl());

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice().toString());

        viewHolder.getButton(R.id.hot_button_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartProvider.put(wares);
                MainActivity activity = (MainActivity) mContext;
                activity.showSnackBar(view,"已添加到购物车");
            }
        });
    }


}
