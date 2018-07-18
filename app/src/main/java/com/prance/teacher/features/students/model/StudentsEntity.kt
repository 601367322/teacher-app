package com.prance.teacher.features.students.model

import java.io.Serializable

class StudentsEntity : Serializable {

    var id: Int? = null
    var name: String? = null
    var clickers: MutableList<Clicker>? = null

    class Clicker : Serializable {
        var id: Int? = null
        var number: String? = null
    }
}