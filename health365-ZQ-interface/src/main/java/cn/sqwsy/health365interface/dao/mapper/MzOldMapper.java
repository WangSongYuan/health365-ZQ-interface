package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.OutpatientServiceInfo;
import cn.sqwsy.health365interface.dao.sql.MzOldSqlFactory;


public interface MzOldMapper {
	@SelectProvider(type=MzOldSqlFactory.class,method="getMz")
	OutpatientServiceInfo getMz(Map<String, Object> para);
	
	@InsertProvider(type = MzOldSqlFactory.class, method = "setMz")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setMz(OutpatientServiceInfo mz);
	
	@UpdateProvider(type=MzOldSqlFactory.class,method="updateMz")
	void updateMz(OutpatientServiceInfo mz);
}