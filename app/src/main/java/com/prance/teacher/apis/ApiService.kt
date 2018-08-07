package com.prance.teacher.apis

import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.database.UserEntity
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.login.model.QrCodeEntity
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable
import retrofit2.http.*

interface ApiService {

    companion object {

        val qrCodeDetail: String
            get() = if (UrlUtil.isLocalHost())
                "unauth/app/qrCode"
            else
                "backend/user/unauth/app/qrCode"

        val checkQrCode: String
            get() = if (UrlUtil.isLocalHost())
                "unauth/app/login"
            else
                "backend/user/unauth/app/login"

        val allClasses: String
            get() = if (UrlUtil.isLocalHost())
                "app/classList"
            else
                "backend/course/app/classList"

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

        val replaceKeyPad: String
            get() = if (UrlUtil.isLocalHost())
                "app/replace"
            else
                "backend/course/app/replace"

        val checkVersion: String
            get() = if (UrlUtil.isLocalHost())
                "app/replace"
            else
                "backend/course/app/replace"

        val postResult: String
            get() = if (UrlUtil.isLocalHost())
                "webApp/questionResult"
            else
                "backend/course/webApp/questionResult"
    }

    @GET
    fun qrCodeDetail(@Url url: String): Flowable<QrCodeEntity>

    @POST
    fun checkQrCode(@Url url: String, @Query("timestamp") timestamp: Long, @Query("token") token: String): Flowable<UserEntity>

    @GET
    fun allClasses(@Url url: String): Flowable<ResponseBody<ClassesEntity>>

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
    fun postResult(@Url url: String, @Field("classId") classId: String, @Field("answerMsgs") answersJsonArray: String, @Field("questionId") questionId: String): Flowable<Any>

}