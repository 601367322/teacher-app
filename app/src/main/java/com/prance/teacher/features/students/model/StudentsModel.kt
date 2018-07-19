package com.prance.teacher.features.students.model

import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.teacher.base.http.ResponseBody
import com.prance.lib.teacher.base.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsModel : BaseModelKt(), IStudentsContract.Model {

    override fun getStudentsByClassesId(id: String): Flowable<ResponseBody<StudentsEntity>> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).studentsForClasses(id)
    }

    override fun startBind(classesId: String, keyPadIds: MutableList<String>): Flowable<ResponseBody<StudentsEntity>> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).bindKeyPad(classesId,keyPadIds )
    }
}

