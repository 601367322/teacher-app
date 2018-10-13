package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:38:34
 */
public class ClassStudentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 
	 */
	private Integer classId;
	/**
	 * 
	 */
	private Integer studentId;
	/**
	 * 0为退班，1为续班
	 */
	private Integer state;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 1 正常 0 删除
	 */
	private Integer deleteState;

	/**
	 * 设置：ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	/**
	 * 获取：
	 */
	public Integer getClassId() {
		return classId;
	}
	/**
	 * 设置：
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * 获取：
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * 设置：0为退班，1为续班
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：0为退班，1为续班
	 */
	public Integer getState() {
		return state;
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
	 * 设置：1 正常 0 删除
	 */
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：1 正常 0 删除
	 */
	public Integer getDeleteState() {
		return deleteState;
	}
}
