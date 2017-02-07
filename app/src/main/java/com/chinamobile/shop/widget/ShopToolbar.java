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

/**
 * Created by yjj on 2017/1/15.
 */

public class ShopToolbar extends Toolbar{

    private LayoutInflater mInflater;
    private View mView;
    private ImageView mLeftImageButton;
    private Button mRightButton;
    private TextView mTextTitle;
    private EditText mSearchView;

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

            final Drawable rightIcon = a.getDrawable(R.styleable.ShopToolbar_rightButtonIcon);
            if (navIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.ShopToolbar_isShowSearchView,false);
            if (isShowSearchView){
                showSearchView();
                hideTitleView();
            }
            a.recycle();
        }
    }

    public Button getRightButton(){
        return this.mRightButton;
    }

    public void setRightButtonText(String text){
        if (mRightButton != null){
            mRightButton.setTextColor(Color.WHITE);
            mRightButton.setText(text);
            mRightButton.setVisibility(View.VISIBLE);
        }
    }

    private void setRightButtonIcon(Drawable navIcon) {
        if (mRightButton != null){
            mRightButton.setBackground(navIcon);
            mRightButton.setVisibility(View.VISIBLE);
        }
    }

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

    private void initView() {
        if (mView == null){
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar,null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mLeftImageButton = (ImageView) mView.findViewById(R.id.toolbar_leftButton);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
            addView(mView,lp);
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle!=null){
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void setRightButtonListener(OnClickListener listener){
        mRightButton.setOnClickListener(listener);
    }

    public void showTitleView(){
        mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView(){
        mTextTitle.setVisibility(GONE);
    }

    public void showSearchView(){
        mSearchView.setVisibility(VISIBLE);
    }

    public void hideSearchView(){
        mSearchView.setVisibility(GONE);
    }
}
