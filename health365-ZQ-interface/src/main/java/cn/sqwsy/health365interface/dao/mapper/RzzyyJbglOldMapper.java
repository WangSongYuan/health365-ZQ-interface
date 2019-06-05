package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.RzzyyJbgl;
import cn.sqwsy.health365interface.dao.sql.RzzyyJbglSqlFactory;

public interface RzzyyJbglOldMapper {
	@SelectProvider(type=RzzyyJbglSqlFactory.class,method="getRzzyyJbgl")
	RzzyyJbgl getRzzyyJbgl(Map<String, Object> para);

	@InsertProvider(type = RzzyyJbglSqlFactory.class, method = "setRzzyyJbgl")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setRzzyyJbgl(RzzyyJbgl rzzyyJbgl);
	
	
	@UpdateProvider(type=RzzyyJbglSqlFactory.class,method="updateRzzyyJbgl")
	void updateRzzyyJbgl(RzzyyJbgl rzzyyJbgl);
}