package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:37:15
 */
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String phone;
	/**
	 * 
	 */
	private String password;
	/**
	 * 角色:0讲师 1助教
	 */
	private Integer roleId;
	/**
	 * 所属机构名称
	 */
	private String institution;
	/**
	 * 头像路径
	 */
	private String avatar;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String createMan;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String updateMan;
	/**
	 * 0删除，1正常
	 */
	private Integer deleteState;
	/**
	 * 
	 */
	private Integer userCount;
	/**
	 * 
	 */
	private String email;
	/**
	 * 部门ID
	 */
	private Long deptId;
	/**
	 * 前后台账号标识(0,后台账号、1,前台账号)
	 */
	private Integer style;
	/**
	 * 学校ID
	 */
	private Long schoolId;
	/**
	 * 部门权限 1 本人数据 2 只能看到本人及与自己平级部门的数据 3 与自己平级部门及该部门下的子部门 4显示管辖学校的组织架构，勾选部门 5 所有的数据
	 */
	private Integer deptFlag;
	/**
	 * 盐值
	 */
	private String salt;
	/**
	 * 1正常 0 禁用
	 */
	private Integer status;
	/**
	 * 1 正常 0 锁定
	 */
	private Integer lockFlag;
	/**
	 * 1是 0 否
	 */
	private Integer teacherFlag;
	/**
	 * 性别
	 */
	private Integer sex;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：角色:0讲师 1助教
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：角色:0讲师 1助教
	 */
	public Integer getRoleId() {
		return roleId;
	}
	/**
	 * 设置：所属机构名称
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	/**
	 * 获取：所属机构名称
	 */
	public String getInstitution() {
		return institution;
	}
	/**
	 * 设置：头像路径
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	/**
	 * 获取：头像路径
	 */
	public String getAvatar() {
		return avatar;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	/**
	 * 获取：
	 */
	public String getCreateMan() {
		return createMan;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	/**
	 * 获取：
	 */
	public String getUpdateMan() {
		return updateMan;
	}
	/**
	 * 设置：0删除，1正常
	 */
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：0删除，1正常
	 */
	public Integer getDeleteState() {
		return deleteState;
	}
	/**
	 * 设置：
	 */
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	/**
	 * 获取：
	 */
	public Integer getUserCount() {
		return userCount;
	}
	/**
	 * 设置：
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：部门ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门ID
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：前后台账号标识(0,后台账号、1,前台账号)
	 */
	public void setStyle(Integer style) {
		this.style = style;
	}
	/**
	 * 获取：前后台账号标识(0,后台账号、1,前台账号)
	 */
	public Integer getStyle() {
		return style;
	}
	/**
	 * 设置：学校ID
	 */
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：学校ID
	 */
	public Long getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：部门权限 1 本人数据 2 只能看到本人及与自己平级部门的数据 3 与自己平级部门及该部门下的子部门 4显示管辖学校的组织架构，勾选部门 5 所有的数据
	 */
	public void setDeptFlag(Integer deptFlag) {
		this.deptFlag = deptFlag;
	}
	/**
	 * 获取：部门权限 1 本人数据 2 只能看到本人及与自己平级部门的数据 3 与自己平级部门及该部门下的子部门 4显示管辖学校的组织架构，勾选部门 5 所有的数据
	 */
	public Integer getDeptFlag() {
		return deptFlag;
	}
	/**
	 * 设置：盐值
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * 获取：盐值
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * 设置：1正常 0 禁用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：1正常 0 禁用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：1 正常 0 锁定
	 */
	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
	}
	/**
	 * 获取：1 正常 0 锁定
	 */
	public Integer getLockFlag() {
		return lockFlag;
	}
	/**
	 * 设置：1是 0 否
	 */
	public void setTeacherFlag(Integer teacherFlag) {
		this.teacherFlag = teacherFlag;
	}
	/**
	 * 获取：1是 0 否
	 */
	public Integer getTeacherFlag() {
		return teacherFlag;
	}
	/**
	 * 设置：性别
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 获取：性别
	 */
	public Integer getSex() {
		return sex;
	}
}
