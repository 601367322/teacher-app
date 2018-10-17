package com.prance.teacher.features.modifybind.model

import com.prance.teacher.features.modifybind.contract.IChooseKeyPadContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/10/17  1:49 PM
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ChooseKeyPadModel : BaseModelKt(), IChooseKeyPadContract.Model {

    override fun modifyBind(classId: String, oldKeyPadId: String, newKeyPadId: String): Flowable<Any> {
        return RetrofitUtils.getApiService(ApiService::class.java).modifyBind(ApiService.modifyBind, classId, oldKeyPadId, newKeyPadId)
    }
}

