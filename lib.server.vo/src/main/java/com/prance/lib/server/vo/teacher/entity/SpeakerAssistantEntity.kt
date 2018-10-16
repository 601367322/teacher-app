package com.prance.lib.server.vo.teacher.entity


import java.io.Serializable
import java.util.Date

/**
 * 主讲、助教表
 *
 * @author yijunhao
 * @date 2018-10-11 21:38:34
 */
class SpeakerAssistantEntity : Serializable {

    /**
     * 自增ID
     */
    /**
     * 获取：自增ID
     */
    /**
     * 设置：自增ID
     */
    var id: Int? = null
    /**
     * UID标识
     */
    /**
     * 获取：UID标识
     */
    /**
     * 设置：UID标识
     */
    var uid: String? = null
    /**
     * 关联前台账号ID
     */
    /**
     * 获取：关联前台账号ID
     */
    /**
     * 设置：关联前台账号ID
     */
    var frontId: Int? = null
    /**
     * 类型(0,主讲、1,助教)
     */
    /**
     * 获取：类型(0,主讲、1,助教)
     */
    /**
     * 设置：类型(0,主讲、1,助教)
     */
    var type: Int? = null
    /**
     * 姓名
     */
    /**
     * 获取：姓名
     */
    /**
     * 设置：姓名
     */
    var name: String? = null
    /**
     * 性别
     */
    /**
     * 获取：性别
     */
    /**
     * 设置：性别
     */
    var sex: Int? = null
    /**
     * 教授科目(语文、数学、英语)
     */
    /**
     * 获取：教授科目(语文、数学、英语)
     */
    /**
     * 设置：教授科目(语文、数学、英语)
     */
    var subjects: String? = null
    /**
     * 教授年级(一年级、二年级、三年级)
     */
    /**
     * 获取：教授年级(一年级、二年级、三年级)
     */
    /**
     * 设置：教授年级(一年级、二年级、三年级)
     */
    var grade: String? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var photo: String? = null
    /**
     * 手机号； 因校长端系统 ， 有一个是否创建账号，如果是否的话， 手机号需要保存 -By Noah
     */
    /**
     * 获取：手机号； 因校长端系统 ， 有一个是否创建账号，如果是否的话， 手机号需要保存 -By Noah
     */
    /**
     * 设置：手机号； 因校长端系统 ， 有一个是否创建账号，如果是否的话， 手机号需要保存 -By Noah
     */
    var phone: String? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var synopsis: String? = null
    /**
     * 部门 校长端
     */
    /**
     * 获取：部门 校长端
     */
    /**
     * 设置：部门 校长端
     */
    var department: String? = null
    /**
     * 所属学校
     */
    /**
     * 获取：所属学校
     */
    /**
     * 设置：所属学校
     */
    var schoolId: Int? = null
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
     * 更新时间
     */
    /**
     * 获取：更新时间
     */
    /**
     * 设置：更新时间
     */
    var updateTime: Long? = null
    /**
     * 更新人
     */
    /**
     * 获取：更新人
     */
    /**
     * 设置：更新人
     */
    var updateMan: String? = null
    /**
     * 删除标识
     */
    /**
     * 获取：删除标识
     */
    /**
     * 设置：删除标识
     */
    var deleteState: Int? = null
    /**
     * 上级领导 -- by 教学端
     */
    /**
     * 获取：上级领导 -- by 教学端
     */
    /**
     * 设置：上级领导 -- by 教学端
     */
    var leader: String? = null
    /**
     * userId
     */
    /**
     * 获取：userId
     */
    /**
     * 设置：userId
     */
    var userId: Long? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}
