package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.HisError;
import cn.sqwsy.health365interface.dao.sql.HisErrorSqlFactory;

public interface HisErrorOldMapper {
	@SelectProvider(type=HisErrorSqlFactory.class,method="getHisError")
	HisError getHisError(Map<String, Object> para);

	@InsertProvider(type = HisErrorSqlFactory.class, method = "setHisError")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setHisError(HisError hisError);
	
	@UpdateProvider(type=HisErrorSqlFactory.class,method="updateHisError")
	void updateHisError(HisError hisError);
}