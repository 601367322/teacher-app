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

    FooterHolder footerHolder;

    /**
     * 添加数据
     *
     * @param data        数据源
     * @param hasMoreData 是否显示加载更多
     */
    public void addDatas(List<T> data, boolean hasMoreData) {
        addData(data);
        setFooterVisibility(hasMoreData);
    }

    public void setDatas(List<T> data, boolean hasMoreData) {
        setData(data);
        setFooterVisibility(hasMoreData);
    }

    /**
     * 总数多1，是Footer
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size() + 1;
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
        if (viewType == TYPE_FOOTER) {
            //初始化Footer
            footerHolder = new FooterHolder(parent);
            footerHolder.setLoadMoreListener(loadMoreListener);
            return footerHolder;
        }
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

    public void setLoading(boolean loading) {
        if (footerHolder != null) {
            footerHolder.setLoading(loading);
        }
    }

    /**
     * 是否显示更多数据
     *
     * @param visibility
     */
    public void setFooterVisibility(boolean visibility) {
        if (footerHolder != null) {
            footerHolder.setVisibility(visibility);
        }
    }

    LoadMoreRecyclerView.ILoadListener loadMoreListener;

    /**
     * 设置加载更多监听
     * @param loadMoreListener
     */
    public void setLoadMoreListener(LoadMoreRecyclerView.ILoadListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void clearData() {
        super.clearData();
        setFooterVisibility(false);
        notifyDataSetChanged();
    }


    class FooterHolder extends BaseRecyclerHolder {

        LoadMoreListViewFooter mFooterRefreshView;

        private LoadMoreRecyclerView.ILoadListener loadListener;

        //是否有更多数据可供加载，用来判断是否“显示”加载更多view
        private boolean visibility = false;

        private boolean loading = false;

        public FooterHolder(View parent) {
            super(new LoadMoreListViewFooter(parent.getContext()));
            mFooterRefreshView = (LoadMoreListViewFooter) itemView;
            mFooterRefreshView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mFooterRefreshView.setOnClickListener(loadMoreClick);
        }

        @Override
        public void onBind(Object bean) {

            setLoading(isLoading());
            setVisibility(isVisibility());
        }

        View.OnClickListener loadMoreClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadListener != null)
                    loadListener.onLoadMore();
            }
        };

        /**
         * 用来控制loadMore视图显示的内容
         *
         * @param loading
         */
        public void setLoading(boolean loading) {
            this.loading = loading;

            if (mFooterRefreshView != null) {
                if (isLoading()) {
                    mFooterRefreshView.loading();
                } else {
                    mFooterRefreshView.normal();
                }
            }
        }

        /**
         * 是否显示更多数据
         * 用来控制是否显示整个loadMore视图
         *
         * @param hasMoreData
         */
        public void setVisibility(boolean hasMoreData) {
            this.visibility = hasMoreData;

            if (mFooterRefreshView != null) {
                if (isVisibility()) {
                    mFooterRefreshView.show();
                } else {
                    mFooterRefreshView.hide();
                }
            }
        }

        public boolean isVisibility() {
            return visibility;
        }

        public boolean isLoading() {
            return loading;
        }

        public void setLoadMoreListener(LoadMoreRecyclerView.ILoadListener loadMoreListener) {
            this.loadListener = loadMoreListener;
        }
    }
}
