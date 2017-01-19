package com.chinamobile.shop.listener;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by yjj on 2017/1/19.
 */

public interface IimageListener {

    void display(Context context, ImageView imageView, Uri uri);

    void display(Context context, ImageView imageView, String uri);

    void display(Context context, ImageView imageView,String url, int progressId);

    void display(Context context, ImageView imageView,String url, int progressId, int errorId);

    void display(Context context, ImageView imageView,String url, int progressId, int errorId,Object tag);
}
