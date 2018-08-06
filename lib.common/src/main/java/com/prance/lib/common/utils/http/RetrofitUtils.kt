package com.prance.lib.common.utils.http

import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.prance.lib.common.utils.UrlUtil
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.HashMap

class RetrofitUtils private constructor() {

    var mRetrofit: Retrofit

    internal var map: MutableMap<String, Any> = HashMap()

    init {
        map = HashMap()

        val mGson =
                GsonBuilder()
                        .registerTypeAdapter(
                                Double::class.java,
                                JsonSerializer<Double> { src, _, _ ->
                                    if (src == src!!.toDouble())
                                        JsonPrimitive(src.toLong())
                                    else
                                        JsonPrimitive(src)
                                }
                        )
                        .create()
        mRetrofit = Retrofit.Builder()
                .baseUrl(UrlUtil.getUrl())
                .client(OkHttpUtils.instance.mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun <T> create(clazz: Class<T>): T {
        val impl = RetrofitUtils.instance.mRetrofit.create(clazz)
        map[clazz.name] = impl as Any
        return impl
    }

    companion object {

        val instance = RetrofitUtils()

        fun <T> getApiService(clazz: Class<T>): T {
            return if (instance.map.containsKey(clazz.name)) {
                instance.map[clazz.name] as T
            } else {
                instance.create(clazz)
            }
        }
    }

}