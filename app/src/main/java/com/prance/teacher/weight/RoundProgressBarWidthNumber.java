package com.prance.teacher.weight;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.prance.teacher.R;

public class RoundProgressBarWidthNumber extends
        HorizontalProgressBarWithNumber {

    enum ProgressState {
        NORMAL, ERROR, COMPLETE
    }

    private ProgressState state = ProgressState.NORMAL;

    /**
     * color of reached bar
     */
    protected int mReachedBarErrorColor = DEFAULT_TEXT_COLOR;
    protected int mReachedBarCompleteColor = DEFAULT_TEXT_COLOR;

    /**
     * mRadius of view
     */
    private int mRadius = dp2px(30);
    private int mMaxPaintWidth;

    public RoundProgressBarWidthNumber(Context context) {
        this(context, null);
    }

    public RoundProgressBarWidthNumber(Context context, AttributeSet attrs) {
        super(context, attrs);

        mReachedProgressBarHeight = (int) (mUnReachedProgressBarHeight * 2.5f);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBarWithNumber);
        mRadius = (int) ta.getDimension(
                R.styleable.RoundProgressBarWithNumber_radius, mRadius);
        mReachedBarErrorColor = ta
                .getColor(
                        R.styleable.HorizontalProgressBarWithNumber_progress_reached_error_color,
                        mTextColor);
        mReachedBarCompleteColor = ta
                .getColor(
                        R.styleable.HorizontalProgressBarWithNumber_progress_reached_complete_color,
                        mTextColor);
        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 这里默认在布局中padding值要么不设置，要么全部设置
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {

        mMaxPaintWidth = Math.max(mReachedProgressBarHeight,
                mUnReachedProgressBarHeight);
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft()
                + getPaddingRight();
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);
        int realWidth = Math.min(width, height);

        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

        setMeasuredDimension(realWidth, realWidth);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        int reachedBarColor = mReachedBarColor;

        String text ="";
        switch (state){
            case NORMAL:
                text = getProgress() + "%";
                break;
            case ERROR:
                text = "异常";
                reachedBarColor = mReachedBarErrorColor;
                break;
            case COMPLETE:
                text = "正常";
                reachedBarColor = mReachedBarCompleteColor;
                break;
        }


        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop()
                + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        // draw unreaded bar
        mPaint.setColor(mUnReachedBarColor);
        mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        // draw reached bar
        mPaint.setColor(reachedBarColor);
        mPaint.setStrokeWidth(mReachedProgressBarHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), -90,
                sweepAngle, false, mPaint);
        // draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight,
                mPaint);

        canvas.restore();

    }

    public void startAnim(AnimatorListenerAdapter adapter) {
        state = ProgressState.NORMAL;
        ValueAnimator animator = ObjectAnimator.ofInt(1, 100);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = Integer.valueOf(animation.getAnimatedValue().toString());
                setProgress(progress);
            }
        });
        animator.addListener(adapter);
        animator.start();
    }

    public void error() {
        state = ProgressState.ERROR;
        postInvalidate();
    }

    public void ok() {
        state = ProgressState.COMPLETE;
        postInvalidate();
    }
}