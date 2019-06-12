package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

public class Patient extends PO{

	private Integer id;//用户ID
	private String cardnum;//身份证号
	private Integer age;//年龄
	private String nation;//民族
	private String name;//姓名
	private String phoneone;//病人自己电话1
	private String phonetwo;//病人自己电话2
	private String phonethree;//病人以修改电话
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
	private String truerelation;//与联系人正确
	private String education;//教育程度
	private String ageunit;//年龄单位
	private transient String costtype;//医保卡类型
	private transient String outhospitaldiagnoseall;//出院诊断
	private String patientHisId;//wangsongyuan 新增章丘HIS患者ID 20180912
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	private Integer wxState=0;//wangsongyuan 新增章丘微信小程序注册状态 20190531 0代表未注册 1代表已注册
	
	public String getOuthospitaldiagnoseall() {
		return outhospitaldiagnoseall;
	}
	public void setOuthospitaldiagnoseall(String outhospitaldiagnoseall) {
		this.outhospitaldiagnoseall = outhospitaldiagnoseall;
	}
	public String getCosttype() {
		return costtype;
	}
	public void setCosttype(String costtype) {
		this.costtype = costtype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cardnum
	 */
	public String getCardnum() {
		return cardnum;
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param cardnum the cardnum to set
	 */
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
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
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the phoneone
	 */
	public String getPhoneone() {
		return phoneone;
	}
	/**
	 * @param phoneone the phoneone to set
	 */
	public void setPhoneone(String phoneone) {
		this.phoneone = phoneone;
	}
	/**
	 * @return the phonetwo
	 */
	public String getPhonetwo() {
		return phonetwo;
	}
	/**
	 * @param phonetwo the phonetwo to set
	 */
	public void setPhonetwo(String phonetwo) {
		this.phonetwo = phonetwo;
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
	/**
	 * @return the phonethree
	 */
	public String getPhonethree() {
		return phonethree;
	}
	/**
	 * @param phonethree the phonethree to set
	 */
	public void setPhonethree(String phonethree) {
		this.phonethree = phonethree;
	}
	public String getAgeunit() {
		return ageunit;
	}
	public void setAgeunit(String ageunit) {
		this.ageunit = ageunit;
	}
	public String getTruerelation() {
		return truerelation;
	}
	public void setTruerelation(String truerelation) {
		this.truerelation = truerelation;
	}
	public String getPatientHisId() {
		return patientHisId;
	}
	public void setPatientHisId(String patientHisId) {
		this.patientHisId = patientHisId;
	}
	public Integer getWxState() {
		return wxState;
	}
	public void setWxState(Integer wxState) {
		this.wxState = wxState;
	}
}