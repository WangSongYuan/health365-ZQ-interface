package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.ReservationReminder;

public class ReservationReminderOldSqlFactory {
	public String getReservationReminderList(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("reservationreminder");
	        if(para.get("patientId")!=null&&!para.get("patientId").equals("")){
	        	sql.WHERE("patientId='"+para.get("patientId")+"'");
	        }
	        if(para.get("fakeStatus")!=null&&!para.get("fakeStatus").equals("")){
	        	sql.WHERE("fakeStatus='"+para.get("fakeStatus")+"'");
	        }
	        return sql.toString();
	}
	
	public String updateReservationReminder(ReservationReminder reservationReminder){
		SQL sql = new SQL();
		sql.UPDATE("reservationreminder");
		sql.SET("status = #{status}");
		sql.SET("fakeStatus = #{fakeStatus}");
		sql.WHERE("id=#{id}");
		return sql.toString();
	}
}
