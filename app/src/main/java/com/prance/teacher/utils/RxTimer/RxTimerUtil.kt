package com.prance.teacher.utils.RxTimer

import com.blankj.utilcode.util.LogUtils

import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object RxTimerUtil {
    private var mDisposable: Disposable? = null

    /** milliseconds毫秒后执行next操,单次执行
     *
     * @param milliseconds
     * @param next
     */
    fun timer(milliseconds: Long, next: IRxNext?) {
        //        LogUtils.e("====定时器开启======milliseconds:"+milliseconds);
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onNext(@NonNull p0: Long) {
                        next?.doNext(p0!!)
                    }

                    override fun onSubscribe(@NonNull disposable: Disposable) {
                        mDisposable = disposable
                    }

//                    override fun onNext(@NonNull number: Long?) {
//                        next?.doNext(number!!)
//                    }

                    override fun onError(@NonNull e: Throwable) {
                        //取消订阅
                        cancel()
                    }

                    override fun onComplete() {
                        //取消订阅
                        cancel()
                    }
                })
    }


    /** 每隔milliseconds毫秒后执行next操作，循环执行
     *
     * @param milliseconds
     * @param next
     */
    fun interval(milliseconds: Long, next: IRxNext?) {
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onNext(@NonNull p0: Long) {
                        next?.doNext(p0!!)
                    }

                    override fun onSubscribe(@NonNull disposable: Disposable) {
                        mDisposable = disposable
                    }



                    override fun onError(@NonNull e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
    }


    /**
     * 取消订阅
     */
    fun cancel() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
            //            LogUtils.e("====定时器取消======");

        }
    }

    interface IRxNext {
        fun doNext(number: Long)
    }


}
