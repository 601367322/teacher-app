package com.prance.teacher.features.login.model

import com.prance.teacher.features.login.contract.ILoginContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.database.UserEntity
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/14  下午2:21
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class LoginModel : BaseModelKt(), ILoginContract.Model {

    override fun loadQrCodeDetail(): Flowable<QrCodeEntity> {
        return RetrofitUtils.getApiService(ApiService::class.java).qrCodeDetail()
    }

    override fun checkQrCode(qrCode: QrCodeEntity): Flowable<UserEntity> {
        return RetrofitUtils.getApiService(ApiService::class.java).checkQrCode(qrCode.timestamp, qrCode.token)
    }

    override fun checkVersion(): Flowable<VersionEntity> {
        return RetrofitUtils.getApiService(ApiService::class.java).checkVersion()
    }
}

