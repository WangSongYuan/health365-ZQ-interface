package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.HisError;

public class HisErrorSqlFactory {
	public String getHisError(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("his_error");
	        if(para.get("patientid_his")!=null&&!para.get("patientid_his").equals("")){
	        	sql.WHERE("patientid_his='"+para.get("patientid_his")+"'");
	        }
	        if(para.get("inhospitalcount")!=null&&!para.get("inhospitalcount").equals("")){
	        	sql.WHERE("inhospitalcount='"+para.get("inhospitalcount")+"'");
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
    		sql.VALUES("patientid_his", "#{patientid_his}");
    		sql.VALUES("inhospitalcount", "#{inhospitalcount}");
    		sql.VALUES("status", "#{status}");
    		sql.VALUES("ismakeup", "#{ismakeup}");
    		sql.VALUES("createtime", "#{createtime}");
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
		sql.SET("patientid_his = #{patientid_his}");
		sql.SET("inhospitalcount = #{inhospitalcount}");
		sql.SET("status = #{status}");
		sql.SET("ismakeup = #{ismakeup}");
		sql.SET("updatetime = #{updatetime}");
		sql.WHERE("id=#{id}");
		return sql.toString();
	}
}
