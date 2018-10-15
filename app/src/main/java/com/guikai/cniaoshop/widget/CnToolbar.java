package com.guikai.cniaoshop.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guikai.cniaoshop.R;

/**
 * Created by Ivan on 15/9/28.
 */
public class CnToolbar extends Toolbar {



    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private TextView mSearchView;
    private Button mRightButton;


    public CnToolbar(Context context) {
        this(context,null);
    }

    public CnToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CnToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setContentInsetsRelative(10,10);

        if(attrs !=null) {
            @SuppressLint("RestrictedApi")
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);

            @SuppressLint("RestrictedApi")
            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }

            @SuppressLint("RestrictedApi")
            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView,false);

            if(isShowSearchView){
                showSearchView();
                hideTitleView();
            }

            CharSequence rightButtonText = a.getText(R.styleable.CnToolbar_rightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }
            a.recycle();
        }
    }

    private void initView() {


        if(mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);


            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (TextView) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);


            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon){
        if(mRightButton !=null){
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void setRightButtonIcon(int icon) {
        setRightButtonIcon(getResources().getDrawable(icon,null));
    }


    public void setRightButtonOnClickListener(OnClickListener li){
        mRightButton.setOnClickListener(li);
    }

    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public Button getRightButton(){

        return this.mRightButton;
    }

    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if(mTextTitle !=null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView(){

        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);
    }


    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }

}