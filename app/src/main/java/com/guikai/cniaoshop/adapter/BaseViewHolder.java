package com.guikai.cniaoshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Time:         2018/9/4 23:40
 * Package_Name: com.guikai.cniaoshop.adapter
 * File_Name:    BaseViewHolder
 * Creator:      Anding
 * Note:         TODO
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private SparseArray<View> views;

    private BaseAdapter.OnItemClickListener mOnItemClickListener ;

    public BaseViewHolder(View itemView,BaseAdapter.OnItemClickListener onItemClickListener){
        super(itemView);
        itemView.setOnClickListener(this);

        this.mOnItemClickListener =onItemClickListener;
        this.views = new SparseArray<View>();
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }

    public Button getButton(int viewId) {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return retrieveView(viewId);
    }

    public View getView(int viewId) {
        return retrieveView(viewId);
    }


    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}