package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:36:32
 */
public class CourseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 课程名称
	 */
	private String name;
	/**
	 * userID
	 */
	private Integer teacherId;
	/**
	 * 开课时间
	 */
	private Date startTime;
	/**
	 * 结课时间
	 */
	private Date endTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private String createMan;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 修改人
	 */
	private String updateMan;
	/**
	 * 状态: 0删除  1正常
	 */
	private Integer deleteState;
	/**
	 * 课程类型 数据字典
	 */
	private Integer type;
	/**
	 * 年级/阶段
	 */
	private Integer level;
	/**
	 * 课程状态 0代发布 1 预开课 2当期课程 3 完结
	 */
	private Integer courseStats;
	/**
	 * 大纲模版ID
	 */
	private Integer outlineId;
	/**
	 * 年级
	 */
	private Integer grade;
	/**
	 * 学科
	 */
	private Integer subject;
	/**
	 * 教材版本
	 */
	private Integer teachMaterial;
	/**
	 * 学期
	 */
	private Integer term;
	/**
	 * 班型
	 */
	private Integer classStyle;
	/**
	 * 大纲ID
	 */
	private Integer outlineSubId;
	/**
	 * 老师id
	 */
	private Integer spearkerId;

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
	 * 设置：课程名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：课程名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：userID
	 */
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取：userID
	 */
	public Integer getTeacherId() {
		return teacherId;
	}
	/**
	 * 设置：开课时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：开课时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结课时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结课时间
	 */
	public Date getEndTime() {
		return endTime;
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
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	/**
	 * 获取：修改人
	 */
	public String getUpdateMan() {
		return updateMan;
	}
	/**
	 * 设置：状态: 0删除  1正常
	 */
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：状态: 0删除  1正常
	 */
	public Integer getDeleteState() {
		return deleteState;
	}
	/**
	 * 设置：课程类型 数据字典
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：课程类型 数据字典
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：年级/阶段
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：年级/阶段
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：课程状态 0代发布 1 预开课 2当期课程 3 完结
	 */
	public void setCourseStats(Integer courseStats) {
		this.courseStats = courseStats;
	}
	/**
	 * 获取：课程状态 0代发布 1 预开课 2当期课程 3 完结
	 */
	public Integer getCourseStats() {
		return courseStats;
	}
	/**
	 * 设置：大纲模版ID
	 */
	public void setOutlineId(Integer outlineId) {
		this.outlineId = outlineId;
	}
	/**
	 * 获取：大纲模版ID
	 */
	public Integer getOutlineId() {
		return outlineId;
	}
	/**
	 * 设置：年级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	/**
	 * 获取：年级
	 */
	public Integer getGrade() {
		return grade;
	}
	/**
	 * 设置：学科
	 */
	public void setSubject(Integer subject) {
		this.subject = subject;
	}
	/**
	 * 获取：学科
	 */
	public Integer getSubject() {
		return subject;
	}
	/**
	 * 设置：教材版本
	 */
	public void setTeachMaterial(Integer teachMaterial) {
		this.teachMaterial = teachMaterial;
	}
	/**
	 * 获取：教材版本
	 */
	public Integer getTeachMaterial() {
		return teachMaterial;
	}
	/**
	 * 设置：学期
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
	/**
	 * 获取：学期
	 */
	public Integer getTerm() {
		return term;
	}
	/**
	 * 设置：班型
	 */
	public void setClassStyle(Integer classStyle) {
		this.classStyle = classStyle;
	}
	/**
	 * 获取：班型
	 */
	public Integer getClassStyle() {
		return classStyle;
	}
	/**
	 * 设置：大纲ID
	 */
	public void setOutlineSubId(Integer outlineSubId) {
		this.outlineSubId = outlineSubId;
	}
	/**
	 * 获取：大纲ID
	 */
	public Integer getOutlineSubId() {
		return outlineSubId;
	}
	/**
	 * 设置：老师id
	 */
	public void setSpearkerId(Integer spearkerId) {
		this.spearkerId = spearkerId;
	}
	/**
	 * 获取：老师id
	 */
	public Integer getSpearkerId() {
		return spearkerId;
	}
}
