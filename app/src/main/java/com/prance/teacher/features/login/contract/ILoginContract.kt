package com.prance.teacher.features.login.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.UserEntity
import com.prance.teacher.features.login.model.QrCodeEntity
import com.prance.teacher.features.login.model.VersionEntity
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
        fun checkVersionCallBack(versionEntity: VersionEntity?)
    }

    interface Presenter : IPresenter<View, Model> {
        fun loadQrCodeDetail()
        fun checkQrCode(mQrCode: QrCodeEntity?)
        fun checkVersion()
    }

    interface Model : IModel {
        fun loadQrCodeDetail(): Flowable<QrCodeEntity>
        fun checkVersion(): Flowable<VersionEntity>
        fun checkQrCode(qrCode: QrCodeEntity): Flowable<UserEntity>
    }
}
