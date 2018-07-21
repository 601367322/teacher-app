package com.prance.lib.teacher.base.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * RecyclerView的Adapter
 * 用来显示加载更多的
 * Created by shenbingbing on 16/5/11.
 */
public abstract class BaseLoadMoreAdapter<T, H extends BaseRecyclerHolder> extends BaseRecyclerAdapter<T, BaseRecyclerHolder> {

    public static final int TYPE_FOOTER = 99;

    public static final int TYPE_NORMAL = 0;


    /**
     * 添加数据
     *
     * @param data        数据源
     * @param hasMoreData 是否显示加载更多
     */
    public void addDatas(List<T> data, boolean hasMoreData) {
        addData(data);
    }

    public void setDatas(List<T> data, boolean hasMoreData) {
        setData(data);
    }

    /**
     * 总数多1，是Footer
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 不要继承此方法，用getNormalViewType方法
     *
     * @param position
     * @return
     */
    @Override
    public final int getItemViewType(int position) {
        if (position >= mData.size()) {
            return TYPE_FOOTER;
        } else {
            return getNormalViewType(position);
        }
    }

    /**
     * item类型
     *
     * @param position
     * @return 不能是 TYPE_FOOTER
     */
    protected int getNormalViewType(int position) {
        return TYPE_NORMAL;
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreate(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            viewHolder.onBind(null);
            return;
        }
        super.onBindViewHolder(viewHolder, position);
        onBind((H) viewHolder, position, mData.get(position));
    }

    public abstract H onCreate(ViewGroup parent, final int viewType);

    public abstract void onBind(H viewHolder, int position, T data);


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        //Grid特殊处理
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_FOOTER) {
                        return gridManager.getSpanCount();
                    } else {
                        return getGridSpanSize(position);
                    }
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseRecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);

        //特殊处理 footer
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (holder.getLayoutPosition() == getItemCount()) {
                p.setFullSpan(true);
            } else {
                p.setFullSpan(false);
            }
        }
    }

    /**
     * 所占行数
     *
     * @param position
     * @return
     */
    public int getGridSpanSize(int position) {
        return 1;
    }


    @Override
    public void clearData() {
        super.clearData();
        notifyDataSetChanged();
    }


}
