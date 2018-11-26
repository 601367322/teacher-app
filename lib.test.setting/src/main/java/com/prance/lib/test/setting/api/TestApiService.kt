package com.prance.lib.test.setting.api

import com.prance.lib.test.setting.bean.LogUploadBean
import io.reactivex.Flowable
import okhttp3.MultipartBody
import retrofit2.http.*

interface TestApiService {

    @Multipart
    @POST("backend/course/unauth/webApp/upload")
    fun uploadLog(@Part file: MultipartBody.Part, @Query("logsss") log: String = "false"): Flowable<LogUploadBean>


    @FormUrlEncoded
    @POST("backend/teacher/unauth/webApp/saveLog")
    fun uploadLogInfo(@Field("equipmentNumber") deviceId: String, @Field("logUrl") logUrl: String): Flowable<Any>
}