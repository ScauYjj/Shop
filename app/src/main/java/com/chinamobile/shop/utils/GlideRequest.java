package com.chinamobile.shop.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.chinamobile.shop.R;
import com.chinamobile.shop.listener.IimageListener;

/**
 * Created by yjj on 2017/1/19.
 */

public class GlideRequest implements IimageListener{

    @Override
    public void display(Context context, ImageView imageView, Uri uri) {
        DrawableTypeRequest<Uri> load = Glide.with(context).load(uri);
        Glide.with(context).load(uri);
        load.into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, String uri) {
        display(context,imageView,uri,-1,-1,null);
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId) {
        display(context,imageView,url,progressId,-1,null);
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId, int errorId) {
        display(context,imageView,url,progressId,errorId,null);

    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId, int errorId, Object tag) {
        DrawableTypeRequest<String> load = Glide.with(context).load(url);
        if (progressId != -1) {
            load.placeholder(progressId).centerCrop();
        } else {
            load.placeholder(new ColorDrawable(Color.GRAY));
            }
        if (errorId != -1) {
            load.error(errorId);
            }else{
            load.error(R.mipmap.ic_launcher);
        }
        load.into(imageView);
    }
}
