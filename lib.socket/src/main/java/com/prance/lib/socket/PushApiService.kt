package com.prance.lib.socket

import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PushApiService {

//    @GET("backend/course/webApp/receipt")
    @FormUrlEncoded
    @POST("backend/course/webApp/receipt")
    fun messageReceivedCallBack(@Field("msgId") msgId: String): Flowable<Any>
}
