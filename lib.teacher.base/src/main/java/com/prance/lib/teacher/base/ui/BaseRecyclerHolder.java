package com.prance.lib.teacher.base.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shenbingbing on 16/5/17.
 */
public abstract class BaseRecyclerHolder<T> extends RecyclerView.ViewHolder {

    BaseRecyclerAdapter mAdapter;

    public BaseRecyclerHolder(View itemView) {
        super(itemView);

    }

    public abstract void onBind(T bean);

    public BaseRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseRecyclerAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}