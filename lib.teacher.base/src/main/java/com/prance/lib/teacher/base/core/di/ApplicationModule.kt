package com.prance.lib.teacher.base.core.di

import android.content.Context
import com.google.gson.*
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.teacher.base.TeacherApplication
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
                .baseUrl(UrlUtil.getUrl())
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (ModelUtil.isTestModel) {
            okHttpClientBuilder.addInterceptor(ChuckInterceptor(application))
        }
        return okHttpClientBuilder.build()
    }

}
