package com.prance.teacher.features.subject.contract

import com.prance.lib.base.mvp.*

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

    interface Presenter : IPresenter<View, Model> {}
    interface Model : IModel {}
}
