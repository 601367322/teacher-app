package com.prance.lib.base.mvp

import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.http.ResultException
import retrofit2.HttpException

/**
 * Description :
 * @author  XQ Yang
 * @date 2018/7/6  10:36
 */
open class BasePresenterKt<V : ITopView> {
    var mView: V? = null

    var onSubscribeError: (Throwable) -> Unit = {
        mView?.run {
            if (this is BaseFragment) {
                hideProgress()
            }
        }
        if (it is HttpException && it.code() == 401) {
            mView?.exitToLogin()
        } else {
            if (mView?.onNetworkError(it) == false)
                defaultOnNetworkError(it)
        }
    }

    private fun defaultOnNetworkError(throwable: Throwable) {
        throwable.printStackTrace()
        if (throwable is ResultException) {
            ToastUtils.showShort(throwable.msg)
        } else {
            ToastUtils.showShort("网络连接失败")
        }
    }
}