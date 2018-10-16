package com.prance.lib.common.utils.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.prance.lib.common.R;

public class FocusRelativeLayout extends RelativeLayout {

    protected Rect mBound;
    protected Drawable mDrawable;
    protected Rect mRect;

    private float defaultZ = 0;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;

    public FocusRelativeLayout(Context context) {
        super(context);
        init();
    }

    public FocusRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FocusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        setWillNotDraw(false);
        mRect = new Rect();
        mBound = new Rect();
        //获取焦点后,外侧的阴影图片
//        mDrawable = getResources().getDrawable(R.drawable.btn_common_focused);
        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);
        setClipChildren(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        defaultZ = getZ();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (hasFocus()) {
//            super.getDrawingRect(mRect);
//            mBound.set(-5 + mRect.left, -5 + mRect.top, 5 + mRect.right, 5 + mRect.bottom);
//            mDrawable.setBounds(mBound);
//            canvas.save();
//            mDrawable.draw(canvas);
//            canvas.restore();
//        }
//        super.onDraw(canvas);
//    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            zoomOut();
        } else {
            zoomIn();
        }
    }

    /**
     * 缩小动画
     */
    private void zoomIn() {
        setElevation(0);
        if (scaleSmallAnimation == null) {
            scaleSmallAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_small);
        }
        startAnimation(scaleSmallAnimation);
    }

    /**
     * 放倒动画
     */
    private void zoomOut() {
        setElevation(getResources().getDimensionPixelOffset(R.dimen.m20_0));
        if (scaleBigAnimation == null) {
            scaleBigAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_big);
        }
        startAnimation(scaleBigAnimation);
    }

}
