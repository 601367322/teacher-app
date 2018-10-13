package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:38:34
 */
public class ClickerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 答题器编号
	 */
	private String number;
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
	 * 删除状态:0 正常 1 删除
	 */
	private String deleteState;

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
	 * 设置：答题器编号
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * 获取：答题器编号
	 */
	public String getNumber() {
		return number;
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
	 * 设置：删除状态:0 正常 1 删除
	 */
	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：删除状态:0 正常 1 删除
	 */
	public String getDeleteState() {
		return deleteState;
	}
}
