package com.prance.teacher.features.pk.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.socket.PushServicePresenter
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.subject.model.KeyPadResult

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IPKContract {
    interface View : IView<Presenter> {
    }
    interface Presenter : IPresenter<View, Model> {
        fun sendAnswer(push: PushServicePresenter, result: KeyPadResult, setting: ClassesDetailFragment.Question)
    }
    interface Model : IModel {}
}
