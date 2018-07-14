package com.prance.teacher.features.login.presenter

import com.prance.teacher.features.login.contract.ILoginContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.teacher.base.core.di.mySubscribe
import com.prance.lib.teacher.base.core.di.onError
import com.prance.teacher.features.login.QrCode
import com.prance.teacher.features.login.model.LoginModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/14  下午2:21
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class LoginPresenter : BasePresenterKt<ILoginContract.View>(), ILoginContract.Presenter {

    override val mModel: ILoginContract.Model
        get() = LoginModel()

    override fun loadQrCodeDetail() {
        mModel.loadQrCodeDetail().mySubscribe(onError, { mView?.renderQrCode(it) })
    }

    override fun checkQrCode(mQrCode: QrCode?) {
        mQrCode?.let {
            mModel.checkQrCode(mQrCode).mySubscribe({ mView?.checkQrCodeFailCallBack(it) }, { mView?.checkQrCodeSuccessCallBack(it) })
        }
    }
}

