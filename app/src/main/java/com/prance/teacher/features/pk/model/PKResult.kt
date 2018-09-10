package com.prance.teacher.features.pk.model

import com.prance.teacher.features.pk.presenter.PKPresenter
import java.io.Serializable

class PKResult : Serializable {

    var orderClasses: MutableList<PKPresenter.PKResultMessage.ClassVO>? = null
    var questionDesc: MutableList<Question>? = null

    constructor(orderClasses: MutableList<PKPresenter.PKResultMessage.ClassVO>?, questionDesc: MutableList<Question>?) {
        this.orderClasses = orderClasses
        this.questionDesc = questionDesc
    }


    class Question : Serializable {

        var type: Int? = null //0做对 1做错 2不做 3第一名 4第二名 5第三名 6红包持续时间 7红包积分
        var value: Int? = null

        constructor(type: Int?, value: Int?) {
            this.type = type
            this.value = value
        }
    }

    fun getScoreByNumber(number: Int): Int? {
        questionDesc?.let {
            for (q in it) {
                if (q.type?.minus(3) ?: 0 == number) {
                    return q.value
                }
            }
        }
        return null
    }
}