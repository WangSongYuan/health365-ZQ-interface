package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

public class Outofthehospitalinhospitalinformation extends PO{

	private Integer id;//主键
	private Patient patient;//患者Id
	
	private String cardnum;//身份证号
	private Integer departmentid;//科室ID
	private String inhospitaldepartment;//住院科室
	private String inhospitaldepartmentid;//住院科室id
	private String outhospitaldepartment;//出院科室
	private String outhospitaldepartmentid;//出院科室id
	private Integer inhospitaldays;//住院天数
	private Timestamp inhospitaldate;//入院日期
	private Timestamp outhospitaldate;//出院日期
	private Timestamp outhospitaldateclose;//出院日期结算
	private Integer closetype;//结算类型
	private Double totalcost;//总花费
	private String maindoctorname;//主管医师
	private String maindoctorid;//主管医师ID
	private String doordoctorname;//门诊医师
	private String doordoctorid;//门诊医师ID
	private String costtype;//费用类型
	private String inhospitalid;//住院号
	private Integer inhospitalcount;//住院次数
	private Integer inhospitaltag;//住院标识
	private String filenumber;//档案号
	private String outhospitaldiagnose;//出院诊断
	private String outhospitaldiagnoseall;//所有出院诊断
	private String outhospitaldiagnoseicd;//出院诊断ICD码
	private String outhospitinfo;//出院情况
	private String outhospitrecordid;//出院记录编号outhospitrecordid
	private String drugallergy;//有无药物过敏
	private String allergydrug;//过敏药物
	private String bloodtype;//血型
	private String rh;//RH
	private String outhospitaltype;//离院方式(医嘱离院,医嘱转院,医嘱转社区卫生服务机构/乡镇卫生院,非医嘱离院,死亡,其他)
	private Integer Schedulingstate=1;//排期状态1.未排期2.已排期3.勿访4.24小时内出院等5.排期过期(24小时前医生未排期的直接设置成排期过期)
	private String donotvisitthecause;//勿訪原因
	private String pathologydiagnosename;//病理诊断名称
	private String pathologydiagnosecode;//病理诊断码
	private String inhospitalway;//入院途径
	private String outhospitalchinadoctordiagnosediseasname;//出院中医诊断疾病名称
	private String outhospitalchinadoctordiagnosediseascode;//出院中医诊断疾病名称编码
	private String outhospitalchinadoctordiagnosecardname;//出院中医诊断证型
	private String outhospitalchinadoctordiagnosecardcode;//出院中医诊断证型编码
	private String mainoperationname;//主要手术名称
	private String mainoperationcode;//主要手术编码
	private String otheroperationnameone;//其他手术名称1
	private String otheroperationcodeone;//其他手术编码1
	
	private String otheroperationnametwo;//其他手术名称2
	private String otheroperationcodetwo;//其他手术编码2
	
	private String otheroperationnamethree;//其他手术名称3
	private String otheroperationcodethree;//其他手术编码3
	
	private String otheroperationnamefour;//其他手术名称4
	private String otheroperationcodefour;//其他手术编码4
	
	private String outhospitalotherdiagnosenameone;//出院其他诊断名1
	private String outhospitalotherdiagnosecodeone;//出院其他诊断编码1
	
	private String outhospitalotherdiagnosenametwo;//出院其他诊断名2
	private String outhospitalotherdiagnosecodetwo;//出院其他诊断编码2
	
	private String outhospitalotherdiagnosenamethree;//出院其他诊断名3
	private String outhospitalotherdiagnosecodethree;//出院其他诊断编码3
	
	private String outhospitalotherdiagnosenamefour;//出院其他诊断名4
	private String outhospitalotherdiagnosecodefour;//出院其他诊断	编码4
	
	private String outhospitalotherdiagnosenamefive;//出院其他诊断名5
	private String outhospitalotherdiagnosecodefive;//出院其他诊断编码5
	private Double owncost;//自费金额
	private Double healthinsurancecost;//医保金额
	/**
	 * 后加字段
	 */
	private Integer hisrecordstate=0;//1.院中2.出院(针对HIS)
	private Integer healthrecordstate=0;//1.院中2.出院(针对自己系统)
	private String patientid_his;;//医院HIS系统对应的患者ID
	private Integer recordoperator;//记录操作人(User表ID)
	/**********院中独特字段***********/
	private Integer taskstatus=1;//1.未处理  2.已处理 3.终止处理 4.删除记录
	private Integer recordcollector;//记录还原人 
	private String taskreason;//任务原因(正常)[已处理、终止处理、删除记录的原因]
	private String reasonsforreduction;//还原原因
	private String inhospitaremarks;//住院信息备注
	private String managementrecordstate="未记录";//管理记录状态  (未记录\管理中\归档)
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	
	private Integer exclusiveInterview;//章丘新增随访类别：0科室随访，1疾病与健康管理中心随访
	private Integer chronicDiseaseId;//病种表ID
	///章丘------------栗
	private Integer zcxzstate=1;//1：未下转2.已下转
	private Integer hospitaldictionaryid;//医院字典表ID 转出后才会设置
	private String hospitalcoding;//转出的医院编码
	private Timestamp referralTime;//wangsongyuan 新增下转时间 20190212
	public String getOuthospitaldiagnoseall() {
		return outhospitaldiagnoseall;
	}
	public void setOuthospitaldiagnoseall(String outhospitaldiagnoseall) {
		this.outhospitaldiagnoseall = outhospitaldiagnoseall;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Integer getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}
	public String getInhospitaldepartment() {
		return inhospitaldepartment;
	}
	public void setInhospitaldepartment(String inhospitaldepartment) {
		this.inhospitaldepartment = inhospitaldepartment;
	}
	public String getInhospitaldepartmentid() {
		return inhospitaldepartmentid;
	}
	public String getOuthospitaldepartment() {
		return outhospitaldepartment;
	}
	public void setOuthospitaldepartment(String outhospitaldepartment) {
		this.outhospitaldepartment = outhospitaldepartment;
	}
	public String getOuthospitaldepartmentid() {
		return outhospitaldepartmentid;
	}
	public void setOuthospitaldepartmentid(String outhospitaldepartmentid) {
		this.outhospitaldepartmentid = outhospitaldepartmentid;
	}
	public void setInhospitaldepartmentid(String inhospitaldepartmentid) {
		this.inhospitaldepartmentid = inhospitaldepartmentid;
	}
	public Integer getInhospitaldays() {
		return inhospitaldays;
	}
	public void setInhospitaldays(Integer inhospitaldays) {
		this.inhospitaldays = inhospitaldays;
	}
	public Timestamp getInhospitaldate() {
		return inhospitaldate;
	}
	public void setInhospitaldate(Timestamp inhospitaldate) {
		this.inhospitaldate = inhospitaldate;
	}
	public Timestamp getOuthospitaldate() {
		return outhospitaldate;
	}
	public void setOuthospitaldate(Timestamp outhospitaldate) {
		this.outhospitaldate = outhospitaldate;
	}
	public Timestamp getOuthospitaldateclose() {
		return outhospitaldateclose;
	}
	public void setOuthospitaldateclose(Timestamp outhospitaldateclose) {
		this.outhospitaldateclose = outhospitaldateclose;
	}
	public Integer getClosetype() {
		return closetype;
	}
	public void setClosetype(Integer closetype) {
		this.closetype = closetype;
	}
	public Double getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(Double totalcost) {
		this.totalcost = totalcost;
	}
	public String getMaindoctorname() {
		return maindoctorname;
	}
	public void setMaindoctorname(String maindoctorname) {
		this.maindoctorname = maindoctorname;
	}
	public String getMaindoctorid() {
		return maindoctorid;
	}
	public void setMaindoctorid(String maindoctorid) {
		this.maindoctorid = maindoctorid;
	}
	public String getDoordoctorname() {
		return doordoctorname;
	}
	public void setDoordoctorname(String doordoctorname) {
		this.doordoctorname = doordoctorname;
	}
	public String getDoordoctorid() {
		return doordoctorid;
	}
	public void setDoordoctorid(String doordoctorid) {
		this.doordoctorid = doordoctorid;
	}
	public String getCosttype() {
		return costtype;
	}
	public void setCosttype(String costtype) {
		this.costtype = costtype;
	}
	public String getInhospitalid() {
		return inhospitalid;
	}
	public void setInhospitalid(String inhospitalid) {
		this.inhospitalid = inhospitalid;
	}
	public Integer getInhospitalcount() {
		return inhospitalcount;
	}
	public void setInhospitalcount(Integer inhospitalcount) {
		this.inhospitalcount = inhospitalcount;
	}
	public Integer getInhospitaltag() {
		return inhospitaltag;
	}
	public void setInhospitaltag(Integer inhospitaltag) {
		this.inhospitaltag = inhospitaltag;
	}
	public String getFilenumber() {
		return filenumber;
	}
	public void setFilenumber(String filenumber) {
		this.filenumber = filenumber;
	}
	public String getOuthospitaldiagnose() {
		return outhospitaldiagnose;
	}
	public void setOuthospitaldiagnose(String outhospitaldiagnose) {
		this.outhospitaldiagnose = outhospitaldiagnose;
	}
	public String getOuthospitaldiagnoseicd() {
		return outhospitaldiagnoseicd;
	}
	public void setOuthospitaldiagnoseicd(String outhospitaldiagnoseicd) {
		this.outhospitaldiagnoseicd = outhospitaldiagnoseicd;
	}
	public String getOuthospitinfo() {
		return outhospitinfo;
	}
	public void setOuthospitinfo(String outhospitinfo) {
		this.outhospitinfo = outhospitinfo;
	}
	public String getOuthospitrecordid() {
		return outhospitrecordid;
	}
	public void setOuthospitrecordid(String outhospitrecordid) {
		this.outhospitrecordid = outhospitrecordid;
	}
	public String getDrugallergy() {
		return drugallergy;
	}
	public void setDrugallergy(String drugallergy) {
		this.drugallergy = drugallergy;
	}
	public String getAllergydrug() {
		return allergydrug;
	}
	public void setAllergydrug(String allergydrug) {
		this.allergydrug = allergydrug;
	}
	public String getBloodtype() {
		return bloodtype;
	}
	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}
	public String getRh() {
		return rh;
	}
	public void setRh(String rh) {
		this.rh = rh;
	}
	public String getOuthospitaltype() {
		return outhospitaltype;
	}
	public void setOuthospitaltype(String outhospitaltype) {
		this.outhospitaltype = outhospitaltype;
	}
	public Integer getSchedulingstate() {
		return Schedulingstate;
	}
	public void setSchedulingstate(Integer schedulingstate) {
		Schedulingstate = schedulingstate;
	}
	
	public String getDonotvisitthecause() {
		return donotvisitthecause;
	}
	public void setDonotvisitthecause(String donotvisitthecause) {
		this.donotvisitthecause = donotvisitthecause;
	}
	public String getPathologydiagnosename() {
		return pathologydiagnosename;
	}
	public void setPathologydiagnosename(String pathologydiagnosename) {
		this.pathologydiagnosename = pathologydiagnosename;
	}
	public String getPathologydiagnosecode() {
		return pathologydiagnosecode;
	}
	public void setPathologydiagnosecode(String pathologydiagnosecode) {
		this.pathologydiagnosecode = pathologydiagnosecode;
	}
	public String getInhospitalway() {
		return inhospitalway;
	}
	public void setInhospitalway(String inhospitalway) {
		this.inhospitalway = inhospitalway;
	}
	public String getOuthospitalchinadoctordiagnosediseasname() {
		return outhospitalchinadoctordiagnosediseasname;
	}
	public void setOuthospitalchinadoctordiagnosediseasname(String outhospitalchinadoctordiagnosediseasname) {
		this.outhospitalchinadoctordiagnosediseasname = outhospitalchinadoctordiagnosediseasname;
	}
	public String getOuthospitalchinadoctordiagnosediseascode() {
		return outhospitalchinadoctordiagnosediseascode;
	}
	public void setOuthospitalchinadoctordiagnosediseascode(String outhospitalchinadoctordiagnosediseascode) {
		this.outhospitalchinadoctordiagnosediseascode = outhospitalchinadoctordiagnosediseascode;
	}
	public String getOuthospitalchinadoctordiagnosecardname() {
		return outhospitalchinadoctordiagnosecardname;
	}
	public void setOuthospitalchinadoctordiagnosecardname(String outhospitalchinadoctordiagnosecardname) {
		this.outhospitalchinadoctordiagnosecardname = outhospitalchinadoctordiagnosecardname;
	}
	public String getOuthospitalchinadoctordiagnosecardcode() {
		return outhospitalchinadoctordiagnosecardcode;
	}
	public void setOuthospitalchinadoctordiagnosecardcode(String outhospitalchinadoctordiagnosecardcode) {
		this.outhospitalchinadoctordiagnosecardcode = outhospitalchinadoctordiagnosecardcode;
	}
	public String getMainoperationname() {
		return mainoperationname;
	}
	public void setMainoperationname(String mainoperationname) {
		this.mainoperationname = mainoperationname;
	}
	public String getMainoperationcode() {
		return mainoperationcode;
	}
	public void setMainoperationcode(String mainoperationcode) {
		this.mainoperationcode = mainoperationcode;
	}
	public String getOtheroperationnameone() {
		return otheroperationnameone;
	}
	public void setOtheroperationnameone(String otheroperationnameone) {
		this.otheroperationnameone = otheroperationnameone;
	}
	public String getOtheroperationcodeone() {
		return otheroperationcodeone;
	}
	public void setOtheroperationcodeone(String otheroperationcodeone) {
		this.otheroperationcodeone = otheroperationcodeone;
	}
	public String getOtheroperationnametwo() {
		return otheroperationnametwo;
	}
	public void setOtheroperationnametwo(String otheroperationnametwo) {
		this.otheroperationnametwo = otheroperationnametwo;
	}
	public String getOtheroperationcodetwo() {
		return otheroperationcodetwo;
	}
	public void setOtheroperationcodetwo(String otheroperationcodetwo) {
		this.otheroperationcodetwo = otheroperationcodetwo;
	}
	public String getOtheroperationnamethree() {
		return otheroperationnamethree;
	}
	public void setOtheroperationnamethree(String otheroperationnamethree) {
		this.otheroperationnamethree = otheroperationnamethree;
	}
	public String getOtheroperationcodethree() {
		return otheroperationcodethree;
	}
	public void setOtheroperationcodethree(String otheroperationcodethree) {
		this.otheroperationcodethree = otheroperationcodethree;
	}
	public String getOtheroperationnamefour() {
		return otheroperationnamefour;
	}
	public void setOtheroperationnamefour(String otheroperationnamefour) {
		this.otheroperationnamefour = otheroperationnamefour;
	}
	public String getOtheroperationcodefour() {
		return otheroperationcodefour;
	}
	public void setOtheroperationcodefour(String otheroperationcodefour) {
		this.otheroperationcodefour = otheroperationcodefour;
	}
	public String getOuthospitalotherdiagnosenameone() {
		return outhospitalotherdiagnosenameone;
	}
	public void setOuthospitalotherdiagnosenameone(String outhospitalotherdiagnosenameone) {
		this.outhospitalotherdiagnosenameone = outhospitalotherdiagnosenameone;
	}
	public String getOuthospitalotherdiagnosecodeone() {
		return outhospitalotherdiagnosecodeone;
	}
	public void setOuthospitalotherdiagnosecodeone(String outhospitalotherdiagnosecodeone) {
		this.outhospitalotherdiagnosecodeone = outhospitalotherdiagnosecodeone;
	}
	public String getOuthospitalotherdiagnosenametwo() {
		return outhospitalotherdiagnosenametwo;
	}
	public void setOuthospitalotherdiagnosenametwo(String outhospitalotherdiagnosenametwo) {
		this.outhospitalotherdiagnosenametwo = outhospitalotherdiagnosenametwo;
	}
	public String getOuthospitalotherdiagnosecodetwo() {
		return outhospitalotherdiagnosecodetwo;
	}
	public void setOuthospitalotherdiagnosecodetwo(String outhospitalotherdiagnosecodetwo) {
		this.outhospitalotherdiagnosecodetwo = outhospitalotherdiagnosecodetwo;
	}
	public String getOuthospitalotherdiagnosenamethree() {
		return outhospitalotherdiagnosenamethree;
	}
	public void setOuthospitalotherdiagnosenamethree(String outhospitalotherdiagnosenamethree) {
		this.outhospitalotherdiagnosenamethree = outhospitalotherdiagnosenamethree;
	}
	public String getOuthospitalotherdiagnosecodethree() {
		return outhospitalotherdiagnosecodethree;
	}
	public void setOuthospitalotherdiagnosecodethree(String outhospitalotherdiagnosecodethree) {
		this.outhospitalotherdiagnosecodethree = outhospitalotherdiagnosecodethree;
	}
	public String getOuthospitalotherdiagnosenamefour() {
		return outhospitalotherdiagnosenamefour;
	}
	public void setOuthospitalotherdiagnosenamefour(String outhospitalotherdiagnosenamefour) {
		this.outhospitalotherdiagnosenamefour = outhospitalotherdiagnosenamefour;
	}
	public String getOuthospitalotherdiagnosecodefour() {
		return outhospitalotherdiagnosecodefour;
	}
	public void setOuthospitalotherdiagnosecodefour(String outhospitalotherdiagnosecodefour) {
		this.outhospitalotherdiagnosecodefour = outhospitalotherdiagnosecodefour;
	}
	public String getOuthospitalotherdiagnosenamefive() {
		return outhospitalotherdiagnosenamefive;
	}
	public void setOuthospitalotherdiagnosenamefive(String outhospitalotherdiagnosenamefive) {
		this.outhospitalotherdiagnosenamefive = outhospitalotherdiagnosenamefive;
	}
	public String getOuthospitalotherdiagnosecodefive() {
		return outhospitalotherdiagnosecodefive;
	}
	public void setOuthospitalotherdiagnosecodefive(String outhospitalotherdiagnosecodefive) {
		this.outhospitalotherdiagnosecodefive = outhospitalotherdiagnosecodefive;
	}
	public Double getOwncost() {
		return owncost;
	}
	public void setOwncost(Double owncost) {
		this.owncost = owncost;
	}
	public Double getHealthinsurancecost() {
		return healthinsurancecost;
	}
	public void setHealthinsurancecost(Double healthinsurancecost) {
		this.healthinsurancecost = healthinsurancecost;
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
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public Integer getHisrecordstate() {
		return hisrecordstate;
	}
	public void setHisrecordstate(Integer hisrecordstate) {
		this.hisrecordstate = hisrecordstate;
	}
	public Integer getHealthrecordstate() {
		return healthrecordstate;
	}
	public void setHealthrecordstate(Integer healthrecordstate) {
		this.healthrecordstate = healthrecordstate;
	}
	public String getPatientid_his() {
		return patientid_his;
	}
	public void setPatientid_his(String patientid_his) {
		this.patientid_his = patientid_his;
	}
	public Integer getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(Integer taskstatus) {
		this.taskstatus = taskstatus;
	}
	
	public Integer getRecordoperator() {
		return recordoperator;
	}
	public void setRecordoperator(Integer recordoperator) {
		this.recordoperator = recordoperator;
	}
	public Integer getRecordcollector() {
		return recordcollector;
	}
	public void setRecordcollector(Integer recordcollector) {
		this.recordcollector = recordcollector;
	}
	public String getReasonsforreduction() {
		return reasonsforreduction;
	}
	public void setReasonsforreduction(String reasonsforreduction) {
		this.reasonsforreduction = reasonsforreduction;
	}
	public String getTaskreason() {
		return taskreason;
	}
	public void setTaskreason(String taskreason) {
		this.taskreason = taskreason;
	}
	public String getInhospitaremarks() {
		return inhospitaremarks;
	}
	public void setInhospitaremarks(String inhospitaremarks) {
		this.inhospitaremarks = inhospitaremarks;
	}
	public String getManagementrecordstate() {
		return managementrecordstate;
	}
	public void setManagementrecordstate(String managementrecordstate) {
		this.managementrecordstate = managementrecordstate;
	}
	public Integer getExclusiveInterview() {
		return exclusiveInterview;
	}
	public void setExclusiveInterview(Integer exclusiveInterview) {
		this.exclusiveInterview = exclusiveInterview;
	}
	public Integer getChronicDiseaseId() {
		return chronicDiseaseId;
	}
	public void setChronicDiseaseId(Integer chronicDiseaseId) {
		this.chronicDiseaseId = chronicDiseaseId;
	}
	public Integer getZcxzstate() {
		return zcxzstate;
	}
	public void setZcxzstate(Integer zcxzstate) {
		this.zcxzstate = zcxzstate;
	}
	public Integer getHospitaldictionaryid() {
		return hospitaldictionaryid;
	}
	public void setHospitaldictionaryid(Integer hospitaldictionaryid) {
		this.hospitaldictionaryid = hospitaldictionaryid;
	}
	public String getHospitalcoding() {
		return hospitalcoding;
	}
	public void setHospitalcoding(String hospitalcoding) {
		this.hospitalcoding = hospitalcoding;
	}
	public Timestamp getReferralTime() {
		return referralTime;
	}
	public void setReferralTime(Timestamp referralTime) {
		this.referralTime = referralTime;
	}
}
