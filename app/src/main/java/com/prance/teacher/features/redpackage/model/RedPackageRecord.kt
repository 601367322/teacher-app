package com.prance.teacher.features.redpackage.model

/**
 *Created by rich on 2018/8/8
 */

class RedPackageRecord {
    //{\"clickerId\":\"0001\",\"studentId\":\"1\",\"answerTime\":\"12345678912\"}
    var clickerId: String? = null
    var studentId: String? = null
    var answerTime: Long? = null

    constructor(clickerId: String?, studentId: String?, answerTime: Long?) {
        this.clickerId = clickerId
        this.studentId = studentId
        this.answerTime = answerTime
    }
}