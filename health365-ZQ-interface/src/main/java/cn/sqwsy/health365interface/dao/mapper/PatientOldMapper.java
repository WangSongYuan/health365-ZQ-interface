package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.Patient;
import cn.sqwsy.health365interface.dao.sql.PatientOldlSqlFactory;

public interface PatientOldMapper {
	@SelectProvider(type=PatientOldlSqlFactory.class,method="getPatient")
	Patient getPatient(Map<String, Object> para);

	@InsertProvider(type = PatientOldlSqlFactory.class, method = "setPatient")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setPatient(Patient patient);
	
	@UpdateProvider(type=PatientOldlSqlFactory.class,method="updatePatient")
	void updatePatient(Patient patient);
}