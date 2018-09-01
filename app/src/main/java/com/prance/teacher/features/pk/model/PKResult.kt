package com.prance.teacher.features.pk.model

import com.prance.teacher.features.pk.presenter.PKPresenter
import java.io.Serializable

class PKResult : Serializable {

    var orderClasses: MutableList<PKPresenter.PKResultMessage.ClassVO>? = null
    var questionDesc: MutableList<Question>? = null

    class Question : Serializable {

        var type: Int? = null
        var value: Int? = null
    }

    override fun toString(): String {
        return "PKResult(orderClasses=$orderClasses, questionDesc=$questionDesc)"
    }


}