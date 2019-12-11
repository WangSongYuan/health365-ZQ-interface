package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.HisError;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

public class HisErrorSqlFactory {
	public String getHisError(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("his_error");
	        if(para.get("patientid_his")!=null&&!para.get("patientid_his").equals("")){
	        	sql.WHERE("patientid_his='"+para.get("patientid_his")+"'");
	        }
	        if(para.get("inhospitalid")!=null&&!para.get("inhospitalid").equals("")){
	        	sql.WHERE("inhospitalid='"+para.get("inhospitalid")+"'");
	        }
	        if(para.get("inhospitalcount")!=null&&!para.get("inhospitalcount").equals("")){
	        	sql.WHERE("inhospitalcount='"+para.get("inhospitalcount")+"'");
	        }
	        if(para.get("thirdpartyhisid")!=null&&!para.get("thirdpartyhisid").equals("")){
	        	sql.WHERE("thirdpartyhisid='"+para.get("thirdpartyhisid")+"'");
	        }
	        if(para.get("zy_code")!=null&&!para.get("zy_code").equals("")){
	        	sql.WHERE("zy_code='"+para.get("zy_code")+"'");
	        }
	        return sql.toString();
	}
	
	public String setHisError(HisError hisError){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("his_error");
    		sql.VALUES("jobnum", "#{jobnum}");
    		sql.VALUES("maindoctorid", "#{maindoctorid}");
    		sql.VALUES("msg", "#{msg}");
    		sql.VALUES("thirdpartyhisid", "#{thirdpartyhisid}");
    		if(ValidateUtil.isNotNull(hisError.getPatientid_his())){
    			sql.VALUES("patientid_his", "#{patientid_his}");
    		}
    		if(hisError.getInhospitalid()!=null){
    			sql.VALUES("inhospitalid", "#{inhospitalid}");
    		}
    		if(hisError.getInhospitalcount()!=null){
    			sql.VALUES("inhospitalcount", "#{inhospitalcount}");
    		}
    		sql.VALUES("status", "#{status}");
    		sql.VALUES("ismakeup", "#{ismakeup}");
    		sql.VALUES("createtime", "#{createtime}");
    		sql.VALUES("updatetime", "#{updatetime}");
    		sql.VALUES("zy_code", "#{zy_code}");
	        sql.WHERE("id=#{id}");
			return sql.toString();
	}
	
	public String updateHisError(HisError hisError){
		SQL sql = new SQL();
		sql.UPDATE("his_error");
		sql.SET("jobnum = #{jobnum}");
		sql.SET("maindoctorid = #{maindoctorid}");
		sql.SET("msg = #{msg}");
		sql.SET("thirdpartyhisid = #{thirdpartyhisid}");
		if(ValidateUtil.isNotNull(hisError.getPatientid_his())){
			sql.SET("patientid_his = #{patientid_his}");
		}
		if(ValidateUtil.isNotNull(hisError.getInhospitalid())){
			sql.SET("inhospitalid = #{inhospitalid}");
		}
		if(hisError.getInhospitalcount()!=null){
			sql.SET("inhospitalcount = #{inhospitalcount}");
		}
		sql.SET("status = #{status}");
		sql.SET("ismakeup = #{ismakeup}");
		sql.SET("updatetime = #{updatetime}");
		sql.SET("zy_code = #{zy_code}");
		sql.WHERE("id=#{id}");
		return sql.toString();
	}
}
