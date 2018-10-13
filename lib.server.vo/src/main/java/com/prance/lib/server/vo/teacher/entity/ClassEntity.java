package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:38:46
 */
public class ClassEntity implements Serializable {
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
	 * 地址
	 */
	private String addr;
	/**
	 * 课程ID
	 */
	private Integer courseId;
	/**
	 * 助教ID
	 */
	private Integer assistantId;
	/**
	 * 主讲老师ID
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
	 * 班级状态:0 正常 1 结课,(保留0 1状态，其余状态从数据字典班级状态Iid)
	 */
	private Integer state;
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
	 * 班级id标识
	 */
	private String uid;
	/**
	 * 学校id
	 */
	private String schoolId;
	/**
	 * 班级类型(数据字典中班级类型id)
	 */
	private Integer classType;
	/**
	 * 班级类别(数据字典中班级类型)
	 */
	private Integer classStyle;
	/**
	 * 年级(数据字典中年级id)
	 */
	private Integer grade;
	/**
	 * 学期(数据字典中学期id)
	 */
	private Integer semester;
	/**
	 * 学科(数据字典中学科id)
	 */
	private Integer subjects;
	/**
	 * 教材
	 */
	private String teachingMaterial;
	/**
	 * 备注
	 */
	private String mark;
	/**
	 * 课程类型(0,正式课)
	 */
	private Integer courseType;
	/**
	 * 
	 */
	private Integer level;
	/**
	 * 预招学生数量
	 */
	private Integer preStudentCount;
	/**
	 * 招生状态，学员达到班级预招生人数时，变为0 停止招生， 默认1 招生
	 */
	private Integer enrolStudentsState;

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
	 * 设置：地址
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}
	/**
	 * 获取：地址
	 */
	public String getAddr() {
		return addr;
	}
	/**
	 * 设置：课程ID
	 */
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程ID
	 */
	public Integer getCourseId() {
		return courseId;
	}
	/**
	 * 设置：助教ID
	 */
	public void setAssistantId(Integer assistantId) {
		this.assistantId = assistantId;
	}
	/**
	 * 获取：助教ID
	 */
	public Integer getAssistantId() {
		return assistantId;
	}
	/**
	 * 设置：主讲老师ID
	 */
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取：主讲老师ID
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
	 * 设置：班级状态:0 正常 1 结课,(保留0 1状态，其余状态从数据字典班级状态Iid)
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：班级状态:0 正常 1 结课,(保留0 1状态，其余状态从数据字典班级状态Iid)
	 */
	public Integer getState() {
		return state;
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
	 * 设置：班级id标识
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * 获取：班级id标识
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * 设置：学校id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：学校id
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：班级类型(数据字典中班级类型id)
	 */
	public void setClassType(Integer classType) {
		this.classType = classType;
	}
	/**
	 * 获取：班级类型(数据字典中班级类型id)
	 */
	public Integer getClassType() {
		return classType;
	}
	/**
	 * 设置：班级类别(数据字典中班级类型)
	 */
	public void setClassStyle(Integer classStyle) {
		this.classStyle = classStyle;
	}
	/**
	 * 获取：班级类别(数据字典中班级类型)
	 */
	public Integer getClassStyle() {
		return classStyle;
	}
	/**
	 * 设置：年级(数据字典中年级id)
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	/**
	 * 获取：年级(数据字典中年级id)
	 */
	public Integer getGrade() {
		return grade;
	}
	/**
	 * 设置：学期(数据字典中学期id)
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}
	/**
	 * 获取：学期(数据字典中学期id)
	 */
	public Integer getSemester() {
		return semester;
	}
	/**
	 * 设置：学科(数据字典中学科id)
	 */
	public void setSubjects(Integer subjects) {
		this.subjects = subjects;
	}
	/**
	 * 获取：学科(数据字典中学科id)
	 */
	public Integer getSubjects() {
		return subjects;
	}
	/**
	 * 设置：教材
	 */
	public void setTeachingMaterial(String teachingMaterial) {
		this.teachingMaterial = teachingMaterial;
	}
	/**
	 * 获取：教材
	 */
	public String getTeachingMaterial() {
		return teachingMaterial;
	}
	/**
	 * 设置：备注
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}
	/**
	 * 获取：备注
	 */
	public String getMark() {
		return mark;
	}
	/**
	 * 设置：课程类型(0,正式课)
	 */
	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}
	/**
	 * 获取：课程类型(0,正式课)
	 */
	public Integer getCourseType() {
		return courseType;
	}
	/**
	 * 设置：
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：预招学生数量
	 */
	public void setPreStudentCount(Integer preStudentCount) {
		this.preStudentCount = preStudentCount;
	}
	/**
	 * 获取：预招学生数量
	 */
	public Integer getPreStudentCount() {
		return preStudentCount;
	}
	/**
	 * 设置：招生状态，学员达到班级预招生人数时，变为0 停止招生， 默认1 招生
	 */
	public void setEnrolStudentsState(Integer enrolStudentsState) {
		this.enrolStudentsState = enrolStudentsState;
	}
	/**
	 * 获取：招生状态，学员达到班级预招生人数时，变为0 停止招生， 默认1 招生
	 */
	public Integer getEnrolStudentsState() {
		return enrolStudentsState;
	}
}
