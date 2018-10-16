package com.prance.lib.server.vo.teacher.entity


import java.io.Serializable
import java.util.Date

/**
 *
 *
 * @author yijunhao
 * @date 2018-10-11 21:38:46
 */
open class ClassEntity : Serializable {

    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var id: Int? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var name: String? = null
    /**
     * 地址
     */
    /**
     * 获取：地址
     */
    /**
     * 设置：地址
     */
    var addr: String? = null
    /**
     * 课程ID
     */
    /**
     * 获取：课程ID
     */
    /**
     * 设置：课程ID
     */
    var courseId: Int? = null
    /**
     * 助教ID
     */
    /**
     * 获取：助教ID
     */
    /**
     * 设置：助教ID
     */
    var assistantId: Int? = null
    /**
     * 主讲老师ID
     */
    /**
     * 获取：主讲老师ID
     */
    /**
     * 设置：主讲老师ID
     */
    var teacherId: Int? = null
    /**
     * 开课时间
     */
    /**
     * 获取：开课时间
     */
    /**
     * 设置：开课时间
     */
    var startTime: Long? = null
    /**
     * 结课时间
     */
    /**
     * 获取：结课时间
     */
    /**
     * 设置：结课时间
     */
    var endTime: Long? = null
    /**
     * 班级状态:0 正常 1 结课,(保留0 1状态，其余状态从数据字典班级状态Iid)
     */
    /**
     * 获取：班级状态:0 正常 1 结课,(保留0 1状态，其余状态从数据字典班级状态Iid)
     */
    /**
     * 设置：班级状态:0 正常 1 结课,(保留0 1状态，其余状态从数据字典班级状态Iid)
     */
    var state: Int? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var createTime: Long? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var createMan: String? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var updateTime: Long? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var updateMan: String? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var deleteState: Int? = null
    /**
     * 班级id标识
     */
    /**
     * 获取：班级id标识
     */
    /**
     * 设置：班级id标识
     */
    var uid: String? = null
    /**
     * 学校id
     */
    /**
     * 获取：学校id
     */
    /**
     * 设置：学校id
     */
    var schoolId: String? = null
    /**
     * 班级类型(数据字典中班级类型id)
     */
    /**
     * 获取：班级类型(数据字典中班级类型id)
     */
    /**
     * 设置：班级类型(数据字典中班级类型id)
     */
    var classType: Int? = null
    /**
     * 班级类别(数据字典中班级类型)
     */
    /**
     * 获取：班级类别(数据字典中班级类型)
     */
    /**
     * 设置：班级类别(数据字典中班级类型)
     */
    var classStyle: Int? = null
    /**
     * 年级(数据字典中年级id)
     */
    /**
     * 获取：年级(数据字典中年级id)
     */
    /**
     * 设置：年级(数据字典中年级id)
     */
    var grade: Int? = null
    /**
     * 学期(数据字典中学期id)
     */
    /**
     * 获取：学期(数据字典中学期id)
     */
    /**
     * 设置：学期(数据字典中学期id)
     */
    var semester: Int? = null
    /**
     * 学科(数据字典中学科id)
     */
    /**
     * 获取：学科(数据字典中学科id)
     */
    /**
     * 设置：学科(数据字典中学科id)
     */
    var subjects: Int? = null
    /**
     * 教材
     */
    /**
     * 获取：教材
     */
    /**
     * 设置：教材
     */
    var teachingMaterial: String? = null
    /**
     * 备注
     */
    /**
     * 获取：备注
     */
    /**
     * 设置：备注
     */
    var mark: String? = null
    /**
     * 课程类型(0,正式课)
     */
    /**
     * 获取：课程类型(0,正式课)
     */
    /**
     * 设置：课程类型(0,正式课)
     */
    var courseType: Int? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var level: Int? = null
    /**
     * 预招学生数量
     */
    /**
     * 获取：预招学生数量
     */
    /**
     * 设置：预招学生数量
     */
    var preStudentCount: Int? = null
    /**
     * 招生状态，学员达到班级预招生人数时，变为0 停止招生， 默认1 招生
     */
    /**
     * 获取：招生状态，学员达到班级预招生人数时，变为0 停止招生， 默认1 招生
     */
    /**
     * 设置：招生状态，学员达到班级预招生人数时，变为0 停止招生， 默认1 招生
     */
    var enrolStudentsState: Int? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}
