package com.chinamobile.shop.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.shop.R;
import com.chinamobile.shop.bean.Tab;
import com.chinamobile.shop.fragment.CartFragment;
import com.chinamobile.shop.fragment.CategoryFragment;
import com.chinamobile.shop.fragment.HomeFragment;
import com.chinamobile.shop.fragment.HotFragment;
import com.chinamobile.shop.fragment.MineFragment;
import com.chinamobile.shop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    private LayoutInflater mInflater;
    private List<Tab> mTabs;
    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();
    }

    /**
     * 初始化tabHost
     */
    private void initTab(){
        mInflater = LayoutInflater.from(this);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        Tab tab_home = new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab  tab_hot = new Tab(R.string.hot,R.drawable.selector_icon_hot,HotFragment.class);
        Tab tab_category = new Tab(R.string.category,R.drawable.selector_icon_category,CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);

        mTabs = new ArrayList<>();
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        for (Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec,tab.getFragment(),null);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId == getString(R.string.cart)){
                    refData();
                }
            }
        });
        //去除底部分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }

    /**
     * 往购物车添加数据时刷新
     */
    private void refData(){
        if (cartFragment == null){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null){
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
            }
        }else {
            cartFragment.refData();
        }
    }

    /**
     * 创建底部tab
     * @param tab
     * @return
     */
    private View buildIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.text_indicator);

        img.setImageResource(tab.getIcon());
        text.setText(getString(tab.getTitle()));
        return view;
    }

    @Override
    public void showSnackBar(View view, int resId) {
        super.showSnackBar(view, resId);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            {
                exitBy2Click();
            }
        }
        return false;
    }

    /**
     * 是否退出该程序
     */
    private  static Boolean isExit = false;

    /**
     * 双击返回退出程序
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false){
            isExit = true; //准备退出
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务 
        }else {
            /**TODO 为什么无效呢？**/
            //Activitycollector.finishAll();
            System.exit(0);
        }
    }
}
