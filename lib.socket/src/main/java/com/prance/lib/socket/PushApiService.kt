package com.prance.lib.socket

import com.prance.lib.common.utils.UrlUtil
import io.reactivex.Flowable
import retrofit2.http.*

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
    fun messageReceivedCallBack(@Url url: String, @Field("msgId") msgId: String, @Query("log") log: String): Flowable<Any>


}
