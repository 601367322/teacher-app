package com.prance.teacher.features.welcome

import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.subject.contract.ISubjectContract
import io.reactivex.Flowable

class WelcomeModel: BaseModelKt(), IWelcomeContract.Model{

    override fun checkVersion(): Flowable<VersionEntity> {
        return RetrofitUtils.getApiService(ApiService::class.java).checkVersion(ApiService.checkVersion)
    }
}