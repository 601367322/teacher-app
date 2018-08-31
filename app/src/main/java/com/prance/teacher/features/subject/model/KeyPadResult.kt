package com.prance.teacher.features.subject.model

import java.io.Serializable

class KeyPadResult : Serializable {

    val clickerId: String
    val answer: String
        get() {
            if (field == "1") {
                return "A"
            }else if(field == "2"){
                return "B"
            }
            return field
        }
    val answerTime: Long

    constructor(clickerId: String, answer: String, answerTime: Long) {
        this.clickerId = clickerId
        this.answer = answer
        this.answerTime = answerTime
    }
}