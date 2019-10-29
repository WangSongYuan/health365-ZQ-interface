package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.RzzyyEmr;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

public class RzzyyEmrOldSqlFactory {

	public String getEMR(Map<String, Object> para){
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("rzzyy_emr");
        if(para.get("leavehospitalid")!=null){
        	sql.WHERE("leavehospitalid='"+para.get("leavehospitalid")+"'");
        }
        return sql.toString();
	}
	
	public String setEMR(RzzyyEmr emr){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("rzzyy_emr");
	        if(ValidateUtil.isNotNull(emr.getLeavehospitalid())){
	        	sql.VALUES("leavehospitalid", "#{leavehospitalid}");
	        }
	        if(ValidateUtil.isNotNull(emr.getLeavehospitalcontent())){
	        	sql.VALUES("leavehospitalcontent", "#{leavehospitalcontent}");
	        }
	        sql.VALUES("updatetime", "now()");
	        return sql.toString();
	}
	
	public String updateEMR(RzzyyEmr emr){
        SQL sql = new SQL();
        sql.UPDATE("rzzyy_emr");
        if(ValidateUtil.isNotNull(emr.getLeavehospitalcontent())){
        	sql.SET("leavehospitalcontent=#{leavehospitalcontent}");
        }
        sql.SET("updatetime=now()");
        sql.WHERE("leavehospitalid=#{leavehospitalid}");
        return sql.toString();
	}
}