package com.prance.teacher.features.redpackage.model

import com.prance.teacher.features.students.model.StudentEntity
import java.io.Serializable

class StudentScore(var student: StudentEntity,
                   var score: Long,
                   var redPackageNum: Int) : Serializable