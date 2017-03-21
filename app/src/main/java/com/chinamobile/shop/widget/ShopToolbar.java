package com.chinamobile.shop.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.chinamobile.shop.R;

import org.w3c.dom.Text;

/**
 * Created by yjj on 2017/1/15.
 */

public class ShopToolbar extends Toolbar{

    private LayoutInflater mInflater;
    private View mView;
    private ImageView mLeftImageButton;
    private ImageView mRightIcon;
    private TextView mTextTitle;
    private EditText mSearchView;
    private TextView mRightText;

    public ShopToolbar(Context context) {
        this(context,null);
    }

    public ShopToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ShopToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //设置边距
        setContentInsetsRelative(10,10);

        initView();

        if (attrs != null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.ShopToolbar, defStyleAttr, 0);

            final Drawable navIcon = a.getDrawable(R.styleable.ShopToolbar_leftButtonIcon);
            if (navIcon != null) {
                setLeftButtonIcon(navIcon);
            }

            final Drawable rightIcon = a.getDrawable(R.styleable.ShopToolbar_rightImageIcon);
            if (navIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.ShopToolbar_isShowSearchView,false);
            if (isShowSearchView){
                showSearchView();
                hideTitleView();
            }

            boolean isShowRightText = a.getBoolean(R.styleable.ShopToolbar_isShowRightText,false);
            String rightText = a.getString(R.styleable.ShopToolbar_rightText);
            if (isShowRightText){
                showRightText(rightText);
            }

            a.recycle();
        }
    }


    /**
     * 隐藏右Icon
     */
    private void showRightButtonIcon() {
        mRightIcon.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
    }

    /**
     * 显示右字体
     */
    private void showRightText(String text) {
        mRightIcon.setVisibility(View.GONE);
        mRightText.setVisibility(View.VISIBLE);
        setRightText(text);

    }

    public ImageView getRightIcon(){
        return this.mRightIcon;
    }

    public TextView getRightText(){
        return this.mRightText;
    }

    /**
     * 设置右图标
     * @param navIcon
     */
    private void setRightButtonIcon(Drawable navIcon) {
        if (mRightIcon != null){
            mRightIcon.setImageDrawable(navIcon);
            mRightIcon.setVisibility(View.VISIBLE);
            mRightText.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右字内容
     * @param text
     */
    public void setRightText(String text){
        if (text != null){
            mRightText.setText(text);
        }
    }

    /**
     * 设置左图标
     * @param navIcon
     */
    private void setLeftButtonIcon(Drawable navIcon) {
        if (mLeftImageButton != null){
            mLeftImageButton.setImageDrawable(navIcon);
            mLeftImageButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        mLeftImageButton.setImageDrawable(icon);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        if (mView == null){
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar,null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mLeftImageButton = (ImageView) mView.findViewById(R.id.toolbar_leftButton);
            mRightIcon = (ImageView) mView.findViewById(R.id.toolbar_right_icon);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightText = (TextView) mView.findViewById(R.id.toolbar_right_text);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
            addView(mView,lp);
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    /**
     * 设置title
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle!=null){
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    /**
     * 设置右图标点击事件
     * @param listener
     */
    public void setRightButtonListener(OnClickListener listener){
        mRightIcon.setOnClickListener(listener);
    }

    /**
     * 设置左图标点击事件
     * @param listener
     */
    public void setLeftButtonListener(OnClickListener listener){
        mLeftImageButton.setOnClickListener(listener);
    }

    public void setRightTextListener(OnClickListener listener){
        mRightText.setOnClickListener(listener);
    }

    /**
     * 显示title
     */
    public void showTitleView(){
        mTextTitle.setVisibility(VISIBLE);
    }

    /**
     * 隐藏title
     */
    public void hideTitleView(){
        mTextTitle.setVisibility(GONE);
    }

    /**
     * 显示搜索框
     */
    public void showSearchView(){
        mSearchView.setVisibility(VISIBLE);
    }

    /**
     * 隐藏搜索框
     */
    public void hideSearchView(){
        mSearchView.setVisibility(GONE);
    }
}
