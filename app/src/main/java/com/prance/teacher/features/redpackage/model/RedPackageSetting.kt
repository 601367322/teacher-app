package com.prance.teacher.features.redpackage.model

import com.prance.teacher.features.students.model.StudentEntity
import java.io.Serializable

/**
 *Created by rich on 2018/8/3
 * 收到抢红包指令后data中的数据
 */

class RedPackageSetting : Serializable {
    var classId: Int? = null
    /**
     * 抢红包持续时间
     */
    var lastTime: Int? = null
    /**
     * 趣味活动id
     */
    var interactId: Int? = null
    /**
     * 每个红包的积分数量
     */
    var integrat: Int? = null

    var signStudents: MutableList<StudentEntity>? = null

    constructor(classId: Int?, lastTime: Int?, interactId: Int?, integrat: Int?) {
        this.classId = classId
        this.lastTime = lastTime
        this.interactId = interactId
        this.integrat = integrat
    }
}