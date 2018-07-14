package com.prance.teacher.features.login.model

import com.prance.teacher.features.login.contract.ILoginContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.database.UserEntity
import com.prance.lib.teacher.base.core.di.RetrofitUtils
import com.prance.teacher.features.login.LoginApiService
import com.prance.teacher.features.login.QrCode
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/14  下午2:21
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class LoginModel : BaseModelKt(), ILoginContract.Model {

    override fun loadQrCodeDetail(): Flowable<QrCode> {
        return RetrofitUtils.instance.mRetrofit.create(LoginApiService::class.java).qrCodeDetail()
    }

    override fun checkQrCode(qrCode: QrCode): Flowable<UserEntity> {
        return RetrofitUtils.instance.mRetrofit.create(LoginApiService::class.java).checkQrCode(qrCode.timestamp, qrCode.token)
    }
}

