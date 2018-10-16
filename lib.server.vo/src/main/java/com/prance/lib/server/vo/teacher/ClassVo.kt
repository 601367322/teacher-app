package com.prance.lib.server.vo.teacher


import com.prance.lib.server.vo.teacher.entity.ClassEntity
import com.prance.lib.server.vo.teacher.entity.CourseEntity
import com.prance.lib.server.vo.teacher.entity.LessonEntity
import com.prance.lib.server.vo.teacher.entity.SpeakerAssistantEntity
import com.prance.lib.server.vo.teacher.entity.UserEntity
import java.io.Serializable

/**
 * @author: yijunhao
 * @Date: 2018/10/11 22:52
 */
class ClassVo : ClassEntity(), Serializable {

    /**
     * 课程对象
     */
    var course: CourseEntity? = null

    /**
     * 最近课次
     */
    var lesson: LessonEntity? = null

    /**
     * 主讲老师
     */
    var teacher: UserEntity? = null

    /**
     * 辅导老师
     */
    var assistant: SpeakerAssistantEntity? = null

    /**
     * 学生数量
     */
    var studentCount: Int = 0

    /**
     * 答题器绑定人数
     */
    var bindingCount: Int = 0

}
