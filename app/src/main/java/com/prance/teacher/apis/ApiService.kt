package com.prance.teacher.apis

import com.prance.lib.database.UserEntity
import com.prance.lib.teacher.base.http.ResponseBody
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.login.model.QrCodeEntity
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //    @GET("http://result.eolinker.com/FfBct6q9d9d994b38dc3c41b45b3cbb591fefab7c045a43?uri=http://api-shuangshi.dev.tengyue360.com/backend/user/app/qrCode")
    @GET("backend/user/app/qrCode")
    fun qrCodeDetail(): Flowable<QrCodeEntity>

    //    @POST("http://result.eolinker.com/FfBct6q9d9d994b38dc3c41b45b3cbb591fefab7c045a43?uri=http://api-shuangshi.dev.tengyue360.com/backend/user/app/login")
    @POST("backend/user/app/login")
    fun checkQrCode(@Query("timestamp") timestamp: Long, @Query("token") token: String): Flowable<UserEntity>

    @GET("backend/course/app/classList")
    fun allClasses(): Flowable<ResponseBody<ClassesEntity>>

    @GET("backend/course/app/studentList")
    fun studentsForClasses(@Query("classId") classId: String): Flowable<ResponseBody<StudentsEntity>>

    @GET("backend/course/app/binding")
    fun bindKeyPad(@Query("classId") classId: String, @Query("clickerNums") clickerNums: MutableList<String>): Flowable<ResponseBody<StudentsEntity>>

    @POST("backend/course/app/replace")
    fun replaceKeyPad(@Query("classId") classId: String, @Query("oldClikerNum") oldKeyPadId: String, @Query("newClikerNum") newKeyPadId: String): Flowable<Any>
}