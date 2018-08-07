package com.prance.teacher.features.subject.model

import com.google.gson.Gson
import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/26  上午10:37
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class SubjectModel : BaseModelKt(), ISubjectContract.Model {

    override fun sendResult(classId: Int, mResult: MutableList<KeyPadResult>, questionId: String): Flowable<Any> {
        return RetrofitUtils.getApiService(ApiService::class.java).postResult(ApiService.postResult, classId.toString(), Gson().toJson(mResult), questionId)
    }
}

