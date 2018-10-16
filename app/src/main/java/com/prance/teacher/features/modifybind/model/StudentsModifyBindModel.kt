package com.prance.teacher.features.modifybind.model

import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import com.prance.teacher.features.modifybind.contract.IStudentsModifyBindContract
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsModifyBindModel : BaseModelKt(), IStudentsModifyBindContract.Model {

    override fun getStudentsByClassesId(id: String): Flowable<ResponseBody<StudentsEntity>> {
        return RetrofitUtils.getApiService(ApiService::class.java).studentsForClasses(ApiService.studentsForClasses, id)
    }
}

