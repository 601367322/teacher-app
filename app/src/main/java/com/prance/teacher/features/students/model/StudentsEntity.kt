package com.prance.teacher.features.students.model

import java.io.Serializable

class StudentsEntity : Serializable {

    var id: Int? = null
    var name: String? = null
    var clickers: MutableList<Clicker>? = null
    var avatar: String? = null

    constructor(name: String?, avatar: String?) {
        this.name = name
        this.avatar = avatar
    }

    class Clicker : Serializable {
        var id: Int? = null
        var number: String? = null
        override fun toString(): String {
            return "Clicker(id=$id, number=$number)"
        }

    }



    fun getClicker(): Clicker? {
        clickers?.let { return it[0] }
        return null
    }

    override fun toString(): String {
        return "StudentsEntity(id=$id, name=$name, clickers=$clickers)"
    }

}