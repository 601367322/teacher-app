package com.prance.lib.server.vo.teacher.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author yijunhao
 * @date 2018-10-11 21:39:05
 */
public class LessonEntity implements Serializable {
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
	 * 
	 */
	private Integer courseId;
	/**
	 * 班级ID
	 */
	private Integer classId;
	/**
	 * 
	 */
	private Integer number;
	/**
	 * 课节状态 0 未开始 1 已结束 2 正在上课
	 */
	private String state;
	/**
	 * 
	 */
	private Date realStartTime;
	/**
	 * 
	 */
	private Date realEndTime;
	/**
	 * 上课时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
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
	 * 代课老师id
	 */
	private Integer teacherId;
	/**
	 * 课次标签 课次类型 1:作文课 2 ：期中考试 3: 期末考试
	 */
	private Integer lessonTag;
	/**
	 * 教室课件
	 */
	private Integer teachCourseware;
	/**
	 * 学生讲义
	 */
	private Integer stuHandout;
	/**
	 * 课前测
	 */
	private Integer preClassTest;
	/**
	 * 老师教案
	 */
	private Integer teachCase;
	/**
	 * 课次类型 1:作文课 2 ：期中考试 3: 期末考试 已废弃
	 */
	private Integer lessonType;
	/**
	 * 与lesson_tag 值对应
	 */
	private Integer knowledgeType;
	/**
	 * 磨课状态:0未磨课 1已磨课
	 */
	private Integer grindState;
	/**
	 * 磨课视频VID
	 */
	private String grindVideoId;
	/**
	 * 是否开启 1开启 0未开启
	 */
	private Integer openFlag;
	/**
	 * 课程视频 open 1开启 0未开启
	 */
	private Integer openVideoFlag;
	/**
	 * 课程视频 VID
	 */
	private String videoId;
	/**
	 * 代课老师user_id
	 */
	private Integer substituteTeacher;

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
	 * 设置：
	 */
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：
	 */
	public Integer getCourseId() {
		return courseId;
	}
	/**
	 * 设置：班级ID
	 */
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	/**
	 * 获取：班级ID
	 */
	public Integer getClassId() {
		return classId;
	}
	/**
	 * 设置：
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	/**
	 * 获取：
	 */
	public Integer getNumber() {
		return number;
	}
	/**
	 * 设置：课节状态 0 未开始 1 已结束 2 正在上课
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取：课节状态 0 未开始 1 已结束 2 正在上课
	 */
	public String getState() {
		return state;
	}
	/**
	 * 设置：
	 */
	public void setRealStartTime(Date realStartTime) {
		this.realStartTime = realStartTime;
	}
	/**
	 * 获取：
	 */
	public Date getRealStartTime() {
		return realStartTime;
	}
	/**
	 * 设置：
	 */
	public void setRealEndTime(Date realEndTime) {
		this.realEndTime = realEndTime;
	}
	/**
	 * 获取：
	 */
	public Date getRealEndTime() {
		return realEndTime;
	}
	/**
	 * 设置：上课时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：上课时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return endTime;
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
	/**
	 * 设置：代课老师id
	 */
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取：代课老师id
	 */
	public Integer getTeacherId() {
		return teacherId;
	}
	/**
	 * 设置：课次标签 课次类型 1:作文课 2 ：期中考试 3: 期末考试
	 */
	public void setLessonTag(Integer lessonTag) {
		this.lessonTag = lessonTag;
	}
	/**
	 * 获取：课次标签 课次类型 1:作文课 2 ：期中考试 3: 期末考试
	 */
	public Integer getLessonTag() {
		return lessonTag;
	}
	/**
	 * 设置：教室课件
	 */
	public void setTeachCourseware(Integer teachCourseware) {
		this.teachCourseware = teachCourseware;
	}
	/**
	 * 获取：教室课件
	 */
	public Integer getTeachCourseware() {
		return teachCourseware;
	}
	/**
	 * 设置：学生讲义
	 */
	public void setStuHandout(Integer stuHandout) {
		this.stuHandout = stuHandout;
	}
	/**
	 * 获取：学生讲义
	 */
	public Integer getStuHandout() {
		return stuHandout;
	}
	/**
	 * 设置：课前测
	 */
	public void setPreClassTest(Integer preClassTest) {
		this.preClassTest = preClassTest;
	}
	/**
	 * 获取：课前测
	 */
	public Integer getPreClassTest() {
		return preClassTest;
	}
	/**
	 * 设置：老师教案
	 */
	public void setTeachCase(Integer teachCase) {
		this.teachCase = teachCase;
	}
	/**
	 * 获取：老师教案
	 */
	public Integer getTeachCase() {
		return teachCase;
	}
	/**
	 * 设置：课次类型 1:作文课 2 ：期中考试 3: 期末考试 已废弃
	 */
	public void setLessonType(Integer lessonType) {
		this.lessonType = lessonType;
	}
	/**
	 * 获取：课次类型 1:作文课 2 ：期中考试 3: 期末考试 已废弃
	 */
	public Integer getLessonType() {
		return lessonType;
	}
	/**
	 * 设置：与lesson_tag 值对应
	 */
	public void setKnowledgeType(Integer knowledgeType) {
		this.knowledgeType = knowledgeType;
	}
	/**
	 * 获取：与lesson_tag 值对应
	 */
	public Integer getKnowledgeType() {
		return knowledgeType;
	}
	/**
	 * 设置：磨课状态:0未磨课 1已磨课
	 */
	public void setGrindState(Integer grindState) {
		this.grindState = grindState;
	}
	/**
	 * 获取：磨课状态:0未磨课 1已磨课
	 */
	public Integer getGrindState() {
		return grindState;
	}
	/**
	 * 设置：磨课视频VID
	 */
	public void setGrindVideoId(String grindVideoId) {
		this.grindVideoId = grindVideoId;
	}
	/**
	 * 获取：磨课视频VID
	 */
	public String getGrindVideoId() {
		return grindVideoId;
	}
	/**
	 * 设置：是否开启 1开启 0未开启
	 */
	public void setOpenFlag(Integer openFlag) {
		this.openFlag = openFlag;
	}
	/**
	 * 获取：是否开启 1开启 0未开启
	 */
	public Integer getOpenFlag() {
		return openFlag;
	}
	/**
	 * 设置：课程视频 open 1开启 0未开启
	 */
	public void setOpenVideoFlag(Integer openVideoFlag) {
		this.openVideoFlag = openVideoFlag;
	}
	/**
	 * 获取：课程视频 open 1开启 0未开启
	 */
	public Integer getOpenVideoFlag() {
		return openVideoFlag;
	}
	/**
	 * 设置：课程视频 VID
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	/**
	 * 获取：课程视频 VID
	 */
	public String getVideoId() {
		return videoId;
	}
	/**
	 * 设置：代课老师user_id
	 */
	public void setSubstituteTeacher(Integer substituteTeacher) {
		this.substituteTeacher = substituteTeacher;
	}
	/**
	 * 获取：代课老师user_id
	 */
	public Integer getSubstituteTeacher() {
		return substituteTeacher;
	}
}
