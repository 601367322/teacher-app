package com.prance.teacher.features.classes.model

import com.prance.teacher.features.classes.contract.IClassesDetailContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.teacher.base.http.ResponseBody
import com.prance.lib.teacher.base.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  rich
 * @date 2018/8/8  上午11:02
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesDetailModel : BaseModelKt(), IClassesDetailContract.Model {
    override fun getStudentsByClassesId(id: String): Flowable<ResponseBody<StudentsEntity>> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).studentsForClasses(id)
    }
}

