package com.prance.lib.socket

import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PushApiService {

//    @GET("backend/course/webApp/receipt")
    @FormUrlEncoded
    @POST("http://10.88.88.204:8080/webApp/receipt")
    fun messageReceivedCallBack(@Field("msgId") msgId: String): Flowable<Any>
}
