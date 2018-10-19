package com.prance.teacher.features.students.model

import java.io.Serializable

class StudentEntity : Serializable {

    var id: Int? = null
    var name: String = ""
    var clickers: MutableList<Clicker>? = null
    var head: String? = null

    constructor(name: String?, avatar: String?) {
        this.name = name ?: ""
        this.head = avatar
    }

    constructor(id: Int?, name: String?, avatar: String?) {
        this.name = name ?: ""
        this.head = avatar
        this.id = id ?: 0
    }

    class Clicker : Serializable {
        var id: Int? = null
        var number: String? = null

        constructor(number: String) {
            this.number = number
        }

        override fun toString(): String {
            return "Clicker(id=$id, number=$number)"
        }

    }


    fun getClicker(): Clicker? {
        clickers?.let { return it[0] }
        return null
    }

    override fun toString(): String {
        return "StudentEntity(id=$id, name=$name, clickers=$clickers)"
    }

}