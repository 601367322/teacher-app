package com.prance.teacher.features.replacekeypad.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.KeyPadEntity

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IReplaceKeyPadContract {
    interface View : IView<Presenter> {
    }
    interface Presenter : IPresenter<View, Model> {
    }

    interface Model : IModel {
    }
}
