package com.prance.lib.teacher.base.core.di

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DefaultSubscriber

/**
 * Description :
 * @author  XQ Yang
 * @date 2018/7/6  11:02
 */

fun <T> Flowable<T>.mySubscribe(onError: ((Throwable) -> Unit)? = null, onSuccess: (T) -> Unit) {
    this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.invoke(it) }, { onError?.invoke(it) })
}

var onError: (Throwable) -> Unit = {
    if (it is ResultException) {
        ToastUtils.showShort(it.msg)
    } else {
        ToastUtils.showShort("网络连接失败")
    }
}