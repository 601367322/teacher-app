package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 账号表
 * 
 * @author yijunhao
 * @date 2018-10-11 22:21:43
 */
public class AccountNumberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	private Integer id;
	/**
	 * 账号角色(0，管理员、1,学生、2 家长、3 校长、4 助教、5 主讲、6员工、7校长员工)
	 */
	private Integer role;
	/**
	 * 账号名称
	 */
	private String name;
	/**
	 * 账号(手机号,邮箱)
	 */
	private String accountNumber;
	/**
	 * 账号类型(0 已开通，1 未开通)
	 */
	private Integer type;
	/**
	 * 已开通账号状态(0 停用，1 启用)
	 */
	private Integer state;
	/**
	 * 关联用户ID
	 */
	private Integer userId;
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
	 * 删除标识(0,删除，1正常)
	 */
	private Integer deleteState;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 前后台账号标识(0,后台账号、1,前台账号)
	 */
	private Integer style;
	/**
	 * 明文密码
	 */
	private String enablepassword;

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
	 * 设置：账号角色(0，管理员、1,学生、2 家长、3 校长、4 助教、5 主讲、6员工、7校长员工)
	 */
	public void setRole(Integer role) {
		this.role = role;
	}
	/**
	 * 获取：账号角色(0，管理员、1,学生、2 家长、3 校长、4 助教、5 主讲、6员工、7校长员工)
	 */
	public Integer getRole() {
		return role;
	}
	/**
	 * 设置：账号名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：账号名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：账号(手机号,邮箱)
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * 获取：账号(手机号,邮箱)
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * 设置：账号类型(0 已开通，1 未开通)
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：账号类型(0 已开通，1 未开通)
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：已开通账号状态(0 停用，1 启用)
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：已开通账号状态(0 停用，1 启用)
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：关联用户ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：关联用户ID
	 */
	public Integer getUserId() {
		return userId;
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
	 * 设置：删除标识(0,删除，1正常)
	 */
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：删除标识(0,删除，1正常)
	 */
	public Integer getDeleteState() {
		return deleteState;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
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
	 * 设置：明文密码
	 */
	public void setEnablepassword(String enablepassword) {
		this.enablepassword = enablepassword;
	}
	/**
	 * 获取：明文密码
	 */
	public String getEnablepassword() {
		return enablepassword;
	}
}
