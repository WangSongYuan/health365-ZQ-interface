/**
 * 
 */
package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

/**
 * @title : his疾病管理视图出院信息数据实体类
 * @description : 
 * @version 1.0
 * @email : liuchang_liu_chang@163.com
 * @author : liuchang
 * @createtime : 2015年8月2日 下午8:26:27
 */
public class RzzyyJbgl extends PO {
	private Integer id;//主键
	private String department;//科室名称
	private String inhospitaldepartment;//住院科室
	private String inhospitaldepartmentid;//住院科室id
	private String outhospitaldepartment;//出院科室
	private String outhospitaldepartmentid;//出院科室id
	private Integer inhospitaldays;//住院天数
	private Timestamp inhospitaldate;//入院日期
	private Timestamp outhospitaldatehome;//首页出院日期
	private Timestamp outhospitaldateclose;//结算出院日期
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
	private String outhospitaldiagnose;//出院诊断
	private String outhospitaldiagnoseicd;//出院诊断ICD码
	private String outhospitinfo;//出院情况
	private String outhospitrecordid;//出院记录编号
	private String drugallergy;//有无药物过敏
	private String allergydrug;//过敏药物
	private String rh;//RH
	private String outhospitaltype;//离院方式
	private String pathologydiagnosename;//病理诊断名称
	private String pathologydiagnosecode;//病理诊断码
	private String inhospitalway;//入院途径
	private String filenumber;//档案号
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
	private String outhospitalotherdiagnosecodefour;//出院其他诊断编码4
	
	private String outhospitalotherdiagnosenamefive;//出院其他诊断名5
	private String outhospitalotherdiagnosecodefive;//出院其他诊断编码5
	
	private String bloodtype;//血型
	private Double owncost;//自费金额
	private Double healthinsurancecost;//医保金额
	private String ageunit;//年龄单位
	private Integer age;//年龄
	private String nation;//民族
	private String name;//姓名
	private String patientphone;//病人电话
	private String companyphone;//病人单位电话
	private String relationphone;//联系人电话
	private Timestamp birthday;//生日
	private String sex;//性别
	private String marry;//婚姻状况
	private String profession;//职业
	private String currentaddress;//现住址
	private String teladdress;//联系住址
	private String company;//工作单位
	private String telname;//联系人名称
	private String relation;//与联系人关系
	private String education;//教育程度
	private Integer ispigeonhole=1;//是否归档 ，1是未归档，2是已归档
	private String cardnum;//身份证号
	private String jobnum;//主治医师工号
	private String patientid_his;//医院HIS系统对应的患者ID
	private String visitnum;//就诊号
	private String bednum;//床号
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	//新加字段
	private Integer isStatus = 1;//1院中 2院后
	private Integer exclusiveInterview=1;//章丘新增随访类别：0科室随访，1疾病与健康管理中心随访 默认疾病与健康管理中心随访
	private Integer chronicDiseaseId;//病种表ID
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the inhospitaldepartment
	 */
	public String getInhospitaldepartment() {
		return inhospitaldepartment;
	}
	/**
	 * @param inhospitaldepartment the inhospitaldepartment to set
	 */
	public void setInhospitaldepartment(String inhospitaldepartment) {
		this.inhospitaldepartment = inhospitaldepartment;
	}
	/**
	 * @return the inhospitaldepartmentid
	 */
	public String getInhospitaldepartmentid() {
		return inhospitaldepartmentid;
	}
	/**
	 * @param inhospitaldepartmentid the inhospitaldepartmentid to set
	 */
	public void setInhospitaldepartmentid(String inhospitaldepartmentid) {
		this.inhospitaldepartmentid = inhospitaldepartmentid;
	}
	/**
	 * @return the outhospitaldepartment
	 */
	public String getOuthospitaldepartment() {
		return outhospitaldepartment;
	}
	/**
	 * @param outhospitaldepartment the outhospitaldepartment to set
	 */
	public void setOuthospitaldepartment(String outhospitaldepartment) {
		this.outhospitaldepartment = outhospitaldepartment;
	}
	/**
	 * @return the outhospitaldepartmentid
	 */
	public String getOuthospitaldepartmentid() {
		return outhospitaldepartmentid;
	}
	/**
	 * @param outhospitaldepartmentid the outhospitaldepartmentid to set
	 */
	public void setOuthospitaldepartmentid(String outhospitaldepartmentid) {
		this.outhospitaldepartmentid = outhospitaldepartmentid;
	}
	/**
	 * @return the inhospitaldays
	 */
	public Integer getInhospitaldays() {
		return inhospitaldays;
	}
	/**
	 * @param inhospitaldays the inhospitaldays to set
	 */
	public void setInhospitaldays(Integer inhospitaldays) {
		this.inhospitaldays = inhospitaldays;
	}
	/**
	 * @return the inhospitaldate
	 */
	public Timestamp getInhospitaldate() {
		return inhospitaldate;
	}
	/**
	 * @param inhospitaldate the inhospitaldate to set
	 */
	public void setInhospitaldate(Timestamp inhospitaldate) {
		this.inhospitaldate = inhospitaldate;
	}
	/**
	 * @return the outhospitaldatehome
	 */
	public Timestamp getOuthospitaldatehome() {
		return outhospitaldatehome;
	}
	/**
	 * @param outhospitaldatehome the outhospitaldatehome to set
	 */
	public void setOuthospitaldatehome(Timestamp outhospitaldatehome) {
		this.outhospitaldatehome = outhospitaldatehome;
	}
	/**
	 * @return the outhospitaldateclose
	 */
	public Timestamp getOuthospitaldateclose() {
		return outhospitaldateclose;
	}
	/**
	 * @param outhospitaldateclose the outhospitaldateclose to set
	 */
	public void setOuthospitaldateclose(Timestamp outhospitaldateclose) {
		this.outhospitaldateclose = outhospitaldateclose;
	}
	/**
	 * @return the closetype
	 */
	public Integer getClosetype() {
		return closetype;
	}
	/**
	 * @param closetype the closetype to set
	 */
	public void setClosetype(Integer closetype) {
		this.closetype = closetype;
	}
	/**
	 * @return the totalcost
	 */
	public Double getTotalcost() {
		return totalcost;
	}
	/**
	 * @param totalcost the totalcost to set
	 */
	public void setTotalcost(Double totalcost) {
		this.totalcost = totalcost;
	}
	/**
	 * @return the maindoctorname
	 */
	public String getMaindoctorname() {
		return maindoctorname;
	}
	/**
	 * @param maindoctorname the maindoctorname to set
	 */
	public void setMaindoctorname(String maindoctorname) {
		this.maindoctorname = maindoctorname;
	}
	/**
	 * @return the maindoctorid
	 */
	public String getMaindoctorid() {
		return maindoctorid;
	}
	/**
	 * @param maindoctorid the maindoctorid to set
	 */
	public void setMaindoctorid(String maindoctorid) {
		this.maindoctorid = maindoctorid;
	}
	/**
	 * @return the doordoctorname
	 */
	public String getDoordoctorname() {
		return doordoctorname;
	}
	/**
	 * @param doordoctorname the doordoctorname to set
	 */
	public void setDoordoctorname(String doordoctorname) {
		this.doordoctorname = doordoctorname;
	}
	/**
	 * @return the doordoctorid
	 */
	public String getDoordoctorid() {
		return doordoctorid;
	}
	/**
	 * @param doordoctorid the doordoctorid to set
	 */
	public void setDoordoctorid(String doordoctorid) {
		this.doordoctorid = doordoctorid;
	}
	/**
	 * @return the costtype
	 */
	public String getCosttype() {
		return costtype;
	}
	/**
	 * @param costtype the costtype to set
	 */
	public void setCosttype(String costtype) {
		this.costtype = costtype;
	}
	/**
	 * @return the inhospitalid
	 */
	public String getInhospitalid() {
		return inhospitalid;
	}
	/**
	 * @param inhospitalid the inhospitalid to set
	 */
	public void setInhospitalid(String inhospitalid) {
		this.inhospitalid = inhospitalid;
	}
	/**
	 * @return the inhospitalcount
	 */
	public Integer getInhospitalcount() {
		return inhospitalcount;
	}
	/**
	 * @param inhospitalcount the inhospitalcount to set
	 */
	public void setInhospitalcount(Integer inhospitalcount) {
		this.inhospitalcount = inhospitalcount;
	}
	/**
	 * @return the inhospitaltag
	 */
	public Integer getInhospitaltag() {
		return inhospitaltag;
	}
	/**
	 * @param inhospitaltag the inhospitaltag to set
	 */
	public void setInhospitaltag(Integer inhospitaltag) {
		this.inhospitaltag = inhospitaltag;
	}
	/**
	 * @return the outhospitaldiagnose
	 */
	public String getOuthospitaldiagnose() {
		return outhospitaldiagnose;
	}
	/**
	 * @param outhospitaldiagnose the outhospitaldiagnose to set
	 */
	public void setOuthospitaldiagnose(String outhospitaldiagnose) {
		this.outhospitaldiagnose = outhospitaldiagnose;
	}
	/**
	 * @return the outhospitaldiagnoseicd
	 */
	public String getOuthospitaldiagnoseicd() {
		return outhospitaldiagnoseicd;
	}
	/**
	 * @param outhospitaldiagnoseicd the outhospitaldiagnoseicd to set
	 */
	public void setOuthospitaldiagnoseicd(String outhospitaldiagnoseicd) {
		this.outhospitaldiagnoseicd = outhospitaldiagnoseicd;
	}
	/**
	 * @return the outhospitinfo
	 */
	public String getOuthospitinfo() {
		return outhospitinfo;
	}
	/**
	 * @param outhospitinfo the outhospitinfo to set
	 */
	public void setOuthospitinfo(String outhospitinfo) {
		this.outhospitinfo = outhospitinfo;
	}
	/**
	 * @return the outhospitrecordid
	 */
	public String getOuthospitrecordid() {
		return outhospitrecordid;
	}
	/**
	 * @param outhospitrecordid the outhospitrecordid to set
	 */
	public void setOuthospitrecordid(String outhospitrecordid) {
		this.outhospitrecordid = outhospitrecordid;
	}
	/**
	 * @return the drugallergy
	 */
	public String getDrugallergy() {
		return drugallergy;
	}
	/**
	 * @param drugallergy the drugallergy to set
	 */
	public void setDrugallergy(String drugallergy) {
		this.drugallergy = drugallergy;
	}
	/**
	 * @return the allergydrug
	 */
	public String getAllergydrug() {
		return allergydrug;
	}
	/**
	 * @param allergydrug the allergydrug to set
	 */
	public void setAllergydrug(String allergydrug) {
		this.allergydrug = allergydrug;
	}
	/**
	 * @return the rh
	 */
	public String getRh() {
		return rh;
	}
	/**
	 * @param rh the rh to set
	 */
	public void setRh(String rh) {
		this.rh = rh;
	}
	/**
	 * @return the outhospitaltype
	 */
	public String getOuthospitaltype() {
		return outhospitaltype;
	}
	/**
	 * @param outhospitaltype the outhospitaltype to set
	 */
	public void setOuthospitaltype(String outhospitaltype) {
		this.outhospitaltype = outhospitaltype;
	}
	/**
	 * @return the pathologydiagnosename
	 */
	public String getPathologydiagnosename() {
		return pathologydiagnosename;
	}
	/**
	 * @param pathologydiagnosename the pathologydiagnosename to set
	 */
	public void setPathologydiagnosename(String pathologydiagnosename) {
		this.pathologydiagnosename = pathologydiagnosename;
	}
	/**
	 * @return the pathologydiagnosecode
	 */
	public String getPathologydiagnosecode() {
		return pathologydiagnosecode;
	}
	/**
	 * @param pathologydiagnosecode the pathologydiagnosecode to set
	 */
	public void setPathologydiagnosecode(String pathologydiagnosecode) {
		this.pathologydiagnosecode = pathologydiagnosecode;
	}
	/**
	 * @return the inhospitalway
	 */
	public String getInhospitalway() {
		return inhospitalway;
	}
	/**
	 * @param inhospitalway the inhospitalway to set
	 */
	public void setInhospitalway(String inhospitalway) {
		this.inhospitalway = inhospitalway;
	}
	/**
	 * @return the outhospitalchinadoctordiagnosediseasname
	 */
	public String getOuthospitalchinadoctordiagnosediseasname() {
		return outhospitalchinadoctordiagnosediseasname;
	}
	/**
	 * @param outhospitalchinadoctordiagnosediseasname the outhospitalchinadoctordiagnosediseasname to set
	 */
	public void setOuthospitalchinadoctordiagnosediseasname(String outhospitalchinadoctordiagnosediseasname) {
		this.outhospitalchinadoctordiagnosediseasname = outhospitalchinadoctordiagnosediseasname;
	}
	/**
	 * @return the outhospitalchinadoctordiagnosediseascode
	 */
	public String getOuthospitalchinadoctordiagnosediseascode() {
		return outhospitalchinadoctordiagnosediseascode;
	}
	/**
	 * @param outhospitalchinadoctordiagnosediseascode the outhospitalchinadoctordiagnosediseascode to set
	 */
	public void setOuthospitalchinadoctordiagnosediseascode(String outhospitalchinadoctordiagnosediseascode) {
		this.outhospitalchinadoctordiagnosediseascode = outhospitalchinadoctordiagnosediseascode;
	}
	/**
	 * @return the outhospitalchinadoctordiagnosecardname
	 */
	public String getOuthospitalchinadoctordiagnosecardname() {
		return outhospitalchinadoctordiagnosecardname;
	}
	/**
	 * @param outhospitalchinadoctordiagnosecardname the outhospitalchinadoctordiagnosecardname to set
	 */
	public void setOuthospitalchinadoctordiagnosecardname(String outhospitalchinadoctordiagnosecardname) {
		this.outhospitalchinadoctordiagnosecardname = outhospitalchinadoctordiagnosecardname;
	}
	/**
	 * @return the outhospitalchinadoctordiagnosecardcode
	 */
	public String getOuthospitalchinadoctordiagnosecardcode() {
		return outhospitalchinadoctordiagnosecardcode;
	}
	/**
	 * @param outhospitalchinadoctordiagnosecardcode the outhospitalchinadoctordiagnosecardcode to set
	 */
	public void setOuthospitalchinadoctordiagnosecardcode(String outhospitalchinadoctordiagnosecardcode) {
		this.outhospitalchinadoctordiagnosecardcode = outhospitalchinadoctordiagnosecardcode;
	}
	/**
	 * @return the mainoperationname
	 */
	public String getMainoperationname() {
		return mainoperationname;
	}
	/**
	 * @param mainoperationname the mainoperationname to set
	 */
	public void setMainoperationname(String mainoperationname) {
		this.mainoperationname = mainoperationname;
	}
	/**
	 * @return the mainoperationcode
	 */
	public String getMainoperationcode() {
		return mainoperationcode;
	}
	/**
	 * @param mainoperationcode the mainoperationcode to set
	 */
	public void setMainoperationcode(String mainoperationcode) {
		this.mainoperationcode = mainoperationcode;
	}
	/**
	 * @return the otheroperationnameone
	 */
	public String getOtheroperationnameone() {
		return otheroperationnameone;
	}
	/**
	 * @param otheroperationnameone the otheroperationnameone to set
	 */
	public void setOtheroperationnameone(String otheroperationnameone) {
		this.otheroperationnameone = otheroperationnameone;
	}
	/**
	 * @return the otheroperationcodeone
	 */
	public String getOtheroperationcodeone() {
		return otheroperationcodeone;
	}
	/**
	 * @param otheroperationcodeone the otheroperationcodeone to set
	 */
	public void setOtheroperationcodeone(String otheroperationcodeone) {
		this.otheroperationcodeone = otheroperationcodeone;
	}
	/**
	 * @return the otheroperationnametwo
	 */
	public String getOtheroperationnametwo() {
		return otheroperationnametwo;
	}
	/**
	 * @param otheroperationnametwo the otheroperationnametwo to set
	 */
	public void setOtheroperationnametwo(String otheroperationnametwo) {
		this.otheroperationnametwo = otheroperationnametwo;
	}
	/**
	 * @return the otheroperationcodetwo
	 */
	public String getOtheroperationcodetwo() {
		return otheroperationcodetwo;
	}
	/**
	 * @param otheroperationcodetwo the otheroperationcodetwo to set
	 */
	public void setOtheroperationcodetwo(String otheroperationcodetwo) {
		this.otheroperationcodetwo = otheroperationcodetwo;
	}
	/**
	 * @return the otheroperationnamethree
	 */
	public String getOtheroperationnamethree() {
		return otheroperationnamethree;
	}
	/**
	 * @param otheroperationnamethree the otheroperationnamethree to set
	 */
	public void setOtheroperationnamethree(String otheroperationnamethree) {
		this.otheroperationnamethree = otheroperationnamethree;
	}
	/**
	 * @return the otheroperationcodethree
	 */
	public String getOtheroperationcodethree() {
		return otheroperationcodethree;
	}
	/**
	 * @param otheroperationcodethree the otheroperationcodethree to set
	 */
	public void setOtheroperationcodethree(String otheroperationcodethree) {
		this.otheroperationcodethree = otheroperationcodethree;
	}
	/**
	 * @return the otheroperationnamefour
	 */
	public String getOtheroperationnamefour() {
		return otheroperationnamefour;
	}
	/**
	 * @param otheroperationnamefour the otheroperationnamefour to set
	 */
	public void setOtheroperationnamefour(String otheroperationnamefour) {
		this.otheroperationnamefour = otheroperationnamefour;
	}
	/**
	 * @return the otheroperationcodefour
	 */
	public String getOtheroperationcodefour() {
		return otheroperationcodefour;
	}
	/**
	 * @param otheroperationcodefour the otheroperationcodefour to set
	 */
	public void setOtheroperationcodefour(String otheroperationcodefour) {
		this.otheroperationcodefour = otheroperationcodefour;
	}
	/**
	 * @return the outhospitalotherdiagnosenameone
	 */
	public String getOuthospitalotherdiagnosenameone() {
		return outhospitalotherdiagnosenameone;
	}
	/**
	 * @param outhospitalotherdiagnosenameone the outhospitalotherdiagnosenameone to set
	 */
	public void setOuthospitalotherdiagnosenameone(String outhospitalotherdiagnosenameone) {
		this.outhospitalotherdiagnosenameone = outhospitalotherdiagnosenameone;
	}
	/**
	 * @return the outhospitalotherdiagnosecodeone
	 */
	public String getOuthospitalotherdiagnosecodeone() {
		return outhospitalotherdiagnosecodeone;
	}
	/**
	 * @param outhospitalotherdiagnosecodeone the outhospitalotherdiagnosecodeone to set
	 */
	public void setOuthospitalotherdiagnosecodeone(String outhospitalotherdiagnosecodeone) {
		this.outhospitalotherdiagnosecodeone = outhospitalotherdiagnosecodeone;
	}
	/**
	 * @return the outhospitalotherdiagnosenametwo
	 */
	public String getOuthospitalotherdiagnosenametwo() {
		return outhospitalotherdiagnosenametwo;
	}
	/**
	 * @param outhospitalotherdiagnosenametwo the outhospitalotherdiagnosenametwo to set
	 */
	public void setOuthospitalotherdiagnosenametwo(String outhospitalotherdiagnosenametwo) {
		this.outhospitalotherdiagnosenametwo = outhospitalotherdiagnosenametwo;
	}
	/**
	 * @return the outhospitalotherdiagnosecodetwo
	 */
	public String getOuthospitalotherdiagnosecodetwo() {
		return outhospitalotherdiagnosecodetwo;
	}
	/**
	 * @param outhospitalotherdiagnosecodetwo the outhospitalotherdiagnosecodetwo to set
	 */
	public void setOuthospitalotherdiagnosecodetwo(String outhospitalotherdiagnosecodetwo) {
		this.outhospitalotherdiagnosecodetwo = outhospitalotherdiagnosecodetwo;
	}
	/**
	 * @return the outhospitalotherdiagnosenamethree
	 */
	public String getOuthospitalotherdiagnosenamethree() {
		return outhospitalotherdiagnosenamethree;
	}
	/**
	 * @param outhospitalotherdiagnosenamethree the outhospitalotherdiagnosenamethree to set
	 */
	public void setOuthospitalotherdiagnosenamethree(String outhospitalotherdiagnosenamethree) {
		this.outhospitalotherdiagnosenamethree = outhospitalotherdiagnosenamethree;
	}
	/**
	 * @return the outhospitalotherdiagnosecodethree
	 */
	public String getOuthospitalotherdiagnosecodethree() {
		return outhospitalotherdiagnosecodethree;
	}
	/**
	 * @param outhospitalotherdiagnosecodethree the outhospitalotherdiagnosecodethree to set
	 */
	public void setOuthospitalotherdiagnosecodethree(String outhospitalotherdiagnosecodethree) {
		this.outhospitalotherdiagnosecodethree = outhospitalotherdiagnosecodethree;
	}
	/**
	 * @return the outhospitalotherdiagnosenamefour
	 */
	public String getOuthospitalotherdiagnosenamefour() {
		return outhospitalotherdiagnosenamefour;
	}
	/**
	 * @param outhospitalotherdiagnosenamefour the outhospitalotherdiagnosenamefour to set
	 */
	public void setOuthospitalotherdiagnosenamefour(String outhospitalotherdiagnosenamefour) {
		this.outhospitalotherdiagnosenamefour = outhospitalotherdiagnosenamefour;
	}
	/**
	 * @return the outhospitalotherdiagnosecodefour
	 */
	public String getOuthospitalotherdiagnosecodefour() {
		return outhospitalotherdiagnosecodefour;
	}
	/**
	 * @param outhospitalotherdiagnosecodefour the outhospitalotherdiagnosecodefour to set
	 */
	public void setOuthospitalotherdiagnosecodefour(String outhospitalotherdiagnosecodefour) {
		this.outhospitalotherdiagnosecodefour = outhospitalotherdiagnosecodefour;
	}
	/**
	 * @return the outhospitalotherdiagnosenamefive
	 */
	public String getOuthospitalotherdiagnosenamefive() {
		return outhospitalotherdiagnosenamefive;
	}
	/**
	 * @param outhospitalotherdiagnosenamefive the outhospitalotherdiagnosenamefive to set
	 */
	public void setOuthospitalotherdiagnosenamefive(String outhospitalotherdiagnosenamefive) {
		this.outhospitalotherdiagnosenamefive = outhospitalotherdiagnosenamefive;
	}
	/**
	 * @return the outhospitalotherdiagnosecodefive
	 */
	public String getOuthospitalotherdiagnosecodefive() {
		return outhospitalotherdiagnosecodefive;
	}
	/**
	 * @param outhospitalotherdiagnosecodefive the outhospitalotherdiagnosecodefive to set
	 */
	public void setOuthospitalotherdiagnosecodefive(String outhospitalotherdiagnosecodefive) {
		this.outhospitalotherdiagnosecodefive = outhospitalotherdiagnosecodefive;
	}
	/**
	 * @return the bloodtype
	 */
	public String getBloodtype() {
		return bloodtype;
	}
	/**
	 * @param bloodtype the bloodtype to set
	 */
	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}
	/**
	 * @return the owncost
	 */
	public Double getOwncost() {
		return owncost;
	}
	/**
	 * @param owncost the owncost to set
	 */
	public void setOwncost(Double owncost) {
		this.owncost = owncost;
	}
	/**
	 * @return the healthinsurancecost
	 */
	public Double getHealthinsurancecost() {
		return healthinsurancecost;
	}
	/**
	 * @param healthinsurancecost the healthinsurancecost to set
	 */
	public void setHealthinsurancecost(Double healthinsurancecost) {
		this.healthinsurancecost = healthinsurancecost;
	}
	/**
	 * @return the ageunit
	 */
	public String getAgeunit() {
		return ageunit;
	}
	/**
	 * @param ageunit the ageunit to set
	 */
	public void setAgeunit(String ageunit) {
		this.ageunit = ageunit;
	}
	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * @return the nation
	 */
	public String getNation() {
		return nation;
	}
	/**
	 * @param nation the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the patientphone
	 */
	public String getPatientphone() {
		return patientphone;
	}
	/**
	 * @param patientphone the patientphone to set
	 */
	public void setPatientphone(String patientphone) {
		this.patientphone = patientphone;
	}
	/**
	 * @return the companyphone
	 */
	public String getCompanyphone() {
		return companyphone;
	}
	/**
	 * @param companyphone the companyphone to set
	 */
	public void setCompanyphone(String companyphone) {
		this.companyphone = companyphone;
	}
	/**
	 * @return the relationphone
	 */
	public String getRelationphone() {
		return relationphone;
	}
	/**
	 * @param relationphone the relationphone to set
	 */
	public void setRelationphone(String relationphone) {
		this.relationphone = relationphone;
	}
	/**
	 * @return the birthday
	 */
	public Timestamp getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the marry
	 */
	public String getMarry() {
		return marry;
	}
	/**
	 * @param marry the marry to set
	 */
	public void setMarry(String marry) {
		this.marry = marry;
	}
	/**
	 * @return the profession
	 */
	public String getProfession() {
		return profession;
	}
	/**
	 * @param profession the profession to set
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}
	/**
	 * @return the currentaddress
	 */
	public String getCurrentaddress() {
		return currentaddress;
	}
	/**
	 * @param currentaddress the currentaddress to set
	 */
	public void setCurrentaddress(String currentaddress) {
		this.currentaddress = currentaddress;
	}
	/**
	 * @return the teladdress
	 */
	public String getTeladdress() {
		return teladdress;
	}
	/**
	 * @param teladdress the teladdress to set
	 */
	public void setTeladdress(String teladdress) {
		this.teladdress = teladdress;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the telname
	 */
	public String getTelname() {
		return telname;
	}
	/**
	 * @param telname the telname to set
	 */
	public void setTelname(String telname) {
		this.telname = telname;
	}
	/**
	 * @return the relation
	 */
	public String getRelation() {
		return relation;
	}
	/**
	 * @param relation the relation to set
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}
	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}
	/**
	 * @param education the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}
	/**
	 * @return the createtime
	 */
	public Timestamp getCreatetime() {
		return createtime;
	}
	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	/**
	 * @return the updatetime
	 */
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	/**
	 * @param updatetime the updatetime to set
	 */
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public String getFilenumber() {
		return filenumber;
	}
	public void setFilenumber(String filenumber) {
		this.filenumber = filenumber;
	}
	public Integer getIspigeonhole() {
		return ispigeonhole;
	}
	public void setIspigeonhole(Integer ispigeonhole) {
		this.ispigeonhole = ispigeonhole;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getJobnum() {
		return jobnum;
	}
	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}
	public Integer getIsStatus() {
		return isStatus;
	}
	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}
	public String getPatientid_his() {
		return patientid_his;
	}
	public void setPatientid_his(String patientid_his) {
		this.patientid_his = patientid_his;
	}
	public String getVisitnum() {
		return visitnum;
	}
	public void setVisitnum(String visitnum) {
		this.visitnum = visitnum;
	}
	public String getBednum() {
		return bednum;
	}
	public void setBednum(String bednum) {
		this.bednum = bednum;
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
}
