package com.prance.teacher.features.main.model

import com.prance.teacher.features.main.contract.IMainContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.lib.database.KeyPadDaoUtils
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午12:00
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MainModel : BaseModelKt(), IMainContract.Model {

    override fun logOut(): Flowable<Any> {
        return RetrofitUtils.getApiService(ApiService::class.java).logOut(ApiService.logOut)
    }
}

