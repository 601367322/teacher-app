package com.prance.lib.teacher.base.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.lang.reflect.Field;

/**
 * 可以加载更多
 * Created by shenbingbing on 16/5/11.
 */
public class LoadMoreRecyclerView extends ObservableRecyclerView {

    private boolean needPause = true;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);

        init();
    }

    boolean isLoadingMore;

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void init() {

        //加载更多监听
        addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoadingMore) {
                        isLoadingMore = true;
                        if (loadListener != null) {
                            loadListener.onLoadMore();
                        }
                    }
                }
            }
        });

        setScrollViewCallbacks(new ObservableScrollViewCallbacks() {

            //只发一遍广播
            boolean sendListener = false;

            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                if (sendListener) {
                    sendListener = false;
                    Intent intent = new Intent("com.prance.lib.music.ui.MediaFloatButtonFragment.ScrollSwipeBroadcastReceiver");
                    if (get(this,"mScrollState") == ScrollState.UP) {
                        intent.putExtra("swipeUp", true);
                    } else {
                        intent.putExtra("swipeUp", false);
                    }
                    getContext().sendBroadcast(intent);
                }
            }

            @Override
            public void onDownMotionEvent() {
                this.sendListener = true;
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

            }
        });

        setVerticalScrollBarEnabled(true);
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        isLoadingMore = loadingMore;
    }

    public ILoadListener loadListener;

    public void setLoadListener(ILoadListener loadListener) {
        this.loadListener = loadListener;
        if (getAdapter() != null && getAdapter() instanceof BaseLoadMoreAdapter) {
            ((BaseLoadMoreAdapter) getAdapter()).setLoadMoreListener(loadListener);
        }
    }

    public interface ILoadListener {
        void onLoadMore();
    }

    public static Object get(Object instance, String variableName) {
        Class targetClass = instance.getClass().getSuperclass();
        // YourSuperClass 替换为实际的父类名字
        ObservableRecyclerView superInst = (ObservableRecyclerView) targetClass.cast(instance);
        Field field;
        try {
            field = targetClass.getDeclaredField(variableName);
            //修改访问限制
            field.setAccessible(true);
            // superInst 为 null 可以获取静态成员
            // 非 null 访问实例成员
            return field.get(superInst);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
