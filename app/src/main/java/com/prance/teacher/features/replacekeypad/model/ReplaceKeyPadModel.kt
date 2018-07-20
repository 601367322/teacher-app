package com.prance.teacher.features.replacekeypad.model

import com.prance.teacher.features.replacekeypad.contract.IReplaceKeyPadContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.teacher.base.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ReplaceKeyPadModel : BaseModelKt(), IReplaceKeyPadContract.Model {

    override fun replaceKeyPad(classesId: String, oldKeyPadId: String, newKeyPadId: String): Flowable<Any> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).replaceKeyPad(classesId, oldKeyPadId, newKeyPadId)
    }
}
