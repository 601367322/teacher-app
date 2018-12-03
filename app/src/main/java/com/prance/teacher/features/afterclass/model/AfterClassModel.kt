package com.prance.teacher.features.afterclass.model

import com.google.gson.Gson
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import io.reactivex.Flowable
import java.util.*

/**
 * Description :
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class AfterClassModel : BaseModelKt(), IAfterClassContract.Model {

    var chooseList: ArrayList<Answer> = ArrayList()

    override fun saveChoose(deviceId: String, choose: String) {
        var answer = Answer()
        answer.clickerId = deviceId
        answer.answer = choose
        answer.answerTime = Date().time
        for (StudentsEntity in ClassesDetailFragment.mStudentList!!){
            StudentsEntity.clickerNumber?.let {
                if (deviceId == it){
                    answer.studentId = StudentsEntity.id
                }
            }
        }
        chooseList.add(answer)
    }

    override fun confirmChoose(classId: Int,questionId: Int): Flowable<Any> {
        var json = Gson().toJson(chooseList)
        return RetrofitUtils.getApiService(ApiService::class.java).postFeedbcakResult(ApiService.postFeedbcakResult,classId.toString(),json,questionId.toString())
    }
}

