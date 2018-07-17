package com.prance.lib.teacher.base.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;


public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {

    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setColorSchemeColors(Color.parseColor("#18bbff"),
                Color.parseColor("#ffcc44"),
                Color.parseColor("#17c774"));

    }

}
