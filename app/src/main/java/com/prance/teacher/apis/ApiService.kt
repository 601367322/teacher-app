package com.prance.teacher.apis

import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.database.UserEntity
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.teacher.features.login.model.QrCodeEntity
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.pk.model.PKResult
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable
import retrofit2.http.*

interface ApiService {

    companion object {

        val qrCodeDetail: String
            get() = if (UrlUtil.isLocalHost())
                "unauth/app/qrCode"
            else
                "backend/assistant/unauth/app/qrCode"

        val checkQrCode: String
            get() = if (UrlUtil.isLocalHost())
                "unauth/app/login"
            else
                "backend/assistant/unauth/app/login"

        val allClasses: String
            get() = if (UrlUtil.isLocalHost())
                "app/classList"
            else
                "backend/teacher/android/classes"

        val studentsForClasses: String
            get() = if (UrlUtil.isLocalHost())
                "app/studentList"
            else
                "backend/course/app/studentList"

        val bindKeyPad: String
            get() = if (UrlUtil.isLocalHost())
                "app/binding"
            else
                "backend/course/app/binding"

        val checkVersion: String
            get() = if (UrlUtil.isLocalHost())
                "unauth/webApp/selectNewlyResult"
            else
                "backend/course/unauth/webApp/selectNewlyResult"

        val postResult: String
            get() = if (UrlUtil.isLocalHost())
                "webApp/questionResult"
            else
                "backend/course/webApp/questionResult"

        val postRedPackageResult: String
            get() = if (UrlUtil.isLocalHost())
                "webApp/sendInteract"
            else
                "backend/course/webApp/sendInteract"

        val postFeedbcakResult: String
            get() = if (UrlUtil.isLocalHost())
                "webApp/feedbackResult"
            else
                "backend/course/webApp/feedbackResult"

        val getPKResult: String
            get() = if (UrlUtil.isLocalHost())
                "web/interactiveResult"
            else
                "backend/course/web/interactiveResult"

        val modifyBind: String
            get() = if (UrlUtil.isLocalHost())
                "clicker/replace"
            else
                "backend/course/clicker/replace"
    }

    @GET
    fun qrCodeDetail(@Url url: String): Flowable<QrCodeEntity>

    @POST
    fun checkQrCode(@Url url: String, @Query("timestamp") timestamp: Long, @Query("token") token: String, @Query("logsss") log: String): Flowable<UserEntity>

    @GET
    fun allClasses(@Url url: String): Flowable<ResponseBody<ClassVo>>

    @GET
    fun studentsForClasses(@Url url: String, @Query("classId") classId: String): Flowable<ResponseBody<StudentsEntity>>

    @GET
    fun bindKeyPad(@Url url: String, @Query("classId") classId: String, @Query("clickerNums") clickerNums: MutableList<String>): Flowable<ResponseBody<StudentsEntity>>

    @POST
    fun replaceKeyPad(@Url url: String, @Query("classId") classId: String, @Query("oldClikerNum") oldKeyPadId: String, @Query("newClikerNum") newKeyPadId: String): Flowable<Any>

    @GET
    fun checkVersion(@Url url: String): Flowable<VersionEntity>

    @FormUrlEncoded
    @POST
    fun postResult(@Url url: String, @Field("classId") classId: String, @Field("answerMsgs") answersJsonArray: String, @Field("questionId") questionId: String, @Field("rewardState") i: Int): Flowable<Any>

    /**
     * 上传抢红包的数据
     */
    @FormUrlEncoded
    @POST
    fun postRedPackageResult(@Url url: String, @Field("classId") classId: String, @Field("answerMsgs") answersJsonArray: String,
                             @Field("interactId") interactId: String): Flowable<Any>

    /**
     * 上传课后反馈的数据
     */
    @FormUrlEncoded
    @POST
    fun postFeedbcakResult(@Url url: String, @Field("classId") classId: String, @Field("feedbackMsgs") answersJsonArray: String, @Field("questionId") questionId: String): Flowable<Any>

    /**
     * PK答题结果
     */
    @GET
    fun getPKResult(@Url url: String, @Query("questionId") questionId: Int): Flowable<PKResult>

    /**
     * 替换答题器
     */
    @FormUrlEncoded
    @POST
    fun modifyBind(@Url url: String, @Field("classId") classId: String, @Field("oldClickerNum") oldClickerNum: String, @Field("newClickerNum") newClickerNum: String): Flowable<Any>
}