package com.prance.lib.server.vo.teacher.entity


import java.io.Serializable
import java.util.Date

/**
 *
 *
 * @author yijunhao
 * @date 2018-10-11 21:39:05
 */
class LessonEntity : Serializable {

    /**
     *
     */
     var id: Int? = null
    /**
     *
     */
     var name: String? = null
    /**
     * 地址
     */
     var addr: String? = null
    /**
     *
     */
     var courseId: Int? = null
    /**
     * 班级ID
     */
     var classId: Int? = null
    /**
     *
     */
     var number: Int? = null
    /**
     * 课节状态 0 未开始 1 已结束 2 正在上课
     */
     var state: String? = null
    /**
     *
     */
     var realStartTime: Long? = null
    /**
     *
     */
     var realEndTime: Long? = null
    /**
     * 上课时间
     */
     var startTime: Long? = null
    /**
     * 结束时间
     */
     var endTime: Long? = null
    /**
     *
     */
     var createTime: Long? = null
    /**
     *
     */
     var createMan: String? = null
    /**
     *
     */
     var updateTime: Long? = null
    /**
     *
     */
     var updateMan: String? = null
    /**
     * 删除状态:0 正常 1 删除
     */
     var deleteState: String? = null
    /**
     * 代课老师id
     */
     var teacherId: Int? = null
    /**
     * 课次标签 课次类型 1:作文课 2 ：期中考试 3: 期末考试
     */
     var lessonTag: Int? = null
    /**
     * 教室课件
     */
     var teachCourseware: Int? = null
    /**
     * 学生讲义
     */
     var stuHandout: Int? = null
    /**
     * 课前测
     */
     var preClassTest: Int? = null
    /**
     * 老师教案
     */
     var teachCase: Int? = null
    /**
     * 课次类型 1:作文课 2 ：期中考试 3: 期末考试 已废弃
     */
     var lessonType: Int? = null
    /**
     * 与lesson_tag 值对应
     */
     var knowledgeType: Int? = null
    /**
     * 磨课状态:0未磨课 1已磨课
     */
     var grindState: Int? = null
    /**
     * 磨课视频VID
     */
     var grindVideoId: String? = null
    /**
     * 是否开启 1开启 0未开启
     */
     var openFlag: Int? = null
    /**
     * 课程视频 open 1开启 0未开启
     */
     var openVideoFlag: Int? = null
    /**
     * 课程视频 VID
     */
     var videoId: String? = null
    /**
     * 代课老师user_id
     */
     var substituteTeacher: Int? = null


}
