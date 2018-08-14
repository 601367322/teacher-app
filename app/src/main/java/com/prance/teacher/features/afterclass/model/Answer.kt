package com.prance.teacher.features.afterclass.model

import java.io.Serializable

/**
 *Created by rich on 2018/8/8
 */

class Answer : Serializable {
    var clickerId: String? = null
    var answer: String? = null
    var answerTime: Long? = null
    var studentId: Int? = null
}