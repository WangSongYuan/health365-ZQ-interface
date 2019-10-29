package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.RzzyyEmr;
import cn.sqwsy.health365interface.dao.sql.RzzyyEmrOldSqlFactory;

public interface RzzyyEmrOldMapper {

	@SelectProvider(type = RzzyyEmrOldSqlFactory.class, method = "getEMR")
	RzzyyEmr getEMR(Map<String, Object> para);

	@InsertProvider(type = RzzyyEmrOldSqlFactory.class, method = "setEMR")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void setEMR(RzzyyEmr emr);

	@UpdateProvider(type = RzzyyEmrOldSqlFactory.class, method = "updateEMR")
	void updateEMR(RzzyyEmr emr);

}