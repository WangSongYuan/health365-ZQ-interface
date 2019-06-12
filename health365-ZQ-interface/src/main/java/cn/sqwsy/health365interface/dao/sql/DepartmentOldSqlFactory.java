package cn.sqwsy.health365interface.dao.sql;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.Department;

public class DepartmentOldSqlFactory {
	public String setDepartment(Department department){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("department");
   		sql.VALUES("name", "#{name}");
   		sql.VALUES("needfollowup", "#{needfollowup}");
   		sql.VALUES("phone", "#{phone}");
   		sql.VALUES("address", "#{address}");
   		sql.VALUES("content", "#{content}");
   		sql.VALUES("thirdpartyhisid", "#{thirdpartyhisid}");
   		sql.VALUES("status", "#{status}");
   		sql.SET("exclusivestatus = #{exclusivestatus}");
   		sql.SET("createtime = #{createtime}");
			return sql.toString();
	}
	
	public String updateDepartment(Department department){
		SQL sql = new SQL();
		sql.UPDATE("department");
		sql.SET("name = #{name}");
		sql.SET("needfollowup = #{needfollowup}");
		sql.SET("phone = #{phone}");
		sql.SET("address = #{address}");
		sql.SET("content = #{content}");
		sql.SET("thirdpartyhisid = #{thirdpartyhisid}");
		sql.SET("status = #{status}");
		sql.SET("exclusivestatus = #{exclusivestatus}");
		sql.SET("updatetime = #{updatetime}");
		sql.WHERE("id=#{id}");
		return sql.toString();
	}
}
