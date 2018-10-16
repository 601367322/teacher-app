package com.prance.teacher.weight

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.prance.lib.common.utils.weight.MyButton
import com.prance.teacher.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class CountTimeButton : MyButton {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    var mDisposable: Disposable? = null
    var mTotalTime = 10

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        updateTimeText()

        mDisposable = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .take(mTotalTime.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mTotalTime--

                    if (mTotalTime == 0) {
                        if (context is CountTimButtonListener) {
                            (context as CountTimButtonListener).onTimeEnd()
                        }
                        return@subscribe
                    }

                    updateTimeText()
                }
    }

    private fun updateTimeText() {
        findViewById<TextView>(R.id.text).text = """$mText（${mTotalTime}）"""
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mDisposable?.dispose()
    }

    interface CountTimButtonListener {

        fun onTimeEnd()
    }
}