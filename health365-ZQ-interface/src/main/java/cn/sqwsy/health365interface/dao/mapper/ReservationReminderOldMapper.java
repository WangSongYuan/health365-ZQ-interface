package cn.sqwsy.health365interface.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.ReservationReminder;
import cn.sqwsy.health365interface.dao.sql.ReservationReminderOldSqlFactory;

public interface ReservationReminderOldMapper {
	@SelectProvider(type=ReservationReminderOldSqlFactory.class,method="getReservationReminderList")
	List<ReservationReminder> getReservationReminderList(Map<String,Object> para);
	
	@UpdateProvider(type=ReservationReminderOldSqlFactory.class,method="updateReservationReminder")
	void updateReservationReminder(ReservationReminder reservationReminder);
}
