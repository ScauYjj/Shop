package com.chinamobile.shop.adapter;

import android.content.Context;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Category;

import java.util.List;

/**
 * Created by yjj on 2017/1/22.
 */

public class CategoryAdapter extends SimpleAdapter<Category>{

    public CategoryAdapter(Context context, List<Category> datas) {
        this(context, datas,0);
    }

    public CategoryAdapter(Context context, List<Category> datas, int layoutId) {
        super(context, datas, R.layout.template_single_text);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Category category) {
        viewHolder.getTextView(R.id.textView).setText(category.getName());
    }
}
