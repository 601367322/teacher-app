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
        answer.clikerId = deviceId
        answer.answer = choose
        answer.answerTime = Date().time
        for (StudentsEntity in ClassesDetailFragment.mStudentList!!){
            if (deviceId.equals(StudentsEntity.clickers!![0].id)){
                answer.studentId = StudentsEntity.id
            }
        }
        chooseList.add(answer)
    }

    override fun confirmChoose(classId: Int,questionId: Int): Flowable<Any> {
        var json = Gson().toJson(chooseList)
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).postFeedbcakResult(classId.toString(),json,questionId.toString())
    }
}

