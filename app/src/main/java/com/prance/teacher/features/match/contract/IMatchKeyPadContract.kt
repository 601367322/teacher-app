package com.prance.teacher.features.match.contract

import com.prance.lib.base.mvp.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午3:32
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IMatchKeyPadContract {
    interface View : IView<Presenter> {}
    interface Presenter : IPresenter<View, Model> {}
    interface Model : IModel {}
}
