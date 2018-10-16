package com.prance.lib.server.vo.teacher.entity


import java.io.Serializable
import java.util.Date

/**
 *
 *
 * @author yijunhao
 * @date 2018-10-11 21:36:32
 */
class CourseEntity : Serializable {

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
     * 课程名称
     */
    /**
     * 获取：课程名称
     */
    /**
     * 设置：课程名称
     */
    var name: String? = null
    /**
     * userID
     */
    /**
     * 获取：userID
     */
    /**
     * 设置：userID
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
     * 创建时间
     */
    /**
     * 获取：创建时间
     */
    /**
     * 设置：创建时间
     */
    var createTime: Long? = null
    /**
     * 创建人
     */
    /**
     * 获取：创建人
     */
    /**
     * 设置：创建人
     */
    var createMan: String? = null
    /**
     * 修改时间
     */
    /**
     * 获取：修改时间
     */
    /**
     * 设置：修改时间
     */
    var updateTime: Long? = null
    /**
     * 修改人
     */
    /**
     * 获取：修改人
     */
    /**
     * 设置：修改人
     */
    var updateMan: String? = null
    /**
     * 状态: 0删除  1正常
     */
    /**
     * 获取：状态: 0删除  1正常
     */
    /**
     * 设置：状态: 0删除  1正常
     */
    var deleteState: Int? = null
    /**
     * 课程类型 数据字典
     */
    /**
     * 获取：课程类型 数据字典
     */
    /**
     * 设置：课程类型 数据字典
     */
    var type: Int? = null
    /**
     * 年级/阶段
     */
    /**
     * 获取：年级/阶段
     */
    /**
     * 设置：年级/阶段
     */
    var level: Int? = null
    /**
     * 课程状态 0代发布 1 预开课 2当期课程 3 完结
     */
    /**
     * 获取：课程状态 0代发布 1 预开课 2当期课程 3 完结
     */
    /**
     * 设置：课程状态 0代发布 1 预开课 2当期课程 3 完结
     */
    var courseStats: Int? = null
    /**
     * 大纲模版ID
     */
    /**
     * 获取：大纲模版ID
     */
    /**
     * 设置：大纲模版ID
     */
    var outlineId: Int? = null
    /**
     * 年级
     */
    /**
     * 获取：年级
     */
    /**
     * 设置：年级
     */
    var grade: Int? = null
    /**
     * 学科
     */
    /**
     * 获取：学科
     */
    /**
     * 设置：学科
     */
    var subject: Int? = null
    /**
     * 教材版本
     */
    /**
     * 获取：教材版本
     */
    /**
     * 设置：教材版本
     */
    var teachMaterial: Int? = null
    /**
     * 学期
     */
    /**
     * 获取：学期
     */
    /**
     * 设置：学期
     */
    var term: Int? = null
    /**
     * 班型
     */
    /**
     * 获取：班型
     */
    /**
     * 设置：班型
     */
    var classStyle: Int? = null
    /**
     * 大纲ID
     */
    /**
     * 获取：大纲ID
     */
    /**
     * 设置：大纲ID
     */
    var outlineSubId: Int? = null
    /**
     * 老师id
     */
    /**
     * 获取：老师id
     */
    /**
     * 设置：老师id
     */
    var spearkerId: Int? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}
