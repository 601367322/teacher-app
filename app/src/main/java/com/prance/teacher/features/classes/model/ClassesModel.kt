package com.prance.teacher.features.classes.model

import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.teacher.base.http.ResponseBody
import com.prance.lib.teacher.base.http.RetrofitUtils
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesModel : BaseModelKt(), IClassesContract.Model {

    override fun getAllClasses(userId: String): Flowable<ResponseBody<ClassesEntity>> {
        return RetrofitUtils.instance.mRetrofit.create(ApiService::class.java).allClasses(userId)
    }
}

