package com.prance.lib.common.utils.http

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Flowable<T>.mySubscribe(onError: ((Throwable) -> Unit)? = null, onSuccess: (T) -> Unit): Disposable {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.invoke(it) }, {
                onError?.invoke(it)
            })
}
