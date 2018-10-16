package com.prance.lib.server.vo.teacher.entity


import java.io.Serializable
import java.util.Date

/**
 *
 *
 * @author yijunhao
 * @date 2018-10-11 21:37:15
 */
class UserEntity : Serializable {

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
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
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
    var password: String? = null
    /**
     * 角色:0讲师 1助教
     */
    /**
     * 获取：角色:0讲师 1助教
     */
    /**
     * 设置：角色:0讲师 1助教
     */
    var roleId: Int? = null
    /**
     * 所属机构名称
     */
    /**
     * 获取：所属机构名称
     */
    /**
     * 设置：所属机构名称
     */
    var institution: String? = null
    /**
     * 头像路径
     */
    /**
     * 获取：头像路径
     */
    /**
     * 设置：头像路径
     */
    var avatar: String? = null
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
     * 0删除，1正常
     */
    /**
     * 获取：0删除，1正常
     */
    /**
     * 设置：0删除，1正常
     */
    var deleteState: Int? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var userCount: Int? = null
    /**
     *
     */
    /**
     * 获取：
     */
    /**
     * 设置：
     */
    var email: String? = null
    /**
     * 部门ID
     */
    /**
     * 获取：部门ID
     */
    /**
     * 设置：部门ID
     */
    var deptId: Long? = null
    /**
     * 前后台账号标识(0,后台账号、1,前台账号)
     */
    /**
     * 获取：前后台账号标识(0,后台账号、1,前台账号)
     */
    /**
     * 设置：前后台账号标识(0,后台账号、1,前台账号)
     */
    var style: Int? = null
    /**
     * 学校ID
     */
    /**
     * 获取：学校ID
     */
    /**
     * 设置：学校ID
     */
    var schoolId: Long? = null
    /**
     * 部门权限 1 本人数据 2 只能看到本人及与自己平级部门的数据 3 与自己平级部门及该部门下的子部门 4显示管辖学校的组织架构，勾选部门 5 所有的数据
     */
    /**
     * 获取：部门权限 1 本人数据 2 只能看到本人及与自己平级部门的数据 3 与自己平级部门及该部门下的子部门 4显示管辖学校的组织架构，勾选部门 5 所有的数据
     */
    /**
     * 设置：部门权限 1 本人数据 2 只能看到本人及与自己平级部门的数据 3 与自己平级部门及该部门下的子部门 4显示管辖学校的组织架构，勾选部门 5 所有的数据
     */
    var deptFlag: Int? = null
    /**
     * 盐值
     */
    /**
     * 获取：盐值
     */
    /**
     * 设置：盐值
     */
    var salt: String? = null
    /**
     * 1正常 0 禁用
     */
    /**
     * 获取：1正常 0 禁用
     */
    /**
     * 设置：1正常 0 禁用
     */
    var status: Int? = null
    /**
     * 1 正常 0 锁定
     */
    /**
     * 获取：1 正常 0 锁定
     */
    /**
     * 设置：1 正常 0 锁定
     */
    var lockFlag: Int? = null
    /**
     * 1是 0 否
     */
    /**
     * 获取：1是 0 否
     */
    /**
     * 设置：1是 0 否
     */
    var teacherFlag: Int? = null
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

    companion object {
        private const val serialVersionUID = 1L
    }
}
