package com.prance.teacher.features.classes.model

import java.io.Serializable

class ClassResponseBody : Serializable {
    var classVoList: MutableList<ClassesEntity> = mutableListOf()
}