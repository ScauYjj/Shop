package com.chinamobile.shop.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinamobile.shop.Constant;
import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Wares;
import com.chinamobile.shop.utils.CartProvider;
import com.chinamobile.shop.utils.ToastUtils;
import com.chinamobile.shop.widget.ShopToolbar;
import com.orhanobut.logger.Logger;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;

public class WaresDetailActivity extends BaseActivity {

    private WebView mWebView;
    private ShopToolbar mToolbar;
    private Wares wares;
    private CartProvider cartProvider;
    private WebAppInterface webAppInterface;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wares_detail);
        Serializable serializable = getIntent().getSerializableExtra(Constant.WARE);
        if (serializable == null){
            finish();
        }
        dialog = new SpotsDialog(this,"正在加载...");
        dialog.show();
        wares = (Wares) serializable;
        Logger.d(wares);
        cartProvider = CartProvider.getmInstance(this);
        initToolbar();
        initWebView();
    }

    private void initToolbar() {
        mToolbar = (ShopToolbar) findViewById(R.id.ware_list_toolbar);

        mToolbar.setTitle(wares.getName());
        mToolbar.setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.loadUrl("http://112.124.22.238:8081/course_api/wares/detail.html");

        initWebSettings();

        webAppInterface = new WebAppInterface(this);

        mWebView.addJavascriptInterface(webAppInterface,"appInterface");

        mWebView.setWebViewClient(new MyWebClient());
    }

    private void initWebSettings() {

        WebSettings settings = mWebView.getSettings();
        //支持获取手势焦点
        mWebView.requestFocusFromTouch();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setSupportZoom(true);
        //隐藏原生得缩放控件
        settings.setDisplayZoomControls(false);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.supportMultipleWindows();
        settings.setSupportMultipleWindows(true);
        //设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mWebView.getContext().getCacheDir().getAbsolutePath());

        //设置可访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19){
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
    }


    class WebAppInterface{

        private Context context;
        public WebAppInterface(Context context){
            this.context = context;
        }

        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail("+wares.getId()+")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id){
            cartProvider.put(wares);
            ToastUtils.show(context,"已添加到购物车");

        }

        @JavascriptInterface
        public void addFavorites(long id){

        }
    }

    /**
     * HTML加载完所有的网页后才会调用showDetail()
     */
    class MyWebClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            webAppInterface.showDetail();
        }
    }
}
