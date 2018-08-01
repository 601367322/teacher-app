package com.prance.teacher.features.subject.contract

import com.prance.lib.base.mvp.*
import com.prance.teacher.features.subject.model.KeyPadResult
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/26  上午10:37
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface ISubjectContract {
    interface View : IView<Presenter> {}
    interface FragmentView {

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : IPresenter<View, Model> {
        fun sendResult(classId: Int, mResult: MutableList<KeyPadResult>, questionId: String)
    }

    interface Model : IModel {
        fun sendResult(classId: Int, mResult: MutableList<KeyPadResult>, questionId: String): Flowable<Any>
    }
}
