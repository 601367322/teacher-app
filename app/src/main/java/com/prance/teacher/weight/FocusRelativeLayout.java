package com.prance.teacher.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.prance.teacher.R;

public class FocusRelativeLayout extends RelativeLayout {

    private Rect mBound;
    private Drawable mDrawable;
    private Rect mRect;

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
//        mDrawable = getResources().getDrawable(R.drawable.poster_shadow_4);
        setChildrenDrawingOrderEnabled(true);

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

    @Override
    protected void onDraw(Canvas canvas) {
        if (hasFocus()) {
            super.getDrawingRect(mRect);
            mBound.set(-5 + mRect.left, -5 + mRect.top, 5 + mRect.right, 5 + mRect.bottom);
//            mDrawable.setBounds(mBound);
            canvas.save();
//            mDrawable.draw(canvas);
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            getRootView().requestLayout();
            getRootView().invalidate();
            zoomOut();
        } else {
            zoomIn();
        }
    }

    /**
     * 缩小动画
     */
    private void zoomIn() {
        if (scaleSmallAnimation == null) {
            scaleSmallAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_small);
            scaleSmallAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setZ(defaultZ);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        startAnimation(scaleSmallAnimation);
    }

    /**
     * 放倒动画
     */
    private void zoomOut() {
        setZ(1000);
        if (scaleBigAnimation == null) {
            scaleBigAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_big);
        }
        startAnimation(scaleBigAnimation);
    }

}
