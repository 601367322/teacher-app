package com.prance.lib.teacher.base.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.prance.lib.teacher.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenbingbing on 16/5/11.
 *
 * @param <T> Bean 的类型
 * @param <H> Holder 的类型
 */
public abstract class BaseRecyclerAdapter<T, H extends BaseRecyclerHolder> extends RecyclerView.Adapter<H> {


    //数据源
    protected List<T> mData = new ArrayList<>();

    //item点击事件
    private OnItemClickListener mListener;

    /**
     * 添加数据
     *
     * @param data 数据源
     */
    public void addData(List<T> data) {
        this.mData.addAll(data);
    }

    public void clearData() {
        mData.clear();
    }

    public void setData(List<T> data) {
        this.mData.clear();
        this.mData.addAll(data);
    }

    public int indexOfData(T bean) {
        return mData.indexOf(bean);
    }

    public void addData(T data) {
        this.mData.add(data);
    }

    public void addData(int position, T data) {
        this.mData.add(position, data);
    }

    public void removeData(T data) {
        mData.remove(data);
    }

    public void removeDataByPosition(int position) {
        if (position <= mData.size() - 1) {
            mData.remove(position);
        }
    }

    public void addAllData(int position, List<T> data) {
        this.mData.addAll(position, data);
    }

    @Override
    public void onBindViewHolder(H viewHolder, final int position) {
        viewHolder.setAdapter(this);
        final T data = mData.get(position);
        if (mListener != null) {
            viewHolder.itemView.setTag(R.id.tag_data, data);
            viewHolder.itemView.setTag(R.id.tag_data_position, position);
            viewHolder.itemView.setOnClickListener(onItemClick);
        }
    }

    View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.onItemClick(BaseRecyclerAdapter.this, (Integer) v.getTag(R.id.tag_data_position), v.getTag(R.id.tag_data));
        }
    };

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(BaseRecyclerAdapter adapter, int position, T data);
    }

    public T getData(int position) {
        return this.mData.get(position);
    }

    public List<T> getData() {
        return mData;
    }

}
