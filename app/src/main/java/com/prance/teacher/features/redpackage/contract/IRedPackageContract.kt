package com.prance.teacher.features.redpackage.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.red.RedPackage
import com.prance.teacher.features.students.model.StudentEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IRedPackageContract {
    interface View : IView<Presenter> {
        /**
         * 显示一个随机生成的红包
         */
        fun onShowPackage(redPackage: RedPackage)
        fun onTimeEnd(scores: MutableList<StudentScore>)
        fun startSendRedPackage()
        fun stopSendRedPackage()
        fun startTimer(totalTime: Long)
    }
    interface Presenter : IPresenter<View, Model> {
        /**
         * 获取学生列表信息
         */
        fun getStudentList(classId: String)
        /**
         * 抢红包开始
         */
        fun startRedPackage(mSetting: RedPackageSetting?)

        /**
         * 抢红包结束
         */
        fun stopRedPackage()

        /**
         * 收到答题器的抢红包指令
         */
        fun grabRedPackage(KeyID: String, sInfo: String?)

    }
    interface Model : IModel {
        /**
         * 发送抢红包信息
         */
        fun postRedPackageResult(classId: String,answersJsonArray: String,interactId: String): Flowable<Any>
        /**
         * 获取学生列表信息
         */
        fun getStudentList(classId: String): Flowable<ResponseBody<StudentEntity>>
    }
}
