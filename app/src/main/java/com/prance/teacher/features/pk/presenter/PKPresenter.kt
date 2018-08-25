package com.prance.teacher.features.pk.presenter

import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.socket.BuildConfig
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.socket.generatePostMessage
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.model.PKModel
import com.prance.teacher.features.pk.model.PKSetting
import com.prance.teacher.features.subject.model.KeyPadResult
import java.io.Serializable

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKPresenter : BasePresenterKt<IPKContract.View>(), IPKContract.Presenter {

    override val mModel: IPKContract.Model = PKModel()

    private val mResult = mutableListOf<PKResultMessage>()

    override fun sendAnswer(push: PushServicePresenter, result: KeyPadResult, setting: PKSetting) {
        val students = ClassesDetailFragment.mStudentList
        var studentId: Int? = null
        students?.run {
            for (student in this) {
                if (student.getClicker()?.number == result.clickerId) {
                    studentId = student.id
                }
            }
        }
        if (BuildConfig.DEBUG) {
            if (studentId == null) {
                studentId = 1
            }
        }
        studentId?.run {
            val result = PKResultMessage(this, setting.questionId, setting.classId, result.answer)
            mResult.add(result)
            result.setAvg(calculateAvg(setting))
            push.mService?.sendMessage(generatePostMessage(1, result))
        }
    }

    /**
     * 计算平均答题时间
     *
     * (每个答案的答题时间 - 问题创建时间)的总和/1000毫秒/学生人数
     */
    private fun calculateAvg(setting: PKSetting): Float {
        var totalTime = 0L
        for (result in mResult) {
            totalTime += (result.respond?.answerTime!! - setting.createTime)
        }
        return totalTime.toFloat() / 1000 / mResult.size.toFloat()
    }

    class PKResultMessage : Serializable {
        var respond: Respond? = null
        var classVO: ClassVO? = null

        constructor(studentId: Int, questionId: Int, classId: Int, answer: String) {
            this.respond = Respond(studentId, questionId, answer)
            this.classVO = ClassVO(classId)
        }

        fun setAvg(avg: Float) {
            classVO?.averageTime = Math.round(avg * 100) / 100F
        }

        class IDEntity : Serializable {
            var id: Int? = null

            constructor(id: Int?) {
                this.id = id
            }
        }

        class Respond : Serializable {
            var reply: String? = null
            var student: IDEntity? = null
            var question: IDEntity? = null
            var answerTime = System.currentTimeMillis()

            constructor(studentId: Int, questionId: Int, answer: String) {
                this.reply = answer
                this.student = IDEntity(studentId)
                this.question = IDEntity(questionId)
            }
        }

        class ClassVO : Serializable {
            var klass: IDEntity? = null
            var averageTime: Float? = null

            constructor(classId: Int) {
                this.klass = IDEntity(classId)
            }
        }
    }


}

