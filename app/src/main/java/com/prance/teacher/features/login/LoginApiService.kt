package com.prance.teacher.features.login

import com.prance.lib.database.UserEntity
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {

    @GET("backend/user/app/qrCode")
    fun qrCodeDetail(): Flowable<QrCode>

    @POST("backend/user/app/login")
    fun checkQrCode(@Query("timestamp") timestamp: Long, @Query("token") token: String): Flowable<UserEntity>
}