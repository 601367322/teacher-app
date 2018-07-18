package com.prance.lib.base.mvp

import com.blankj.utilcode.util.ToastUtils
import com.prance.lib.base.http.ResultException
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Flowable<T>.mySubscribe(onError: ((Throwable) -> Unit)? = null, onSuccess: (T) -> Unit) {
    this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.invoke(it) }, { onError?.invoke(it) })
}

fun defaultOnNetworkError(throwable: Throwable) {
    throwable.printStackTrace()
    if (throwable is ResultException) {
        ToastUtils.showShort(throwable.msg)
    } else {
        ToastUtils.showShort("网络连接失败")
    }
}