package com.prance.lib.common.utils.http

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by bingbing on 2017/11/22.
 */

abstract class BaseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        return if (oldRequest.url().toString().indexOf("tengyue360") > 0) {
            doIntercept(chain)
        } else {
            chain.proceed(oldRequest)
        }
    }

    @Throws(IOException::class)
    abstract fun doIntercept(chain: Interceptor.Chain): Response
}
