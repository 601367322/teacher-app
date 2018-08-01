package com.prance.teacher.features.afterclass.model

import android.util.Log
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.lib.base.mvp.BaseModelKt
import io.reactivex.Flowable

/**
 * Description :
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class AfterClassModel : BaseModelKt(), IAfterClassContract.Model {

    var chooseMaps: HashMap<String, String> = HashMap()

    override fun saveChoose(deviceId: String, choose: String) {
        chooseMaps.put(deviceId, choose)
        Log.e("rich:AfterClassModel",deviceId + ";" + choose )
    }

    override fun confirmChoose(): Flowable<Int> {
        return Flowable.range(0,1)
    }
}

