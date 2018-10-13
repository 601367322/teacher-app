package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:38:03
 */
public class StudentClickerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer studentId;
	/**
	 * 
	 */
	private Integer clickerId;

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
	 * 设置：
	 */
	public void setClickerId(Integer clickerId) {
		this.clickerId = clickerId;
	}
	/**
	 * 获取：
	 */
	public Integer getClickerId() {
		return clickerId;
	}
}
