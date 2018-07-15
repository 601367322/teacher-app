package com.prance.lib.base.mvp

/**
 * Description :
 * @author  XQ Yang
 * @date 2018/7/6  10:36
 */
open class BasePresenterKt<V : ITopView> {
    var mView: V? = null

    var onSubscribeError: (Throwable) -> Unit = {
        if (mView?.onNetworkError(it) == false)
            defaultOnNetworkError(it)
    }
}