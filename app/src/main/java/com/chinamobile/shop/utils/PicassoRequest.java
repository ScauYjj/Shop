package com.chinamobile.shop.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.chinamobile.shop.listener.IimageListener;
import com.squareup.picasso.Picasso;

/**
 * Created by yjj on 2017/1/19.
 */

public class PicassoRequest implements IimageListener{
    @Override
    public void display(Context context, ImageView imageView, Uri uri) {
        Picasso.with(context)
                .load(uri)
                .into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, String uri) {
        Picasso.with(context)
                .load(uri)
                .into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId) {
        Picasso.with(context)
                .load(url).
                placeholder(progressId)
                .into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId, int errorId) {
        Picasso.with(context).
                load(url)
                .placeholder(progressId)
                .error(errorId)
                .into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId, int errorId, Object tag) {
        Picasso.with(context).
                load(url)
                .placeholder(progressId)
                .error(errorId)
                .tag(tag)
                .into(imageView);
    }
}
