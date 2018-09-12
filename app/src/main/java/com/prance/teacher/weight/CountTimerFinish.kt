package com.prance.teacher.weight

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CountTimerFinish : FZTextView {

    var mDisposable: Disposable? = null
    var mTotalTime = 10

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        updateTimeText()
        mDisposable = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .take(mTotalTime.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mTotalTime--

                    updateTimeText()
                }
    }

    private fun updateTimeText() {
        if (mTotalTime == 0) {
            (context as Activity).finish()
        }
        text = "${mTotalTime}秒"
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mDisposable?.dispose()
    }
}