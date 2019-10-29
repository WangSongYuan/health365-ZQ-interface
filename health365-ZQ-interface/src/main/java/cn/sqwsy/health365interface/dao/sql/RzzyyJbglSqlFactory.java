package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.RzzyyJbgl;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

public class RzzyyJbglSqlFactory {
	public String getRzzyyJbgl(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("rzzyy_jbgl");
	        if(para.get("patientid_his")!=null&&!para.get("patientid_his").equals("")){
	        	sql.WHERE("patientid_his='"+para.get("patientid_his")+"'");
	        }
	        if(para.get("inhospitalcount")!=null&&!para.get("inhospitalcount").equals("")){
	        	sql.WHERE("inhospitalcount="+para.get("inhospitalcount"));
	        }
	        if(para.get("visitnum")!=null&&!para.get("visitnum").equals("")){
	        	sql.WHERE("visitnum='"+para.get("visitnum")+"'");
	        }
	        return sql.toString();
	}
	
	public String getRzzyyJbglList(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("rzzyy_jbgl");
	        if(para.get("isStatus")!=null&&!para.get("isStatus").equals("")){
	        	sql.WHERE("isStatus='"+para.get("isStatus")+"'");
	        }
	        if(para.get("ispigeonhole")!=null&&!para.get("ispigeonhole").equals("")){
	        	sql.WHERE("ispigeonhole="+para.get("ispigeonhole"));
	        }
	        return sql.toString();
	}
	
	public String setRzzyyJbgl(RzzyyJbgl inhospital){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("rzzyy_jbgl");
	    	
	        if(inhospital.getDepartment()!=null){
	    		sql.VALUES("department", "#{department}");
	    	}
	    	
	    	//住院科室
	    	if(inhospital.getInhospitaldepartment()!=null){
	    		sql.VALUES("inhospitaldepartment", "#{inhospitaldepartment}");
	    	}
	    	
	    	//住院科室ID
	    	if(inhospital.getInhospitaldepartmentid()!=null){
	    		sql.VALUES("inhospitaldepartmentid", "#{inhospitaldepartmentid}");
	    	}

	    	//出院科室
	    	if(inhospital.getOuthospitaldepartment()!=null){
	    		sql.VALUES("outhospitaldepartment", "#{outhospitaldepartment}");
	    	}
	    	
	    	//住院科室ID
	    	if(inhospital.getOuthospitaldepartmentid()!=null){
	    		sql.VALUES("outhospitaldepartmentid", "#{outhospitaldepartmentid}");
	    	}
	    	
	    	//入院日期
	    	if(inhospital.getInhospitaldate()!=null){
	    		sql.VALUES("inhospitaldate", "#{inhospitaldate}");
	    	}
	    	
	    	//首页出院日期
	    	if(inhospital.getOuthospitaldatehome()!=null){
	    		sql.VALUES("outhospitaldatehome", "#{outhospitaldatehome}");
	    	}
	    	
	    	//结算出院日期
	    	if(inhospital.getOuthospitaldateclose()!=null){
	    		sql.VALUES("outhospitaldateclose", "#{outhospitaldateclose}");
	    	}
	    	
	    	//结算类型
	    	if(inhospital.getClosetype()!=null){
	    		sql.VALUES("closetype", "#{closetype}");
	    	}
	    	
	    	//总花费
	    	if(inhospital.getTotalcost()!=null){
	    		sql.VALUES("totalcost", "#{totalcost}");
	    	}
	    	
	    	//主管医师
	    	if(inhospital.getMaindoctorname()!=null){
	    		sql.VALUES("maindoctorname", "#{maindoctorname}");
	    	}
	    	
	    	//主管医师ID
	    	if(inhospital.getMaindoctorid()!=null){
	    		sql.VALUES("maindoctorid", "#{maindoctorid}");
	    	}
	    	
	    	/*
	    	 private String doordoctorname;//门诊医师
	    	 private String doordoctorid;//门诊医师ID
	    	 */	 
	    	
	    	//费用类型
	    	if(inhospital.getCosttype()!=null){
	    		sql.VALUES("costtype", "#{costtype}");
	    	}
	    	
	    	//住院号
	    	if(inhospital.getInhospitalid()!=null){
	    		sql.VALUES("inhospitalid", "#{inhospitalid}");
	    	}
	    	
	    	//住院次数
	    	if(inhospital.getInhospitalcount()!=null){
	    		sql.VALUES("inhospitalcount", "#{inhospitalcount}");
	    	}
	    	
	    	/*
	    	 * private Integer inhospitaltag;//住院标识
	    	 */	
	    	
	    	//出院诊断
	    	if(inhospital.getOuthospitaldiagnose()!=null){
	    		sql.VALUES("outhospitaldiagnose", "#{outhospitaldiagnose}");
	    	}
	    	
	    	//出院诊断ICD码
	    	if(inhospital.getOuthospitaldiagnoseicd()!=null){
	    		sql.VALUES("outhospitaldiagnoseicd", "#{outhospitaldiagnoseicd}");
	    	}
	    	
	    	/*private String outhospitinfo;//出院情况
	    	 */	 
	    	
	    	//出院记录编号
	    	if(inhospital.getOuthospitrecordid()!=null){
	    		sql.VALUES("outhospitrecordid", "#{outhospitrecordid}");
	    	}
	    	
	    	/*private String drugallergy;//有无药物过敏
	    	 */	    
	    	
	    	//过敏药物
	    	if(inhospital.getAllergydrug()!=null){
	    		sql.VALUES("allergydrug", "#{allergydrug}");
	    	}
	    	
	    	//RH
	    	if(inhospital.getRh()!=null){
	    		sql.VALUES("rh", "#{rh}");
	    	}
	    	
	    	//离院方式
	    	if(inhospital.getOuthospitaltype()!=null){
	    		sql.VALUES("outhospitaltype", "#{outhospitaltype}");
	    	}
	    	
	    	/*private String pathologydiagnosename;//病理诊断名称
	    	private String pathologydiagnosecode;//病理诊断码
	    	 */	 
	    	
	    	//入院途径
	    	if(inhospital.getInhospitalway()!=null){
	    		sql.VALUES("inhospitalway", "#{inhospitalway}");
	    	}
	    	
	    	/*private String filenumber;//档案号
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
	    	 */	 
	    	
	    	//血型
	    	if(inhospital.getBloodtype()!=null){
	    		sql.VALUES("bloodtype", "#{bloodtype}");
	    	}
	    	
	    	//自费金额
	    	if(inhospital.getOwncost()!=null){
	    		sql.VALUES("owncost", "#{owncost}");
	    	}
	    	
	    	/*private Double healthinsurancecost;//医保金额
	    	 */	    
	    	
	    	//年龄单位
	    	if(inhospital.getAgeunit()!=null){
	    		sql.VALUES("ageunit", "#{ageunit}");
	    	}
	    	
	    	//年龄
	    	if(inhospital.getAge()!=null){
	    		sql.VALUES("age", "#{age}");
	    	}
	    	
	    	//民族
	    	if(inhospital.getNation()!=null){
	    		sql.VALUES("nation", "#{nation}");
	    	}
	    	
	    	//姓名
	    	if(inhospital.getName()!=null){
	    		sql.VALUES("name", "#{name}");
	    	}
	    	
	    	//病人电话
	    	if(inhospital.getPatientphone()!=null){
	    		sql.VALUES("patientphone", "#{patientphone}");
	    	}
	    	
	    	//病人单位电话
	    	if(inhospital.getCompanyphone()!=null){
	    		sql.VALUES("companyphone", "#{companyphone}");
	    	}
	    	
	    	//联系人电话
	    	if(inhospital.getRelationphone()!=null){
	    		sql.VALUES("relationphone", "#{relationphone}");
	    	}
	    	
	    	//生日
	    	if(inhospital.getBirthday()!=null){
	    		sql.VALUES("birthday", "#{birthday}");
	    	}
	    	
	    	//性别
	    	if(inhospital.getSex()!=null){
	    		sql.VALUES("sex", "#{sex}");
	    	}
	    	
	    	//婚姻状况
	    	if(inhospital.getMarry()!=null){
	    		sql.VALUES("marry", "#{marry}");
	    	}
	    	
	    	//职业
	    	if(inhospital.getProfession()!=null){
	    		sql.VALUES("profession", "#{profession}");
	    	}
	    	
	    	//现住址
	    	if(inhospital.getCurrentaddress()!=null){
	    		sql.VALUES("currentaddress", "#{currentaddress}");
	    	}
	    	
	    	//联系住址
	    	if(inhospital.getTeladdress()!=null){
	    		sql.VALUES("teladdress", "#{teladdress}");
	    	}
	    	
	    	//工作单位
	    	if(inhospital.getCompany()!=null){
	    		sql.VALUES("company", "#{company}");
	    	}
	    	
	    	//联系人名称
	    	if(inhospital.getTelname()!=null){
	    		sql.VALUES("telname", "#{telname}");
	    	}
	    	
	    	//与联系人关系
	    	if(inhospital.getRelation()!=null){
	    		sql.VALUES("relation", "#{relation}");
	    	}
	    	
	    	/*private String education;//教育程度
	    	 */	    	
	    	
	    	//身份证号
	    	if(inhospital.getCardnum()!=null){
	    		sql.VALUES("cardnum", "#{cardnum}");
	    	}
	    	
	    	//主治医师工号
	    	if(inhospital.getJobnum()!=null){
	    		sql.VALUES("jobnum", "#{jobnum}");
	    	}
	    	
	    	//医院HIS系统对应的患者ID
	    	if(inhospital.getPatientid_his()!=null){
	    		sql.VALUES("patientid_his", "#{patientid_his}");
	    	}
	    	
	    	/*private String visitnum;//就诊号
	    	private String bednum;//床号
	    	 */	    	
	    	
	    	//用户信息创建时间
	    	if(inhospital.getCreatetime()!=null){
	    		sql.VALUES("createtime", "#{createtime}");
	    	}
	    	//新加字段
	    	//1院中 2院后
	    	if(inhospital.getIsStatus()!=null){
	    		sql.VALUES("isStatus", "#{isStatus}");
	    	}
	    	
	    	////章丘新增随访类别：0科室随访，1疾病与健康管理中心随访 默认疾病与健康管理中心随访
	    	if(inhospital.getExclusiveInterview()!=null){
	    		sql.VALUES("exclusiveInterview", "#{exclusiveInterview}");
	    	}
	    	
	    	//病种表ID
	    	if(inhospital.getChronicDiseaseId()!=null){
	    		sql.VALUES("chronicDiseaseId", "#{chronicDiseaseId}");
	    	}
	    	sql.VALUES("ispigeonhole", "#{ispigeonhole}");
	    	//广西二附院新增字段
	    	if(ValidateUtil.isNotNull(inhospital.getVisitnum())){
	    		sql.VALUES("visitnum", "#{visitnum}");
	    	}
	    	if(ValidateUtil.isNotNull(inhospital.getPatNo())){
	    		sql.VALUES("PatNo", "#{PatNo}");
	    	}
	    	if(ValidateUtil.isNotNull(inhospital.getCardNo())){
	    		sql.VALUES("CardNo", "#{CardNo}");
	    	}
	        return sql.toString();
	}
	
	
	public String updateRzzyyJbgl(RzzyyJbgl inhospital){
		SQL sql = new SQL();
		sql.UPDATE("rzzyy_jbgl");
		 	/**当前住院科室ID*/
			 if (inhospital.getInhospitaldepartmentid()!=null) {
	             sql.SET("inhospitaldepartmentid = #{inhospitaldepartmentid}");
	         }
			 if(inhospital.getDepartment()!=null){
		    		sql.SET("department = #{department}");
		    	}
		    	
		    	//住院科室
		    	if(inhospital.getInhospitaldepartment()!=null){
		    		sql.SET("inhospitaldepartment = #{inhospitaldepartment}");
		    	}
		    	
		    	//住院科室ID
		    	if(inhospital.getInhospitaldepartmentid()!=null){
		    		sql.SET("inhospitaldepartmentid = #{inhospitaldepartmentid}");
		    	}

		    	//出院科室
		    	if(inhospital.getOuthospitaldepartment()!=null){
		    		sql.SET("outhospitaldepartment = #{outhospitaldepartment}");
		    	}
		    	
		    	//住院科室ID
		    	if(inhospital.getOuthospitaldepartmentid()!=null){
		    		sql.SET("outhospitaldepartmentid = #{outhospitaldepartmentid}");
		    	}
		    	
		    	//入院日期
		    	if(inhospital.getInhospitaldate()!=null){
		    		sql.SET("inhospitaldate = #{inhospitaldate}");
		    	}
		    	
		    	//首页出院日期
		    	if(inhospital.getOuthospitaldatehome()!=null){
		    		sql.SET("outhospitaldatehome = #{outhospitaldatehome}");
		    	}
		    	
		    	//结算出院日期
		    	if(inhospital.getOuthospitaldateclose()!=null){
		    		sql.SET("outhospitaldateclose = #{outhospitaldateclose}");
		    	}
		    	
		    	//结算类型
		    	if(inhospital.getClosetype()!=null){
		    		sql.SET("closetype = #{closetype}");
		    	}
		    	
		    	//总花费
		    	if(inhospital.getTotalcost()!=null){
		    		sql.SET("totalcost = #{totalcost}");
		    	}
		    	
		    	//主管医师
		    	if(inhospital.getMaindoctorname()!=null){
		    		sql.SET("maindoctorname = #{maindoctorname}");
		    	}
		    	
		    	//主管医师ID
		    	if(inhospital.getMaindoctorid()!=null){
		    		sql.SET("maindoctorid = #{maindoctorid}");
		    	}
		    	
		    	/*
		    	 private String doordoctorname;//门诊医师
		    	 private String doordoctorid;//门诊医师ID
		    	 */	 
		    	
		    	//费用类型
		    	if(inhospital.getCosttype()!=null){
		    		sql.SET("costtype = #{costtype}");
		    	}
		    	
		    	//住院号
		    	if(inhospital.getInhospitalid()!=null){
		    		sql.SET("inhospitalid = #{inhospitalid}");
		    	}
		    	
		    	//住院次数
		    	if(inhospital.getInhospitalcount()!=null){
		    		sql.SET("inhospitalcount = #{inhospitalcount}");
		    	}
		    	
		    	/*
		    	 * private Integer inhospitaltag;//住院标识
		    	 */	
		    	
		    	//出院诊断
		    	if(inhospital.getOuthospitaldiagnose()!=null){
		    		sql.SET("outhospitaldiagnose = #{outhospitaldiagnose}");
		    	}
		    	
		    	//出院诊断ICD码
		    	if(inhospital.getOuthospitaldiagnoseicd()!=null){
		    		sql.SET("outhospitaldiagnoseicd = #{outhospitaldiagnoseicd}");
		    	}
		    	
		    	/*private String outhospitinfo;//出院情况
		    	 */	 
		    	
		    	//出院记录编号
		    	if(inhospital.getOuthospitrecordid()!=null){
		    		sql.SET("outhospitrecordid = #{outhospitrecordid}");
		    	}
		    	
		    	/*private String drugallergy;//有无药物过敏
		    	 */	    
		    	
		    	//过敏药物
		    	if(inhospital.getAllergydrug()!=null){
		    		sql.SET("allergydrug = #{allergydrug}");
		    	}
		    	
		    	//RH
		    	if(inhospital.getRh()!=null){
		    		sql.SET("rh = #{rh}");
		    	}
		    	
		    	//离院方式
		    	if(inhospital.getOuthospitaltype()!=null){
		    		sql.SET("outhospitaltype = #{outhospitaltype}");
		    	}
		    	
		    	/*private String pathologydiagnosename;//病理诊断名称
		    	private String pathologydiagnosecode;//病理诊断码
		    	 */	 
		    	
		    	//入院途径
		    	if(inhospital.getInhospitalway()!=null){
		    		sql.SET("inhospitalway = #{inhospitalway}");
		    	}
		    	
		    	/*private String filenumber;//档案号
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
		    	 */	 
		    	
		    	//血型
		    	if(inhospital.getBloodtype()!=null){
		    		sql.SET("bloodtype = #{bloodtype}");
		    	}
		    	
		    	//自费金额
		    	if(inhospital.getOwncost()!=null){
		    		sql.SET("owncost = #{owncost}");
		    	}
		    	
		    	/*private Double healthinsurancecost;//医保金额
		    	 */	    
		    	
		    	//年龄单位
		    	if(inhospital.getAgeunit()!=null){
		    		sql.SET("ageunit = #{ageunit}");
		    	}
		    	
		    	//年龄
		    	if(inhospital.getAge()!=null){
		    		sql.SET("age = #{age}");
		    	}
		    	
		    	//民族
		    	if(inhospital.getNation()!=null){
		    		sql.SET("nation = #{nation}");
		    	}
		    	
		    	//姓名
		    	if(inhospital.getName()!=null){
		    		sql.SET("name = #{name}");
		    	}
		    	
		    	//病人电话
		    	if(inhospital.getPatientphone()!=null){
		    		sql.SET("patientphone = #{patientphone}");
		    	}
		    	
		    	//病人单位电话
		    	if(inhospital.getCompanyphone()!=null){
		    		sql.SET("companyphone = #{companyphone}");
		    	}
		    	
		    	//联系人电话
		    	if(inhospital.getRelationphone()!=null){
		    		sql.SET("relationphone = #{relationphone}");
		    	}
		    	
		    	//生日
		    	if(inhospital.getBirthday()!=null){
		    		sql.SET("birthday = #{birthday}");
		    	}
		    	
		    	//性别
		    	if(inhospital.getSex()!=null){
		    		sql.SET("sex = #{sex}");
		    	}
		    	
		    	//婚姻状况
		    	if(inhospital.getMarry()!=null){
		    		sql.SET("marry = #{marry}");
		    	}
		    	
		    	//职业
		    	if(inhospital.getProfession()!=null){
		    		sql.SET("profession = #{profession}");
		    	}
		    	
		    	//现住址
		    	if(inhospital.getCurrentaddress()!=null){
		    		sql.SET("currentaddress = #{currentaddress}");
		    	}
		    	
		    	//联系住址
		    	if(inhospital.getTeladdress()!=null){
		    		sql.SET("teladdress = #{teladdress}");
		    	}
		    	
		    	//工作单位
		    	if(inhospital.getCompany()!=null){
		    		sql.SET("company = #{company}");
		    	}
		    	
		    	//联系人名称
		    	if(inhospital.getTelname()!=null){
		    		sql.SET("telname = #{telname}");
		    	}
		    	
		    	//与联系人关系
		    	if(inhospital.getRelation()!=null){
		    		sql.SET("relation = #{relation}");
		    	}
		    	
		    	/*private String education;//教育程度
		    	 */	    	
		    	
		    	//身份证号
		    	if(inhospital.getCardnum()!=null){
		    		sql.SET("cardnum = #{cardnum}");
		    	}
		    	
		    	//主治医师工号
		    	if(inhospital.getJobnum()!=null){
		    		sql.SET("jobnum = #{jobnum}");
		    	}
		    	
		    	//医院HIS系统对应的患者ID
		    	if(inhospital.getPatientid_his()!=null){
		    		sql.SET("patientid_his = #{patientid_his}");
		    	}
		    	
		    	/*private String visitnum;//就诊号
		    	private String bednum;//床号
		    	 */	    	
		    	
		    	//用户信息更新时间
		    	if(inhospital.getUpdatetime()!=null){
		    		sql.SET("updatetime = #{updatetime}");
		    	}
		    	//新加字段
		    	//1院中 2院后
		    	if(inhospital.getIsStatus()!=null){
		    		sql.SET("isStatus = #{isStatus}");
		    	}
		    	
		    	////章丘新增随访类别：0科室随访，1疾病与健康管理中心随访 默认疾病与健康管理中心随访
		    	if(inhospital.getExclusiveInterview()!=null){
		    		sql.SET("exclusiveInterview = #{exclusiveInterview}");
		    	}
		    	
		    	//病种表ID
		    	if(inhospital.getChronicDiseaseId()!=null){
		    		sql.SET("chronicDiseaseId = #{chronicDiseaseId}");
		    	}
		    	sql.SET("ispigeonhole=#{ispigeonhole}");
		    	//广西二附院新增字段
		    	if(ValidateUtil.isNotNull(inhospital.getVisitnum())){
		    		sql.SET("visitnum=#{visitnum}");
		    	}
		    	if(ValidateUtil.isNotNull(inhospital.getPatNo())){
		    		sql.SET("PatNo=#{PatNo}");
		    	}
		    	if(ValidateUtil.isNotNull(inhospital.getCardNo())){
		    		sql.SET("CardNo=#{CardNo}");
		    	}
	    	sql.WHERE("id=#{id}");
		return sql.toString();
	
	}
}
