package com.prance.teacher.features.afterclass.contract

import com.prance.lib.base.mvp.*
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import io.reactivex.Flowable

/**
 * Description :
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IAfterClassContract {
    interface View : IView<Presenter> {
        /**
         * 倒计时变化
         */
        fun onTimeChange(time: String)

        /**
         * 提交选择信息成功
         */
        fun confirmChooseSuccess()
        fun showLoading()
        fun startSendQuestion()
        fun stopSendQuestion()
    }
    interface Presenter : IPresenter<View, Model> {
        /**
         * 开始接收答题数据
         */
        fun startReceive(mFeedback: ClassesDetailFragment.Question)

        /**
         * 停止接收
         */
        fun stopReceive()

        /**
         * 保存选择结果
         */
        fun saveChoose(deviceId: String, choice: String)
    }
    interface Model : IModel {
        /**
         * 保存选择结果
         */
        fun saveChoose(deviceId: String, choose: String)

        /**
         * 提交选择结果
         */
        fun confirmChoose (classId: Int,questionId: Int): Flowable<Any>
    }
}
