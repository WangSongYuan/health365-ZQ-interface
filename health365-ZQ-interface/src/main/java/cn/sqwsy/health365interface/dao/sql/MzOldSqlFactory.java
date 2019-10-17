package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.OutpatientServiceInfo;

public class MzOldSqlFactory {

	public String getMz(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("mz_info");
	        if(para.get("patientid_his")!=null&&!para.get("patientid_his").equals("")){
	        	sql.WHERE("patientid_his='"+para.get("patientid_his")+"'");
	        }
	        if(para.get("visit_date")!=null&&!para.get("visit_date").equals("")){
	        	sql.WHERE("visit_date='"+para.get("visit_date")+"'");
	        }
	        if(para.get("visit_no")!=null&&!para.get("visit_no").equals("")){
	        	sql.WHERE("visit_no='"+para.get("visit_no")+"'");
	        }
	        return sql.toString();
	}
	
	public String setMz(OutpatientServiceInfo out){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("mz_info");
	        //患者ID
	        if(out.getPatient()!=null){
	    		sql.VALUES("patientId", "#{patient.id}");
	    	}
	        //科室ID
	        if(out.getDepartment()!=null){
	    		sql.VALUES("departmentId", "#{department.id}");
	    	}
	        //门诊医生
	        if(out.getDoctorinfo()!=null){
	    		sql.VALUES("doctorId", "#{doctorinfo.id}");
	    	}
	        //门诊日期
	        if(out.getVisit_date()!=null){
	    		sql.VALUES("visit_date", "#{visit_date}");
	    	}
	        //总花费
	        if(out.getCosts()!=null){
	    		sql.VALUES("costs", "#{costs}");
	    	}
	        //门诊诊断
	        if(out.getDiag_desc()!=null){
	    		sql.VALUES("diag_desc", "#{diag_desc}");
	    	}
	        //患者HisId
	        if(out.getPatient_HisId()!=null){
	    		sql.VALUES("patient_HisId", "#{patient_HisId}");
	    	}
	        //门诊卡号
	        if(out.getCard_no()!=null){
	    		sql.VALUES("card_no", "#{card_no}");
	    	}
	        //门诊科室ID
	        if(out.getVisit_dept()!=null){
	    		sql.VALUES("visit_dept", "#{visit_dept}");
	    	}
	        //门诊医师ID
	        if(out.getDoctor_no()!=null){
	    		sql.VALUES("doctor_no", "#{doctor_no}");
	    	}
	        //错误原因
	        if(out.getErrorMsg()!=null){
	    		sql.VALUES("errorMsg", "#{errorMsg}");
	    	}
	        //就诊序号
	        if(out.getVisit_no()!=null){
	    		sql.VALUES("visit_no", "#{visit_no}");
	    	}
	        //号别
	        if(out.getClinic_label()!=null){
	    		sql.VALUES("clinic_label", "#{clinic_label}");
	    	}
    		sql.VALUES("createTime", "#{createTime}");
    		sql.VALUES("isValid", "#{isValid}");
	        return sql.toString();
	}
	
	public String updateMz(OutpatientServiceInfo out){
		SQL sql = new SQL();
		sql.UPDATE("mz_info");
			 //患者ID
	        if(out.getPatient()!=null){
	    		sql.SET("patientId = #{patient.id}");
	    	}
	        //科室ID
	        if(out.getDepartment()!=null){
	    		sql.SET("departmentId = #{department.id}");
	    	}
	        //门诊医生
	        if(out.getDoctorinfo()!=null){
	    		sql.SET("doctorId = #{doctorinfo.id}");
	    	}
	        //门诊日期
	        if(out.getVisit_date()!=null){
	    		sql.SET("visit_date = #{visit_date}");
	    	}
	        //总花费
	        if(out.getCosts()!=null){
	    		sql.SET("costs = #{costs}");
	    	}
	        //门诊诊断
	        if(out.getDiag_desc()!=null){
	    		sql.SET("diag_desc = #{diag_desc}");
	    	}
	        //患者HisId
	        if(out.getPatient_HisId()!=null){
	    		sql.SET("patient_HisId = #{patient_HisId}");
	    	}
	        //门诊卡号
	        if(out.getCard_no()!=null){
	    		sql.SET("card_no = #{card_no}");
	    	}
	        //门诊科室ID
	        if(out.getVisit_dept()!=null){
	    		sql.SET("visit_dept = #{visit_dept}");
	    	}
	        //门诊医师ID
	        if(out.getDoctor_no()!=null){
	    		sql.SET("doctor_no = #{doctor_no}");
	    	}
	        //错误原因
	        if(out.getErrorMsg()!=null){
	    		sql.SET("errorMsg = #{errorMsg}");
	    	}
	        //就诊序号
	        if(out.getVisit_no()!=null){
	    		sql.SET("visit_no = #{visit_no}");
	    	}
	        //号别
	        if(out.getClinic_label()!=null){
	    		sql.SET("clinic_label = #{clinic_label}");
	    	}
			sql.SET("updateTime = #{updateTime}");
			sql.SET("isValid = #{isValid}");
	    	sql.WHERE("id=#{id}");
		return sql.toString();
	}
}