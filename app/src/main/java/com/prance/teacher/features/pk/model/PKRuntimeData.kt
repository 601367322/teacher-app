package com.prance.teacher.features.pk.model

import com.prance.teacher.features.pk.presenter.PKPresenter
import java.io.Serializable

class PKRuntimeData : Serializable {

    /**
     * 班级ID
     */
    var klass: PKPresenter.PKResultMessage.IDEntity? = null

    /**
     * 答题平均时间
     */
    var averageTime: Float? = null

    /**
     * 排名
     */
    var order: Int? = null

    /**
     * 正确率
     */
    var correctRate: Float? = null
        get() {
            return field?.times(100)
        }
}