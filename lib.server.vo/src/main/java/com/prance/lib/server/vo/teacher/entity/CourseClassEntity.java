package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 课程班级表
 * 
 * @author yijunhao
 * @date 2018-10-11 21:38:03
 */
public class CourseClassEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 课程 ID 
	 */
	private Integer courseId;
	/**
	 * 班级 ID 
	 */
	private Integer classId;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String createMan;
	/**
	 * 
	 */
	private Date updateMan;
	/**
	 * 1 为关联 0 为断开
	 */
	private Integer state;

	/**
	 * 设置：课程 ID 
	 */
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程 ID 
	 */
	public Integer getCourseId() {
		return courseId;
	}
	/**
	 * 设置：班级 ID 
	 */
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	/**
	 * 获取：班级 ID 
	 */
	public Integer getClassId() {
		return classId;
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
	public void setUpdateMan(Date updateMan) {
		this.updateMan = updateMan;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateMan() {
		return updateMan;
	}
	/**
	 * 设置：1 为关联 0 为断开
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：1 为关联 0 为断开
	 */
	public Integer getState() {
		return state;
	}
}
