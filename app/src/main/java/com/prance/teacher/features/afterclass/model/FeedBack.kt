package com.prance.teacher.features.afterclass.model

import java.io.Serializable

/**
 *Created by rich on 2018/8/7
 */

class FeedBack : Serializable {
    var classId: Int? = null
    var questionId: Int? = null

    constructor(classId: Int?, questionId: Int?) {
        this.classId = classId
        this.questionId = questionId
    }


    override fun toString(): String {
        return "FeedBack(classId=$classId, questionId=$questionId)"
    }

}