package com.prance.lib.teacher.base.core.di

import android.annotation.SuppressLint
import com.blankj.utilcode.util.*
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 设置默认参数
 * Created by shenbingbing on 16/5/12.
 */
class CommonParamsInterceptor : BaseInterceptor() {

    @SuppressLint("MissingPermission")
    @Throws(IOException::class)
    override fun doIntercept(chain: Interceptor.Chain): Response {

        val oldRequest = chain.request()

        // 添加新的参数
        val authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("equipmentId", PhoneUtils.getDeviceId())
                .addQueryParameter("os_version", DeviceUtils.getSDKVersionCode().toString())
                .addQueryParameter("version", AppUtils.getAppVersionCode().toString())
                .addQueryParameter("app_version_name", AppUtils.getAppVersionName())
                .addQueryParameter("screen_width", ScreenUtils.getScreenWidth().toString())
                .addQueryParameter("screen_height", ScreenUtils.getScreenHeight().toString())


        // 新的请求
        val newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build()

        return chain.proceed(newRequest)
    }


}
