package com.prance.lib.teacher.base.core.di

import com.prance.lib.common.utils.ModelUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpUtils private constructor() {

    var mOkHttpClient: OkHttpClient

    init {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(CommonParamsInterceptor())
        if (ModelUtil.isTestModel) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }
        mOkHttpClient = okHttpClientBuilder.build()
    }

    companion object {
        val instance = OkHttpUtils()
    }

}