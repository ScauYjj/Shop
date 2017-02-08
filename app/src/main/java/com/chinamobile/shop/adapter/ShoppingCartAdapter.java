package com.chinamobile.shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.ShoppingCart;
import com.chinamobile.shop.utils.CartProvider;
import com.chinamobile.shop.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yjj on 2017/2/4.
 */

public class ShoppingCartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener{

    private CheckBox checkBox;
    private TextView textTotal;
    private List<ShoppingCart> mDatas;
    private CartProvider cartProvider;

    public ShoppingCartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView textView) {
        super(context, datas, R.layout.template_cart);
        this.mDatas = datas;
        this.checkBox = checkBox;
        this.textTotal = textView;
        setOnItemClickListener(this);
        showTotalPrice();
        cartProvider = new CartProvider(context);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAllOrNull(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, final ShoppingCart cart) {
        viewHolder.getTextView(R.id.text_title).setText(cart.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+cart.getPrice());
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(cart.getImgUrl()));
        final CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(cart.isChecked());
        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHolder.getView(R.id.num_control);
        numberAddSubView.setValue(cart.getCount());

        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                cart.setCount(value);
                cartProvider.update(cart);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                cart.setCount(value);
                cartProvider.update(cart);
                showTotalPrice();
            }
        });
    }

    /**
     * 点击全选或者全不选
     * @param isChecked
     */
    public void checkAllOrNull(Boolean isChecked){
        if (!isNull()){
            return;
        }
        int i = 0;
        for (ShoppingCart cart : mDatas){
            cart.setChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    /**
     * 计算商品总价钱
     * @return
     */
    private float calculatePrice(){
        float sum = 0;
        if (!isNull()){
            return sum;
        }else {
            for (ShoppingCart cart : mDatas){
               if (cart.isChecked()){
                   sum += cart.getCount() * cart.getPrice();
               }
            }
        }
        return sum;
    }

    /**
     * 判断mDatas是否为空
     * @return
     */
    private boolean isNull(){
        Logger.e(String.valueOf(mDatas));
        return mDatas.size() > 0 && mDatas != null;
    }

    /**
     * 展示购物车总的价钱
     */
    public void showTotalPrice(){
        final float totalPrice = calculatePrice();
        this.textTotal.setText("合计: ¥ "+totalPrice);
    }

    /**
     * 单击选择或者取消选择
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(position);
        checkListener();
        showTotalPrice();
    }

    /**
     * 点击所有item则全选，否则不全选
     */
    private void checkListener(){
        int checkNum = 0;
        if (isNull()){
            for (ShoppingCart cart : mDatas){
                if (!cart.isChecked()){
                    checkBox.setChecked(false);
                }else {
                    checkNum ++;
                }
            }
            if (checkNum == mDatas.size()){
                checkBox.setChecked(true);
            }
        }
    }

    /**
     * 删除购物车数据
     */
    public void delCart(){
        if (!isNull())
            return;
        //在对List循环遍历的时候，在循环过程中会改变list的长度，会报错
//        for (ShoppingCart cart : mDatas){
//            if (cart.isChecked()){
//                int position = mDatas.indexOf(cart);
//                cartProvider.delete(cart);
//                mDatas.remove(cart);
//                notifyItemChanged(position);
//            }
//        }

        for (Iterator iterator = mDatas.iterator() ; iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()){
                int position = mDatas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }
}
