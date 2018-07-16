package com.prance.teacher.features.main.presenter

import com.prance.teacher.features.main.contract.IMainContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.teacher.features.main.model.MainModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午12:00
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MainPresenter : BasePresenterKt<IMainContract.View>(), IMainContract.Presenter {

    override val mModel: IMainContract.Model = MainModel()

    override fun checkIfKeyPadAlreadyMatched(serialNumber: String?) {

        mModel.getAllKeyPadByBaseStationSN(serialNumber)
    }
}

