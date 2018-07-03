package com.shuangshi.lib.base.core.di

import android.content.Context
import com.google.gson.*
import com.tengyue.teacher.BuildConfig
import com.prance.teacher.TeacherApplication
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: TeacherApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides @Singleton fun provideRetrofit(): Retrofit {

        val gson = GsonBuilder().registerTypeAdapter(Double::class.java, JsonSerializer<Double> { src, typeOfSrc, context -> if (src == src!!.toDouble()) JsonPrimitive(src.toLong()) else JsonPrimitive(src) }).create()

        return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/android10/Sample-Data/master/Android-CleanArchitecture-Kotlin/")
                .client(createClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {

        }
        return okHttpClientBuilder.build()
    }

}
