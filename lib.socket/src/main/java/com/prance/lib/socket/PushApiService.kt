package com.prance.lib.socket

import com.prance.lib.common.utils.UrlUtil
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface PushApiService {

    companion object {
        val messageReceivedCallBack: String
            get() = if (UrlUtil.isLocalHost())
                "webApp/receipt"
            else
                "backend/course/webApp/receipt"
    }

    @FormUrlEncoded
    @POST
    fun messageReceivedCallBack(@Url url: String, @Field("msgId") msgId: String): Flowable<Any>


}
