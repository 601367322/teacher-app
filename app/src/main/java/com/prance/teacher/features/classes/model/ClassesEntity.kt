package com.prance.teacher.features.classes.model

import java.io.Serializable

class ClassesEntity : Serializable {

    var binding: Int = 0
    var klass: KlassEntity? = null
    var teacher: TeacherEntity? = null

    class KlassEntity : Serializable {
        var id: String? = null
        var name: String? = null
        var course: String? = null
        var teacher: TeacherEntity? = null
    }

    class TeacherEntity : Serializable {

        var id: Int? = null
        var name: String? = null
        var phone: String? = null
        var roleId: Int? = null
        var institution: String? = null
    }
}