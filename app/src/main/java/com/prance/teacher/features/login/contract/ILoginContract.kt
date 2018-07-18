package com.prance.teacher.features.login.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.UserEntity
import com.prance.teacher.features.login.model.QrCodeEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/14  下午2:21
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface ILoginContract {
    interface View : IView<Presenter> {
        fun renderQrCode(code: QrCodeEntity)
        fun checkQrCodeSuccessCallBack(it: UserEntity?)
        fun checkQrCodeFailCallBack(it: Throwable)
    }

    interface Presenter : IPresenter<View, Model> {
        fun loadQrCodeDetail()
        fun checkQrCode(mQrCode: QrCodeEntity?)
    }

    interface Model : IModel {
        fun loadQrCodeDetail(): Flowable<QrCodeEntity>

        fun checkQrCode(qrCode: QrCodeEntity): Flowable<UserEntity>
    }
}
