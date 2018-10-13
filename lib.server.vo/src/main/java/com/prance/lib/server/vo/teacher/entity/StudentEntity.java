package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:37:15
 */
public class StudentEntity implements Serializable {
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
	private String head;
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
	 * 
	 */
	private Integer deleteState;
	/**
	 * 学生性别
	 */
	private Integer sex;
	/**
	 * 前台账号id
	 */
	private Integer frontId;
	/**
	 * 成绩入门
	 */
	private Integer score;
	/**
	 * 家长姓名
	 */
	private String parentName;
	/**
	 * 家长手机号
	 */
	private String parentPhone;
	/**
	 * 积分数量
	 */
	private Integer integralNum;
	/**
	 * 
	 */
	private String mark;
	/**
	 * 学员类型(0,正式学员，1完全退班学员)
	 */
	private Integer type;
	/**
	 * 学生状态(0,当期学员、1,往期学员)
	 */
	private Integer state;
	/**
	 * 
	 */
	private String uid;
	/**
	 * 
	 */
	private Integer grade;
	/**
	 * 离校时间
	 */
	private Date leaSchDate;
	/**
	 * 学员显示状态：0 不显示 1 显示
	 */
	private Integer display;
	/**
	 * 渠道  1:市场推广  2：电话销售 3：传单 4 ：朋友介绍
	 */
	private Integer channel;
	/**
	 * 阶段
	 */
	private Integer phase;
	/**
	 * 
	 */
	private Integer schoolId;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 
	 */
	private String otherTel;
	/**
	 * 居住区域
	 */
	private String areaInfo;

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
	public void setHead(String head) {
		this.head = head;
	}
	/**
	 * 获取：
	 */
	public String getHead() {
		return head;
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
	 * 设置：
	 */
	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}
	/**
	 * 获取：
	 */
	public Integer getDeleteState() {
		return deleteState;
	}
	/**
	 * 设置：学生性别
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 获取：学生性别
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * 设置：前台账号id
	 */
	public void setFrontId(Integer frontId) {
		this.frontId = frontId;
	}
	/**
	 * 获取：前台账号id
	 */
	public Integer getFrontId() {
		return frontId;
	}
	/**
	 * 设置：成绩入门
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	/**
	 * 获取：成绩入门
	 */
	public Integer getScore() {
		return score;
	}
	/**
	 * 设置：家长姓名
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	/**
	 * 获取：家长姓名
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * 设置：家长手机号
	 */
	public void setParentPhone(String parentPhone) {
		this.parentPhone = parentPhone;
	}
	/**
	 * 获取：家长手机号
	 */
	public String getParentPhone() {
		return parentPhone;
	}
	/**
	 * 设置：积分数量
	 */
	public void setIntegralNum(Integer integralNum) {
		this.integralNum = integralNum;
	}
	/**
	 * 获取：积分数量
	 */
	public Integer getIntegralNum() {
		return integralNum;
	}
	/**
	 * 设置：
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}
	/**
	 * 获取：
	 */
	public String getMark() {
		return mark;
	}
	/**
	 * 设置：学员类型(0,正式学员，1完全退班学员)
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：学员类型(0,正式学员，1完全退班学员)
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：学生状态(0,当期学员、1,往期学员)
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：学生状态(0,当期学员、1,往期学员)
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * 获取：
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * 设置：
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	/**
	 * 获取：
	 */
	public Integer getGrade() {
		return grade;
	}
	/**
	 * 设置：离校时间
	 */
	public void setLeaSchDate(Date leaSchDate) {
		this.leaSchDate = leaSchDate;
	}
	/**
	 * 获取：离校时间
	 */
	public Date getLeaSchDate() {
		return leaSchDate;
	}
	/**
	 * 设置：学员显示状态：0 不显示 1 显示
	 */
	public void setDisplay(Integer display) {
		this.display = display;
	}
	/**
	 * 获取：学员显示状态：0 不显示 1 显示
	 */
	public Integer getDisplay() {
		return display;
	}
	/**
	 * 设置：渠道  1:市场推广  2：电话销售 3：传单 4 ：朋友介绍
	 */
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	/**
	 * 获取：渠道  1:市场推广  2：电话销售 3：传单 4 ：朋友介绍
	 */
	public Integer getChannel() {
		return channel;
	}
	/**
	 * 设置：阶段
	 */
	public void setPhase(Integer phase) {
		this.phase = phase;
	}
	/**
	 * 获取：阶段
	 */
	public Integer getPhase() {
		return phase;
	}
	/**
	 * 设置：
	 */
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：
	 */
	public Integer getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：生日
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/**
	 * 获取：生日
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * 设置：
	 */
	public void setOtherTel(String otherTel) {
		this.otherTel = otherTel;
	}
	/**
	 * 获取：
	 */
	public String getOtherTel() {
		return otherTel;
	}
	/**
	 * 设置：居住区域
	 */
	public void setAreaInfo(String areaInfo) {
		this.areaInfo = areaInfo;
	}
	/**
	 * 获取：居住区域
	 */
	public String getAreaInfo() {
		return areaInfo;
	}
}
