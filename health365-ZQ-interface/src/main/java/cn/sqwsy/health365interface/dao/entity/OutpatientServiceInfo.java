package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

public class OutpatientServiceInfo extends PO{

	private Integer id;//Id
	private Patient patient;//患者Id
	private Department department;//科室Id
	private UserInfo doctorinfo;//用户ID(也是门诊医生信息数据)
	private UserInfo userinfo;//操作用户ID(本条数据操作人)
	private Timestamp visit_date;//门诊日期
	private String costs;//总花费
	private String diag_desc;//门诊诊断
	private String patient_HisId;//患者HisId
	private String card_no;//门诊卡号                   
	private String visit_dept;//门诊科室Id(第三方)HIS
	private String doctor_no;//门诊医师Id(第三方)HIS
	private Integer isValid=0;//是否有效 (1是有效0是无效)
	private Integer schedulingState=1;//排期状态1.未排期2.已排期3.勿访4.24小时内出院等5.排期过期6.无需排期(24小时前医生未排期的直接设置成排期过期)
	private String donotvisitthecause_mz;//勿訪原因
	private String errorMsg;//错误原因
	private String visit_no;//就诊序号
	private String clinic_label;//号别
	private Timestamp createTime =  new Timestamp(System.currentTimeMillis());//门诊记录创建时间
	private Timestamp updateTime =  new Timestamp(System.currentTimeMillis());//门诊记录更新时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Timestamp getVisit_date() {
		return visit_date;
	}

	public void setVisit_date(Timestamp visit_date) {
		this.visit_date = visit_date;
	}

	public String getCosts() {
		return costs;
	}

	public void setCosts(String costs) {
		this.costs = costs;
	}

	public String getDiag_desc() {
		return diag_desc;
	}

	public void setDiag_desc(String diag_desc) {
		this.diag_desc = diag_desc;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getPatient_HisId() {
		return patient_HisId;
	}

	public void setPatient_HisId(String patient_HisId) {
		this.patient_HisId = patient_HisId;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getVisit_dept() {
		return visit_dept;
	}

	public void setVisit_dept(String visit_dept) {
		this.visit_dept = visit_dept;
	}

	public String getDoctor_no() {
		return doctor_no;
	}

	public void setDoctor_no(String doctor_no) {
		this.doctor_no = doctor_no;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getSchedulingState() {
		return schedulingState;
	}


	public void setSchedulingState(Integer schedulingState) {
		this.schedulingState = schedulingState;
	}
	
	public String getDonotvisitthecause_mz() {
		return donotvisitthecause_mz;
	}
	public void setDonotvisitthecause_mz(String donotvisitthecause_mz) {
		this.donotvisitthecause_mz = donotvisitthecause_mz;
	}
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public UserInfo getDoctorinfo() {
		return doctorinfo;
	}

	public void setDoctorinfo(UserInfo doctorinfo) {
		this.doctorinfo = doctorinfo;
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
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
	public String getVisit_no() {
		return visit_no;
	}
	public void setVisit_no(String visit_no) {
		this.visit_no = visit_no;
	}
	public String getClinic_label() {
		return clinic_label;
	}
	public void setClinic_label(String clinic_label) {
		this.clinic_label = clinic_label;
	}
}