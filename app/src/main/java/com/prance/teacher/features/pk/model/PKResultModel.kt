package com.prance.teacher.features.pk.model

import com.prance.teacher.features.pk.contract.IPKResultContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/8/29  下午5:49
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKResultModel : BaseModelKt(), IPKResultContract.Model {

    override fun getPKResult(): Flowable<PKResult> {
        return RetrofitUtils.getApiService(ApiService::class.java).getPKResult(ApiService.getPKResult)
    }
}

