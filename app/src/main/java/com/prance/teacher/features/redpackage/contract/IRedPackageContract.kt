package com.prance.teacher.features.redpackage.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.teacher.base.http.ResponseBody
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.view.RedPackageView
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable
import retrofit2.http.Query

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
        fun onShowPackage(redPackage: RedPackageView)
        fun onTimeEnd(resultMaps: HashMap<String,Int>)
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
        fun postRedPackageResult(classId: String,answersJsonArray: String,interactId: String,lessionId: String): Flowable<Any>
        /**
         * 获取学生列表信息
         */
        fun getStudentList(classId: String): Flowable<ResponseBody<StudentsEntity>>
    }
}
