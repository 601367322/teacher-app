package com.prance.lib.teacher.base.http

import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.prance.lib.common.utils.UrlUtil
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RetrofitUtils private constructor() {

    var mRetrofit: Retrofit

    init {
        val mGson = GsonBuilder().registerTypeAdapter(Double::class.java, JsonSerializer<Double> { src, _, _ -> if (src == src!!.toDouble()) JsonPrimitive(src.toLong()) else JsonPrimitive(src) }).create()
        mRetrofit = Retrofit.Builder()
                .baseUrl("http://api-shuangshi.dev.tengyue360.com/")
                .client(OkHttpUtils.instance.mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    companion object {
        val instance = RetrofitUtils()
    }

}