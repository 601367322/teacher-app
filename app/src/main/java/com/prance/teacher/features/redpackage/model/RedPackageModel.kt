package com.prance.teacher.features.redpackage.model

import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackageModel : BaseModelKt(), IRedPackageContract.Model {
    override fun getStudentList(classId: String): Flowable<ResponseBody<StudentsEntity>> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).studentsForClasses(ApiService.studentsForClasses, classId)
    }

    override fun postRedPackageResult(classId: String, answersJsonArray: String, interactId: String, lessionId: String): Flowable<Any> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).postRedPackageResult(classId, answersJsonArray, interactId, lessionId)
    }
}

