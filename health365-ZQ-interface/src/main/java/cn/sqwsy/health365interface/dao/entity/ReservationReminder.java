package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sqwsy.health365interface.service.utils.DateUtil;

public class ReservationReminder extends PO{

	private Integer id;//ID
	private Date reservationTime = DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"yyyy-MM-dd");//预约时间
	private Timestamp createTime =  new Timestamp(System.currentTimeMillis());//创建时间
	private Timestamp updateTime =  new Timestamp(System.currentTimeMillis());//更新时间
	private Integer status;//复诊状态1、待复诊2、已复诊 3、未复诊
	private Integer fakeStatus;//章丘复诊状态 1、待复诊 2、已复诊
	private Integer smsStatus;//短信状态1、未提醒 2、已提醒3、短信发送失败

	private UserInfo userInfo;//操作人
	
	private Outofthehospitalinhospitalinformation outHospital;//关联出院记录
	
	private Patient patient;//患者
	
	private Department department;//科室
	
	private Followup followup;//关联随访记录

	public Integer getId() {
		return id;
	}

	public Date getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(Integer smsStatus) {
		this.smsStatus = smsStatus;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Outofthehospitalinhospitalinformation getOutHospital() {
		return outHospital;
	}

	public void setOutHospital(Outofthehospitalinhospitalinformation outHospital) {
		this.outHospital = outHospital;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Followup getFollowup() {
		return followup;
	}

	public void setFollowup(Followup followup) {
		this.followup = followup;
	}

	public Integer getFakeStatus() {
		return fakeStatus;
	}

	public void setFakeStatus(Integer fakeStatus) {
		this.fakeStatus = fakeStatus;
	}
}
