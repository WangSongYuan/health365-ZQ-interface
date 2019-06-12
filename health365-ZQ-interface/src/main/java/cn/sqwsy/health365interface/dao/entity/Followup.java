package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;
import java.util.Date;

public class Followup extends PO{
	private Integer id;//主键
	private Integer patientid;//患者Id
	private Integer departmentid;//科室ID
	private Integer outofthehospitalinhospitalinformationid;//出住院信息表ID(理解为数据源ID)
	private Integer noscheduletemplateinformationid;//排期模版子模版ID,如果不使用模版,这个ID值为空
	private String maindoctorname;//主管医师
	private Date followupdate;//随访日期
	private String datasources="医院患者";//数据来源(默认医院患者)//健康快车   门诊患者
	private String schedulingpersonnelid;//操作人员id(对应USer表ID)
	private String schedulingpersonnel;//排期人员
	private String followupofreason;//随访原因，一般都不填
	private String shenfang="0";//是否慎防
	private String shenfangyuanyin;//慎防原因
	private String consultingreplacementcontent;//咨询交代内容
	private Integer followupstate=1;//随访状态1.未随访2.已随访 3.暂存4.终止随访(俩种情况1.单次终止[这次随访终此]2.完全终止[这此出院对应的全部随访都终止])
	private String terminationoffollowupreason;//终止随访原因
	private String serviceevaluation;//服务评价(满意,不满意,基本满意,非常满意)
	private String acauseofdiscontent;//不满意原因
	private String promptcontent="饮食指导与告知,用药指导与告知,注意事项提示,健康教育,活动休息";//提示内容（饮食指导与告知，用药知道与告知，注意事项提示，健康教育，活动休息）放到数据库逗号分割
	private String patienttoaskquestions;//病人提出问题
	private String problemsolving;//病人问题解决办法
	private String feedbackproblem;//反馈问题
	private String remark;//备注
	private Integer whetherthereferral=0;//是否转诊 1转,0不转
	private String tasktypes;//转诊任务类型(常规任务，紧急任务)
	private Integer Referralstate;//转诊状态 1.已转诊待处理2.已转诊已处理
	private String referralreason;//转诊原因
	private String doctortreatment;//医生处理意见
	private Integer effectivereferral;//是否为有效转诊 1是 2 不是
	private String temporaryreasons;//暂存原因(完成，关机,电话不通,空号,错号,拒接,修改号,多次无人接听,停机,其它)	
	private Timestamp referraltime;//转诊时间
	private Integer shouldbefollowedup;//应随访次数
	private Integer thefollowuptimes;//本次随访次数
	private Integer whethereffectivefollowup;//是否有效随访(1为有效,2为无效)
	private transient Date reservationTime; //用于显示查出来的预约时间
	private String oldsystemohid;//老系统对应的出院记录表ID(第三方)
	private Integer followuptype=1;//默认为1(1.电话随访 2.短信随访)
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//随访信息创建时间(相当于排期创建时间)
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//随访信息更新时间
	//新增专访字段
	private Integer exclusiveInterview=1;//随访类别0，专科随访{章丘目前只有心内科的心脏病}，1，疾病管理中心随访
	private Integer chronicDiseaseid;//病种表ID
	public String getFeedbackproblem() {
		return feedbackproblem;
	}
	public void setFeedbackproblem(String feedbackproblem) {
		this.feedbackproblem = feedbackproblem;
	}
	public Integer getWhethereffectivefollowup() {
		return whethereffectivefollowup;
	}
	public void setWhethereffectivefollowup(Integer whethereffectivefollowup) {
		this.whethereffectivefollowup = whethereffectivefollowup;
	}
	public String getMaindoctorname() {
		return maindoctorname;
	}
	public void setMaindoctorname(String maindoctorname) {
		this.maindoctorname = maindoctorname;
	}
	public Integer getShouldbefollowedup() {
		return shouldbefollowedup;
	}
	public void setShouldbefollowedup(Integer shouldbefollowedup) {
		this.shouldbefollowedup = shouldbefollowedup;
	}
	public Integer getThefollowuptimes() {
		return thefollowuptimes;
	}
	public void setThefollowuptimes(Integer thefollowuptimes) {
		this.thefollowuptimes = thefollowuptimes;
	}
	public String getPromptcontent() {
		return promptcontent;
	}
	public void setPromptcontent(String promptcontent) {
		this.promptcontent = promptcontent;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPatientid() {
		return patientid;
	}
	public void setPatientid(Integer patientid) {
		this.patientid = patientid;
	}
	public Integer getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}
	public Integer getOutofthehospitalinhospitalinformationid() {
		return outofthehospitalinhospitalinformationid;
	}
	public void setOutofthehospitalinhospitalinformationid(Integer outofthehospitalinhospitalinformationid) {
		this.outofthehospitalinhospitalinformationid = outofthehospitalinhospitalinformationid;
	}
	public Integer getNoscheduletemplateinformationid() {
		return noscheduletemplateinformationid;
	}
	public void setNoscheduletemplateinformationid(Integer noscheduletemplateinformationid) {
		this.noscheduletemplateinformationid = noscheduletemplateinformationid;
	}
	public Date getFollowupdate() {
		return followupdate;
	}
	public void setFollowupdate(Date followupdate) {
		this.followupdate = followupdate;
	}
	public String getDatasources() {
		return datasources;
	}
	public void setDatasources(String datasources) {
		this.datasources = datasources;
	}
	public String getSchedulingpersonnelid() {
		return schedulingpersonnelid;
	}
	public void setSchedulingpersonnelid(String schedulingpersonnelid) {
		this.schedulingpersonnelid = schedulingpersonnelid;
	}
	public String getSchedulingpersonnel() {
		return schedulingpersonnel;
	}
	public void setSchedulingpersonnel(String schedulingpersonnel) {
		this.schedulingpersonnel = schedulingpersonnel;
	}
	public String getFollowupofreason() {
		return followupofreason;
	}
	public void setFollowupofreason(String followupofreason) {
		this.followupofreason = followupofreason;
	}
	public String getShenfang() {
		return shenfang;
	}
	public void setShenfang(String shenfang) {
		this.shenfang = shenfang;
	}
	public String getShenfangyuanyin() {
		return shenfangyuanyin;
	}
	public void setShenfangyuanyin(String shenfangyuanyin) {
		this.shenfangyuanyin = shenfangyuanyin;
	}
	public String getConsultingreplacementcontent() {
		return consultingreplacementcontent;
	}
	public void setConsultingreplacementcontent(String consultingreplacementcontent) {
		this.consultingreplacementcontent = consultingreplacementcontent;
	}
	public Integer getFollowupstate() {
		return followupstate;
	}
	public void setFollowupstate(Integer followupstate) {
		this.followupstate = followupstate;
	}
	public String getTerminationoffollowupreason() {
		return terminationoffollowupreason;
	}
	public void setTerminationoffollowupreason(String terminationoffollowupreason) {
		this.terminationoffollowupreason = terminationoffollowupreason;
	}
	public String getServiceevaluation() {
		return serviceevaluation;
	}
	public void setServiceevaluation(String serviceevaluation) {
		this.serviceevaluation = serviceevaluation;
	}
	public String getAcauseofdiscontent() {
		return acauseofdiscontent;
	}
	public void setAcauseofdiscontent(String acauseofdiscontent) {
		this.acauseofdiscontent = acauseofdiscontent;
	}
	public String getPatienttoaskquestions() {
		return patienttoaskquestions;
	}
	public void setPatienttoaskquestions(String patienttoaskquestions) {
		this.patienttoaskquestions = patienttoaskquestions;
	}
	public String getProblemsolving() {
		return problemsolving;
	}
	public void setProblemsolving(String problemsolving) {
		this.problemsolving = problemsolving;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getWhetherthereferral() {
		return whetherthereferral;
	}
	public void setWhetherthereferral(Integer whetherthereferral) {
		this.whetherthereferral = whetherthereferral;
	}
	public String getTasktypes() {
		return tasktypes;
	}
	public void setTasktypes(String tasktypes) {
		this.tasktypes = tasktypes;
	}
	public Integer getReferralstate() {
		return Referralstate;
	}
	public void setReferralstate(Integer referralstate) {
		Referralstate = referralstate;
	}
	public String getReferralreason() {
		return referralreason;
	}
	public void setReferralreason(String referralreason) {
		this.referralreason = referralreason;
	}
	public String getDoctortreatment() {
		return doctortreatment;
	}
	public void setDoctortreatment(String doctortreatment) {
		this.doctortreatment = doctortreatment;
	}
	public Integer getEffectivereferral() {
		return effectivereferral;
	}
	public void setEffectivereferral(Integer effectivereferral) {
		this.effectivereferral = effectivereferral;
	}
	public String getTemporaryreasons() {
		return temporaryreasons;
	}
	public void setTemporaryreasons(String temporaryreasons) {
		this.temporaryreasons = temporaryreasons;
	}
	public Timestamp getReferraltime() {
		return referraltime;
	}
	public void setReferraltime(Timestamp referraltime) {
		this.referraltime = referraltime;
	}
	
	
	public String getOldsystemohid() {
		return oldsystemohid;
	}
	public void setOldsystemohid(String oldsystemohid) {
		this.oldsystemohid = oldsystemohid;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public Date getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}
	public Integer getFollowuptype() {
		return followuptype;
	}
	public void setFollowuptype(Integer followuptype) {
		this.followuptype = followuptype;
	}
	public Integer getExclusiveInterview() {
		return exclusiveInterview;
	}
	public void setExclusiveInterview(Integer exclusiveInterview) {
		this.exclusiveInterview = exclusiveInterview;
	}
	public Integer getChronicDiseaseid() {
		return chronicDiseaseid;
	}
	public void setChronicDiseaseid(Integer chronicDiseaseid) {
		this.chronicDiseaseid = chronicDiseaseid;
	}
}
