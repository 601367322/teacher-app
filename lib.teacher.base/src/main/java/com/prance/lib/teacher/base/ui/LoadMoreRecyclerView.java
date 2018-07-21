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

        setVerticalScrollBarEnabled(true);
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
