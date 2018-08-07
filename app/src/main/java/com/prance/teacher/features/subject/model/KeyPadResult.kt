package com.prance.teacher.features.subject.model

import java.io.Serializable

data class KeyPadResult(val clickerId: String, val answer: String, val answerTime: Long) : Serializable