package cn.sqwsy.health365interface.dao.sql;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.DepartmentMidrUserInfo;

public class DepartmentMidrUserInfoOldSqlFactory {

	public String setDepartmentMidrUserInfo(DepartmentMidrUserInfo departmentMidrUserInfo){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("departmentmidruserinfo");
   		sql.VALUES("departmentid", "#{departmentid}");
   		sql.VALUES("userinfoid", "#{userinfoid}");
   		sql.SET("createtime = #{createtime}");
			return sql.toString();
	}
	
	public String updateDepartmentMidrUserInfo(DepartmentMidrUserInfo departmentMidrUserInfo){
		SQL sql = new SQL();
		sql.UPDATE("departmentmidruserinfo");
		sql.SET("departmentid = #{departmentid}");
		sql.SET("userinfoid = #{userinfoid}");
		sql.SET("updatetime = #{updatetime}");
		sql.WHERE("id=#{id}");
		return sql.toString();
	}
}