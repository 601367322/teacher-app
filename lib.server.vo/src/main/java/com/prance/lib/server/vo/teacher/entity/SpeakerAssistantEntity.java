package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 主讲、助教表
 * 
 * @author yijunhao
 * @date 2018-10-11 21:38:34
 */
public class SpeakerAssistantEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	private Integer id;
	/**
	 * UID标识
	 */
	private String uid;
	/**
	 * 关联前台账号ID
	 */
	private Integer frontId;
	/**
	 * 类型(0,主讲、1,助教)
	 */
	private Integer type;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 教授科目(语文、数学、英语)
	 */
	private String subjects;
	/**
	 * 教授年级(一年级、二年级、三年级)
	 */
	private String grade;
	/**
	 * 
	 */
	private String photo;
	/**
	 * 手机号； 因校长端系统 ， 有一个是否创建账号，如果是否的话， 手机号需要保存 -By Noah
	 */
	private String phone;
	/**
	 * 
	 */
	private String synopsis;
	/**
	 * 部门 校长端
	 */
	private String department;
	/**
	 * 所属学校
	 */
	private Integer schoolId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private String createMan;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 更新人
	 */
	private String updateMan;
	/**
	 * 删除标识
	 */
	private Integer deleteState;
	/**
	 * 上级领导 -- by 教学端
	 */
	private String leader;
	/**
	 * userId
	 */
	private Long userId;

	/**
	 * 设置：自增ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：自增ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：UID标识
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * 获取：UID标识
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * 设置：关联前台账号ID
	 */
	public void setFrontId(Integer frontId) {
		this.frontId = frontId;
	}
	/**
	 * 获取：关联前台账号ID
	 */
	public Integer getFrontId() {
		return frontId;
	}
	/**
	 * 设置：类型(0,主讲、1,助教)
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型(0,主讲、1,助教)
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
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
	/**
	 * 设置：教授科目(语文、数学、英语)
	 */
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}
	/**
	 * 获取：教授科目(语文、数学、英语)
	 */
	public String getSubjects() {
		return subjects;
	}
	/**
	 * 设置：教授年级(一年级、二年级、三年级)
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * 获取：教授年级(一年级、二年级、三年级)
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * 设置：
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	/**
	 * 获取：
	 */
	public String getPhoto() {
		return photo;
	}
	/**
	 * 设置：手机号； 因校长端系统 ， 有一个是否创建账号，如果是否的话， 手机号需要保存 -By Noah
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机号； 因校长端系统 ， 有一个是否创建账号，如果是否的话， 手机号需要保存 -By Noah
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	/**
	 * 获取：
	 */
	public String getSynopsis() {
		return synopsis;
	}
	/**
	 * 设置：部门 校长端
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：部门 校长端
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：所属学校
	 */
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：所属学校
	 */
	public Integer getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreateMan() {
		return createMan;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：更新人
	 */
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	/**
	 * 获取：更新人
	 */
	public String getUpdateMan() {
		return updateMan;
	}
	/**
	 * 设置：删除标识
	 */
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：删除标识
	 */
	public Integer getDeleteState() {
		return deleteState;
	}
	/**
	 * 设置：上级领导 -- by 教学端
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * 获取：上级领导 -- by 教学端
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * 设置：userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：userId
	 */
	public Long getUserId() {
		return userId;
	}
}
