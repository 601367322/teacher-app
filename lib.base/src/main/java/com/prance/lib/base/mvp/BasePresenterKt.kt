package com.prance.lib.base.mvp

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
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
            ToastUtils.showShort("登录状态已过期，请重新登录")
            try {
                ActivityUtils.finishAllActivities()
                val packageManager = Utils.getApp().packageManager
                val intent = packageManager.getLaunchIntentForPackage(Utils.getApp().packageName)
                val componentName = intent.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                Utils.getApp().startActivity(mainIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
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