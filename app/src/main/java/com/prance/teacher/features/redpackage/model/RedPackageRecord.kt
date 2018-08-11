package com.prance.teacher.features.redpackage.model

/**
 *Created by rich on 2018/8/8
 *
 * 服务器发送数据
 */

class RedPackageRecord {

    var clickerId: String? = null
    var studentId: Int? = null
    var score: Long? = null

    constructor(score: StudentScore) {
        this.clickerId = score.student.getClicker()?.number
        this.studentId = score.student.id
        this.score = score.score
    }
}