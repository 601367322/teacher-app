package com.prance.teacher.features.classes.model

import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.lib.base.mvp.BaseModelKt
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.teacher.apis.ApiService
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesModel : BaseModelKt(), IClassesContract.Model {

    override fun getAllClasses(): Flowable<ResponseBody<ClassVo>> {
        return RetrofitUtils.getApiService(ApiService::class.java).allClasses(ApiService.allClasses)
    }
}

