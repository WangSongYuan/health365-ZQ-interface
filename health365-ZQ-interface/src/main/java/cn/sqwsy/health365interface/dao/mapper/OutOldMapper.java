package cn.sqwsy.health365interface.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.Outofthehospitalinhospitalinformation;
import cn.sqwsy.health365interface.dao.sql.OutOldSqlFactory;


public interface OutOldMapper {
	@SelectProvider(type=OutOldSqlFactory.class,method="getOut")
	Outofthehospitalinhospitalinformation getOut(Map<String, Object> para);
	
	@SelectProvider(type=OutOldSqlFactory.class,method="getOutList")
	List<Outofthehospitalinhospitalinformation> getOutList(Map<String,Object> para);

	@InsertProvider(type = OutOldSqlFactory.class, method = "setOut")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setOut(Outofthehospitalinhospitalinformation out);
	
	@UpdateProvider(type=OutOldSqlFactory.class,method="updateOut")
	void updateOut(Outofthehospitalinhospitalinformation out);
}