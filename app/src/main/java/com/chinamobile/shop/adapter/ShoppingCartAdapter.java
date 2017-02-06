package com.chinamobile.shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.CheckBox;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.ShoppingCart;
import com.chinamobile.shop.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yjj on 2017/2/4.
 */

public class ShoppingCartAdapter extends SimpleAdapter<ShoppingCart> {

    public ShoppingCartAdapter(Context context, List<ShoppingCart> datas) {
        this(context, datas,0);
    }

    public ShoppingCartAdapter(Context context, List<ShoppingCart> datas, int layoutId) {
        super(context, datas, R.layout.template_cart);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, final ShoppingCart cart) {
        viewHolder.getTextView(R.id.text_title).setText(cart.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+cart.getPrice());
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(cart.getImgUrl()));
        CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(cart.isChecked());
        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHolder.getView(R.id.num_control);
        numberAddSubView.setValue(cart.getCount());
    }
}
