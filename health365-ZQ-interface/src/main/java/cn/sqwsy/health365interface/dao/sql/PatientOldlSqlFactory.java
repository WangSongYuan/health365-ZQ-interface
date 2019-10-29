package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.Patient;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

public class PatientOldlSqlFactory {
	public String getPatient(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("patient_info");
	        if(para.get("cardnum")!=null&&!para.get("cardnum").equals("")){
	        	sql.WHERE("cardnum='"+para.get("cardnum")+"'");
	        }
	        return sql.toString();
	}
	
	public String setPatient(Patient patient){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("patient_info");
	        if(ValidateUtil.isNotNull(patient.getCardnum())){
	    		sql.VALUES("cardnum", "#{cardnum}");
	    	}
	        if(patient.getAge()!=null){
	    		sql.VALUES("age", "#{age}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getNation())){
	    		sql.VALUES("nation", "#{nation}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getName())){
	    		sql.VALUES("name", "#{name}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getPhoneone())){
	    		sql.VALUES("phoneone", "#{phoneone}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getPhonetwo())){
	    		sql.VALUES("phonetwo", "#{phonetwo}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getPhonethree())){
	    		sql.VALUES("phonethree", "#{phonethree}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getCompanyphone())){
	    		sql.VALUES("companyphone", "#{companyphone}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getRelationphone())){
	    		sql.VALUES("relationphone", "#{relationphone}");
	    	}
	        if(patient.getBirthday()!=null){
	    		sql.VALUES("birthday", "#{birthday}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getSex())){
	    		sql.VALUES("sex", "#{sex}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getMarry())){
	    		sql.VALUES("marry", "#{marry}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getProfession())){
	    		sql.VALUES("profession", "#{profession}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getCurrentaddress())){
	    		sql.VALUES("currentaddress", "#{currentaddress}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getTeladdress())){
	    		sql.VALUES("teladdress", "#{teladdress}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getCompany())){
	    		sql.VALUES("company", "#{company}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getTelname())){
	    		sql.VALUES("telname", "#{telname}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getRelation())){
	    		sql.VALUES("relation", "#{relation}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getTruerelation())){
	    		sql.VALUES("truerelation", "#{truerelation}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getEducation())){
	    		sql.VALUES("education", "#{education}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getAgeunit())){
	    		sql.VALUES("ageunit", "#{ageunit}");
	    	}
	        if(ValidateUtil.isNotNull(patient.getPatientHisId())){
	    		sql.VALUES("patientHisId", "#{patientHisId}");
	    	}
	        if(patient.getWxState()!=null){
	    		sql.VALUES("wxState", "#{wxState}");
	    	}
	    	sql.VALUES("createtime", "#{createtime}");
	        return sql.toString();
	}
	
	
	public String updatePatient(Patient patient){
		SQL sql = new SQL();
		sql.UPDATE("patient_info");
         	sql.SET("cardnum = #{cardnum}");
         	sql.SET("age = #{age}");
         	sql.SET("nation = #{nation}");
         	sql.SET("name = #{name}");
         	sql.SET("phoneone = #{phoneone}");
         	sql.SET("phonetwo = #{phonetwo}");
         	sql.SET("phonethree = #{phonethree}");
         	sql.SET("companyphone = #{companyphone}");
         	sql.SET("relationphone = #{relationphone}");
         	sql.SET("birthday = #{birthday}");
         	sql.SET("sex = #{sex}");
         	sql.SET("marry = #{marry}");
         	sql.SET("profession = #{profession}");
         	sql.SET("currentaddress = #{currentaddress}");
         	sql.SET("teladdress = #{teladdress}");
         	sql.SET("company = #{company}");
         	sql.SET("telname = #{telname}");
         	sql.SET("relation = #{relation}");
         	sql.SET("truerelation = #{truerelation}");
         	sql.SET("education = #{education}");
         	sql.SET("ageunit = #{ageunit}");
         	sql.SET("updatetime = #{updatetime}");
         	if(patient.getWxState()!=null){
         		sql.SET("wxState = #{wxState}");
         	}
	    	sql.WHERE("id=#{id}");
		return sql.toString();
	}
}
