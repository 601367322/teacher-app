package com.prance.teacher.features.classes.model

import java.io.Serializable

class ClassesEntity : Serializable {

    var binding: Int = 0
    var klass: KlassEntity? = null

    class KlassEntity : Serializable {
        var id: String? = null
        var name: String? = null
        var course: CourseEntity? = null
        var teacher: TeacherEntity? = null
        var startTime: String? = null
        var endTime: String? = null
    }

    class CourseEntity : Serializable {
        var id: String? = null
        var name: String? = null
    }

    class TeacherEntity : Serializable {

        var id: String? = null
        var name: String? = null
        var phone: String? = null
        var roleId: Int? = null
        var institution: String? = null
    }
}