package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.Outofthehospitalinhospitalinformation;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

public class OutOldSqlFactory {

	public String getOut(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("outofthehospitalinhospitalinformation");
	        if(para.get("patientid_his")!=null&&!para.get("patientid_his").equals("")){
	        	sql.WHERE("patientid_his='"+para.get("patientid_his")+"'");
	        }
	        if(para.get("inhospitalcount")!=null&&!para.get("inhospitalcount").equals("")){
	        	sql.WHERE("inhospitalcount="+para.get("inhospitalcount"));
	        }
	        return sql.toString();
	}
	
	public String getOutList(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("outofthehospitalinhospitalinformation");
	        if(para.get("cardnum")!=null&&!para.get("cardnum").equals("")){
	        	sql.WHERE("cardnum='"+para.get("cardnum")+"'");
	        }
	        return sql.toString();
	}
	
	public String setOut(Outofthehospitalinhospitalinformation out){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("outofthehospitalinhospitalinformation");
	        
	        //患者Id
	        sql.VALUES("patientId", "#{patient.id}");
	        
	        //身份证号
	    	if(out.getCardnum()!=null){
	    		sql.VALUES("cardnum", "#{cardnum}");
	    	}
	        
	        if(out.getDepartmentid()!=null){
	    		sql.VALUES("departmentid", "#{departmentid}");
	    	}
	    	
	    	//住院科室
	    	if(out.getInhospitaldepartment()!=null){
	    		sql.VALUES("inhospitaldepartment", "#{inhospitaldepartment}");
	    	}
	    	
	    	//住院科室ID
	    	if(out.getInhospitaldepartmentid()!=null){
	    		sql.VALUES("inhospitaldepartmentid", "#{inhospitaldepartmentid}");
	    	}

	    	//出院科室
	    	if(out.getOuthospitaldepartment()!=null){
	    		sql.VALUES("outhospitaldepartment", "#{outhospitaldepartment}");
	    	}
	    	
	    	//住院科室ID
	    	if(out.getOuthospitaldepartmentid()!=null){
	    		sql.VALUES("outhospitaldepartmentid", "#{outhospitaldepartmentid}");
	    	}
	    	
	    	//住院天数
	    	if(out.getInhospitaldays()!=null){
	    		sql.VALUES("inhospitaldays", "#{inhospitaldays}");
	    	}
	    	
	    	//入院日期
	    	if(out.getInhospitaldate()!=null){
	    		sql.VALUES("inhospitaldate", "#{inhospitaldate}");
	    	}
	    	
	    	//出院日期
	    	if(out.getOuthospitaldate()!=null){
	    		sql.VALUES("outhospitaldate", "#{outhospitaldate}");
	    	}
	    	
	    	//出院结算日期
	    	if(out.getOuthospitaldateclose()!=null){
	    		sql.VALUES("outhospitaldateclose", "#{outhospitaldateclose}");
	    	}
	    	
	    	//结算类型
	    	if(out.getClosetype()!=null){
	    		sql.VALUES("closetype", "#{closetype}");
	    	}
	    	
	    	//总花费
	    	if(out.getTotalcost()!=null){
	    		sql.VALUES("totalcost", "#{totalcost}");
	    	}
	    	
	    	//主管医师
	    	if(out.getMaindoctorname()!=null){
	    		sql.VALUES("maindoctorname", "#{maindoctorname}");
	    	}
	    	
	    	//主管医师ID
	    	if(out.getMaindoctorid()!=null){
	    		sql.VALUES("maindoctorid", "#{maindoctorid}");
	    	}
	    	
	    	//private String doordoctorname;//门诊医师
	    	//private String doordoctorid;//门诊医师ID
	    	
	    	//费用类型
	    	if(out.getCosttype()!=null){
	    		sql.VALUES("costtype", "#{costtype}");
	    	}
	    	
	    	//住院号
	    	if(out.getInhospitalid()!=null){
	    		sql.VALUES("inhospitalid", "#{inhospitalid}");
	    	}
	    	
	    	//住院次数
	    	if(out.getInhospitalcount()!=null){
	    		sql.VALUES("inhospitalcount", "#{inhospitalcount}");
	    	}
	    	
	    	//private Integer inhospitaltag;//住院标识
	    	//private String filenumber;//档案号
	    	
	    	//出院诊断
	    	if(out.getOuthospitaldiagnose()!=null){
	    		sql.VALUES("outhospitaldiagnose", "#{outhospitaldiagnose}");
	    	}
	    	
	    	//所有出院诊断
	    	if(out.getOuthospitaldiagnoseall()!=null){
	    		sql.VALUES("outhospitaldiagnoseall", "#{outhospitaldiagnoseall}");
	    	}
	    	
	    	//出院诊断ICD码
	    	if(out.getOuthospitaldiagnoseicd()!=null){
	    		sql.VALUES("outhospitaldiagnoseicd", "#{outhospitaldiagnoseicd}");
	    	}
	    	
	    	//出院情况
	    	if(ValidateUtil.isNotNull(out.getOuthospitinfo())){
	    		sql.VALUES("outhospitinfo", "#{outhospitinfo}");
	    	}
	    	
	    	//出院记录编号
	    	if(ValidateUtil.isNotNull(out.getOuthospitrecordid())){
	    		sql.VALUES("outhospitrecordid", "#{outhospitrecordid}");
	    	}
	    	
	    	//有无药物过敏
	    	if(ValidateUtil.isNotNull(out.getDrugallergy())){
	    		sql.VALUES("drugallergy", "#{drugallergy}");
	    	}	    
	    	
	    	//过敏药物
	    	if(ValidateUtil.isNotNull(out.getAllergydrug())){
	    		sql.VALUES("allergydrug", "#{allergydrug}");
	    	}
	    	
	    	//血型
	    	if(ValidateUtil.isNotNull(out.getBloodtype())){
	    		sql.VALUES("bloodtype", "#{bloodtype}");
	    	}
	    	
	    	//RH
	    	if(ValidateUtil.isNotNull(out.getRh())){
	    		sql.VALUES("rh", "#{rh}");
	    	}
	    	
	    	//离院方式
	    	if(out.getOuthospitaltype()!=null){
	    		sql.VALUES("outhospitaltype", "#{outhospitaltype}");
	    	}
	    	
	    	//排期状态
	    	if(out.getSchedulingstate()!=null){
	    		sql.VALUES("Schedulingstate", "#{Schedulingstate}");
	    	}
	    	
	    	//勿访原因
	    	if(ValidateUtil.isNotNull(out.getDonotvisitthecause())){
	    		sql.VALUES("donotvisitthecause", "#{donotvisitthecause}");
	    	}
	    	
	    	//病理诊断名称
	    	if(ValidateUtil.isNotNull(out.getPathologydiagnosename())){
	    		sql.VALUES("pathologydiagnosename", "#{pathologydiagnosename}");
	    	}
	    	
	    	//病理诊断码
	    	if(ValidateUtil.isNotNull(out.getPathologydiagnosecode())){
	    		sql.VALUES("pathologydiagnosecode", "#{pathologydiagnosecode}");
	    	}
	    	
	    	//入院途径
	    	if(ValidateUtil.isNotNull(out.getInhospitalway())){
	    		sql.VALUES("inhospitalway", "#{inhospitalway}");
	    	}
	    	
	    	//private String outhospitalchinadoctordiagnosediseasname;//出院中医诊断疾病名称
	    	//private String outhospitalchinadoctordiagnosediseascode;//出院中医诊断疾病名称编码
	    	//private String outhospitalchinadoctordiagnosecardname;//出院中医诊断证型
	    	//private String outhospitalchinadoctordiagnosecardcode;//出院中医诊断证型编码
//	    	private String mainoperationname;//主要手术名称
//	    	private String mainoperationcode;//主要手术编码
//	    	private String otheroperationnameone;//其他手术名称1
//	    	private String otheroperationcodeone;//其他手术编码1
//	    	
//	    	private String otheroperationnametwo;//其他手术名称2
//	    	private String otheroperationcodetwo;//其他手术编码2
//	    	
//	    	private String otheroperationnamethree;//其他手术名称3
//	    	private String otheroperationcodethree;//其他手术编码3
//	    	
//	    	private String otheroperationnamefour;//其他手术名称4
//	    	private String otheroperationcodefour;//其他手术编码4
//	    	
//	    	private String outhospitalotherdiagnosenameone;//出院其他诊断名1
//	    	private String outhospitalotherdiagnosecodeone;//出院其他诊断编码1
//	    	
//	    	private String outhospitalotherdiagnosenametwo;//出院其他诊断名2
//	    	private String outhospitalotherdiagnosecodetwo;//出院其他诊断编码2
//	    	
//	    	private String outhospitalotherdiagnosenamethree;//出院其他诊断名3
//	    	private String outhospitalotherdiagnosecodethree;//出院其他诊断编码3
//	    	
//	    	private String outhospitalotherdiagnosenamefour;//出院其他诊断名4
//	    	private String outhospitalotherdiagnosecodefour;//出院其他诊断	编码4
//	    	
//	    	private String outhospitalotherdiagnosenamefive;//出院其他诊断名5
//	    	private String outhospitalotherdiagnosecodefive;//出院其他诊断编码5
	    	
	    	//自费金额
	    	if(out.getOwncost()!=null){
	    		sql.VALUES("owncost", "#{owncost}");
	    	}
	    	
	    	//医保金额
	    	if(out.getHealthinsurancecost()!=null){
	    		sql.VALUES("healthinsurancecost", "#{healthinsurancecost}");
	    	}
	    	
	    	//1.院中2.出院(针对HIS)
	    	if(out.getHisrecordstate()!=null){
	    		sql.VALUES("hisrecordstate", "#{hisrecordstate}");
	    	}
	    	
	    	//1.院中2.出院(针对自己系统)
	    	if(out.getHealthrecordstate()!=null){
	    		sql.VALUES("healthrecordstate", "#{healthrecordstate}");
	    	}
	    	
	    	//医院HIS系统对应的患者ID
	    	if(out.getPatientid_his()!=null){
	    		sql.VALUES("patientid_his", "#{patientid_his}");
	    	}
	    	
	    	//private Integer recordoperator;//记录操作人(User表ID)
	    	/**********院中独特字段***********/
//	    	private Integer taskstatus=1;//1.未处理  2.已处理 3.终止处理 4.删除记录
//	    	private Integer recordcollector;//记录还原人 
//	    	private String taskreason;//任务原因(正常)[已处理、终止处理、删除记录的原因]
//	    	private String reasonsforreduction;//还原原因
//	    	private String inhospitaremarks;//住院信息备注
//	    	private String managementrecordstate="未记录";//管理记录状态  (未记录\管理中\归档)
	    	
	    	//创建时间
	    	if(out.getCreatetime()!=null){
	    		sql.VALUES("createtime", "#{createtime}");
	    	}
	    	
	    	////章丘新增随访类别：0科室随访，1疾病与健康管理中心随访 默认疾病与健康管理中心随访
	    	if(out.getExclusiveInterview()!=null){
	    		sql.VALUES("exclusiveInterview", "#{exclusiveInterview}");
	    	}
	    	
	    	//病种表ID
	    	if(out.getChronicDiseaseId()!=null){
	    		sql.VALUES("chronicDiseaseId", "#{chronicDiseaseId}");
	    	}
	    	
	    	//1：未下转2.已下转
	    	if(out.getZcxzstate()!=null){
	    		sql.VALUES("zcxzstate", "#{zcxzstate}");
	    	}
//	    	private Integer hospitaldictionaryid;//医院字典表ID 转出后才会设置
//	    	private String hospitalcoding;//转出的医院编码
//	    	private Timestamp referralTime;//wangsongyuan 新增下转时间 20190212
	        return sql.toString();
	}
	
	
	public String updateOut(Outofthehospitalinhospitalinformation out){
		SQL sql = new SQL();
		sql.UPDATE("outofthehospitalinhospitalinformation");
	        //患者Id
	        sql.SET("patientId=#{patient.id}");
	        
	        //身份证号
	    	if(out.getCardnum()!=null){
	    		 sql.SET("cardnum=#{cardnum}");
	    	}
	        
	        if(out.getDepartmentid()!=null){
	    		 sql.SET("departmentid=#{departmentid}");
	    	}
	    	
	    	//住院科室
	    	if(out.getInhospitaldepartment()!=null){
	    		 sql.SET("inhospitaldepartment=#{inhospitaldepartment}");
	    	}
	    	
	    	//住院科室ID
	    	if(out.getInhospitaldepartmentid()!=null){
	    		 sql.SET("inhospitaldepartmentid=#{inhospitaldepartmentid}");
	    	}

	    	//出院科室
	    	if(out.getOuthospitaldepartment()!=null){
	    		 sql.SET("outhospitaldepartment=#{outhospitaldepartment}");
	    	}
	    	
	    	//住院科室ID
	    	if(out.getOuthospitaldepartmentid()!=null){
	    		 sql.SET("outhospitaldepartmentid=#{outhospitaldepartmentid}");
	    	}
	    	
	    	//住院天数
	    	if(out.getInhospitaldays()!=null){
	    		 sql.SET("inhospitaldays=#{inhospitaldays}");
	    	}
	    	
	    	//入院日期
	    	if(out.getInhospitaldate()!=null){
	    		 sql.SET("inhospitaldate=#{inhospitaldate}");
	    	}
	    	
	    	//出院日期
	    	if(out.getOuthospitaldate()!=null){
	    		 sql.SET("outhospitaldate=#{outhospitaldate}");
	    	}
	    	
	    	//出院结算日期
	    	if(out.getOuthospitaldateclose()!=null){
	    		 sql.SET("outhospitaldateclose=#{outhospitaldateclose}");
	    	}
	    	
	    	//结算类型
	    	if(out.getClosetype()!=null){
	    		 sql.SET("closetype=#{closetype}");
	    	}
	    	
	    	//总花费
	    	if(out.getTotalcost()!=null){
	    		 sql.SET("totalcost=#{totalcost}");
	    	}
	    	
	    	//主管医师
	    	if(out.getMaindoctorname()!=null){
	    		 sql.SET("maindoctorname=#{maindoctorname}");
	    	}
	    	
	    	//主管医师ID
	    	if(out.getMaindoctorid()!=null){
	    		 sql.SET("maindoctorid=#{maindoctorid}");
	    	}
	    	
	    	//private String doordoctorname;//门诊医师
	    	//private String doordoctorid;//门诊医师ID
	    	
	    	//费用类型
	    	if(out.getCosttype()!=null){
	    		 sql.SET("costtype=#{costtype}");
	    	}
	    	
	    	//住院号
	    	if(out.getInhospitalid()!=null){
	    		 sql.SET("inhospitalid=#{inhospitalid}");
	    	}
	    	
	    	//住院次数
	    	if(out.getInhospitalcount()!=null){
	    		 sql.SET("inhospitalcount=#{inhospitalcount}");
	    	}
	    	
	    	//private Integer inhospitaltag;//住院标识
	    	//private String filenumber;//档案号
	    	
	    	//出院诊断
	    	if(out.getOuthospitaldiagnose()!=null){
	    		 sql.SET("outhospitaldiagnose=#{outhospitaldiagnose}");
	    	}
	    	
	    	//所有出院诊断
	    	if(out.getOuthospitaldiagnoseall()!=null){
	    		 sql.SET("outhospitaldiagnoseall=#{outhospitaldiagnoseall}");
	    	}
	    	
	    	//出院诊断ICD码
	    	if(out.getOuthospitaldiagnoseicd()!=null){
	    		 sql.SET("outhospitaldiagnoseicd=#{outhospitaldiagnoseicd}");
	    	}
	    	
	    	//出院情况
	    	if(ValidateUtil.isNotNull(out.getOuthospitinfo())){
	    		 sql.SET("outhospitinfo=#{outhospitinfo}");
	    	}
	    	
	    	//出院记录编号
	    	if(ValidateUtil.isNotNull(out.getOuthospitrecordid())){
	    		 sql.SET("outhospitrecordid=#{outhospitrecordid}");
	    	}
	    	
	    	//有无药物过敏
	    	if(ValidateUtil.isNotNull(out.getDrugallergy())){
	    		 sql.SET("drugallergy=#{drugallergy}");
	    	}	    
	    	
	    	//过敏药物
	    	if(ValidateUtil.isNotNull(out.getAllergydrug())){
	    		 sql.SET("allergydrug=#{allergydrug}");
	    	}
	    	
	    	//血型
	    	if(ValidateUtil.isNotNull(out.getBloodtype())){
	    		 sql.SET("bloodtype=#{bloodtype}");
	    	}
	    	
	    	//RH
	    	if(ValidateUtil.isNotNull(out.getRh())){
	    		 sql.SET("rh=#{rh}");
	    	}
	    	
	    	//离院方式
	    	if(out.getOuthospitaltype()!=null){
	    		 sql.SET("outhospitaltype=#{outhospitaltype}");
	    	}
	    	
	    	//排期状态
	    	if(out.getSchedulingstate()!=null){
	    		 sql.SET("Schedulingstate=#{Schedulingstate}");
	    	}
	    	
	    	//勿访原因
	    	if(ValidateUtil.isNotNull(out.getDonotvisitthecause())){
	    		sql.SET("donotvisitthecause=#{donotvisitthecause}");
	    	}
	    	
	    	//病理诊断名称
	    	if(ValidateUtil.isNotNull(out.getPathologydiagnosename())){
	    		 sql.SET("pathologydiagnosename=#{pathologydiagnosename}");
	    	}
	    	
	    	//病理诊断码
	    	if(ValidateUtil.isNotNull(out.getPathologydiagnosecode())){
	    		 sql.SET("pathologydiagnosecode=#{pathologydiagnosecode}");
	    	}
	    	
	    	//入院途径
	    	if(ValidateUtil.isNotNull(out.getInhospitalway())){
	    		 sql.SET("inhospitalway=#{inhospitalway}");
	    	}
	    	
	    	//private String outhospitalchinadoctordiagnosediseasname;//出院中医诊断疾病名称
	    	//private String outhospitalchinadoctordiagnosediseascode;//出院中医诊断疾病名称编码
	    	//private String outhospitalchinadoctordiagnosecardname;//出院中医诊断证型
	    	//private String outhospitalchinadoctordiagnosecardcode;//出院中医诊断证型编码
//	    	private String mainoperationname;//主要手术名称
//	    	private String mainoperationcode;//主要手术编码
//	    	private String otheroperationnameone;//其他手术名称1
//	    	private String otheroperationcodeone;//其他手术编码1
//	    	
//	    	private String otheroperationnametwo;//其他手术名称2
//	    	private String otheroperationcodetwo;//其他手术编码2
//	    	
//	    	private String otheroperationnamethree;//其他手术名称3
//	    	private String otheroperationcodethree;//其他手术编码3
//	    	
//	    	private String otheroperationnamefour;//其他手术名称4
//	    	private String otheroperationcodefour;//其他手术编码4
//	    	
//	    	private String outhospitalotherdiagnosenameone;//出院其他诊断名1
//	    	private String outhospitalotherdiagnosecodeone;//出院其他诊断编码1
//	    	
//	    	private String outhospitalotherdiagnosenametwo;//出院其他诊断名2
//	    	private String outhospitalotherdiagnosecodetwo;//出院其他诊断编码2
//	    	
//	    	private String outhospitalotherdiagnosenamethree;//出院其他诊断名3
//	    	private String outhospitalotherdiagnosecodethree;//出院其他诊断编码3
//	    	
//	    	private String outhospitalotherdiagnosenamefour;//出院其他诊断名4
//	    	private String outhospitalotherdiagnosecodefour;//出院其他诊断	编码4
//	    	
//	    	private String outhospitalotherdiagnosenamefive;//出院其他诊断名5
//	    	private String outhospitalotherdiagnosecodefive;//出院其他诊断编码5
	    	
	    	//自费金额
	    	if(out.getOwncost()!=null){
	    		 sql.SET("owncost=#{owncost}");
	    	}
	    	
	    	//医保金额
	    	if(out.getHealthinsurancecost()!=null){
	    		 sql.SET("healthinsurancecost=#{healthinsurancecost}");
	    	}
	    	
	    	//1.院中2.出院(针对HIS)
	    	if(out.getHisrecordstate()!=null){
	    		 sql.SET("hisrecordstate=#{hisrecordstate}");
	    	}
	    	
	    	//1.院中2.出院(针对自己系统)
	    	if(out.getHealthrecordstate()!=null){
	    		 sql.SET("healthrecordstate=#{healthrecordstate}");
	    	}
	    	
	    	//医院HIS系统对应的患者ID
	    	if(out.getPatientid_his()!=null){
	    		 sql.SET("patientid_his=#{patientid_his}");
	    	}
	    	
	    	//private Integer recordoperator;//记录操作人(User表ID)
	    	/**********院中独特字段***********/
//	    	private Integer taskstatus=1;//1.未处理  2.已处理 3.终止处理 4.删除记录
//	    	private Integer recordcollector;//记录还原人 
//	    	private String taskreason;//任务原因(正常)[已处理、终止处理、删除记录的原因]
//	    	private String reasonsforreduction;//还原原因
//	    	private String inhospitaremarks;//住院信息备注
//	    	private String managementrecordstate="未记录";//管理记录状态  (未记录\管理中\归档)
	    	
	    	//创建时间
	    	if(out.getUpdatetime()!=null){
	    		 sql.SET("updatetime=#{updatetime}");
	    	}
	    	
	    	////章丘新增随访类别：0科室随访，1疾病与健康管理中心随访 默认疾病与健康管理中心随访
	    	if(out.getExclusiveInterview()!=null){
	    		 sql.SET("exclusiveInterview=#{exclusiveInterview}");
	    	}
	    	
	    	//病种表ID
	    	if(out.getChronicDiseaseId()!=null){
	    		 sql.SET("chronicDiseaseId=#{chronicDiseaseId}");
	    	}
	    	
//	    	private Integer zcxzstate=1;//1：未下转2.已下转
//	    	private Integer hospitaldictionaryid;//医院字典表ID 转出后才会设置
//	    	private String hospitalcoding;//转出的医院编码
//	    	private Timestamp referralTime;//wangsongyuan 新增下转时间 20190212
	
	    	sql.WHERE("id=#{id}");
		return sql.toString();
	}
}