package com.prance.teacher.features.pk.model

import java.io.Serializable

data class PKSetting(var classId: Int, var questionId: Int, val type: Int, val param: String, val createTime: Long = System.currentTimeMillis()) : Serializable