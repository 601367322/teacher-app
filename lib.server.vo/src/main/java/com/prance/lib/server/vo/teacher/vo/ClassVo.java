package com.prance.lib.server.vo.teacher.vo;


import com.prance.lib.server.vo.teacher.entity.ClassEntity;
import com.prance.lib.server.vo.teacher.entity.CourseEntity;
import com.prance.lib.server.vo.teacher.entity.LessonEntity;
import com.prance.lib.server.vo.teacher.entity.SpeakerAssistantEntity;
import com.prance.lib.server.vo.teacher.entity.UserEntity;

/**
 * @author: yijunhao
 * @Date: 2018/10/11 22:52
 */
public class ClassVo extends ClassEntity {

    /**
     * 课程对象
     */
    private CourseEntity course;

    /**
     * 最近课次
     */
    private LessonEntity lesson;

    /**
     * 主讲老师
     */
    private UserEntity teacher;

    /**
     * 辅导老师
     */
    private SpeakerAssistantEntity assistant;

    /**
     * 学生数量
     */
    private int studentCount;

    /**
     * 答题器绑定人数
     */
    private int bindingCount;

}
