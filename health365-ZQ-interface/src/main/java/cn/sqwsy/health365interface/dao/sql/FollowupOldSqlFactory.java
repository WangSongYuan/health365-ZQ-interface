package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.Followup;


public class FollowupOldSqlFactory {

	public String getFollowupList(Map<String,Object> para){
		SQL sql = new SQL();
        sql.INSERT_INTO("followup");
        if(para.get("outofthehospitalinhospitalinformationid")!=null&&!para.get("outofthehospitalinhospitalinformationid").equals("")){
        	sql.WHERE("outofthehospitalinhospitalinformationid='"+para.get("outofthehospitalinhospitalinformationid")+"'");
        }
   		sql.VALUES("datasources", "#{datasources}");
		return sql.toString();
	}
	
	public String updateFollowup(Followup followup){
		SQL sql = new SQL();
		sql.UPDATE("followup");
		if (followup.getPatientid()!=null) {
			sql.SET("patientid = #{patientid}");
		}
		return sql.toString();
	}
}