package com.prance.teacher.features.bind.presenter

import com.prance.teacher.features.bind.contract.IBindKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.teacher.features.bind.model.BindKeyPadModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午9:58
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class BindKeyPadPresenter : BasePresenterKt<IBindKeyPadContract.View>(), IBindKeyPadContract.Presenter {

    override val mModel: IBindKeyPadContract.Model = BindKeyPadModel()
}

