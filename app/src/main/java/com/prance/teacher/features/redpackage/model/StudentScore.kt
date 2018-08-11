package com.prance.teacher.features.redpackage.model

import com.prance.teacher.features.students.model.StudentsEntity
import java.io.Serializable

class StudentScore(var student: StudentsEntity,
                   var score: Long,
                   var redPackageNum: Int) : Serializable