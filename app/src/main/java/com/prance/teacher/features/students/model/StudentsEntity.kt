package com.prance.teacher.features.students.model

import java.io.Serializable

class StudentsEntity : Serializable {

    var id: Int? = null
    var name: String? = null
    var clickers: MutableList<Clicker>? = null

    class Clicker : Serializable {
        var id: Int? = null
        var number: String? = null
        override fun toString(): String {
            return "Clicker(id=$id, number=$number)"
        }

    }

    override fun toString(): String {
        return "StudentsEntity(id=$id, name=$name, clickers=$clickers)"
    }

}