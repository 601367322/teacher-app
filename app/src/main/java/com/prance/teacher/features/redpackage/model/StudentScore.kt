package com.prance.teacher.features.redpackage.model

import com.prance.teacher.features.students.model.StudentsEntity

class StudentScore(var student: StudentsEntity,
                   var score: Int,
                   var redPackageNum: Int)