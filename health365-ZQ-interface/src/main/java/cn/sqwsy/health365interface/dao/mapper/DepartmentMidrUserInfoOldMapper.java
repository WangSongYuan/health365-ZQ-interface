package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.DepartmentMidrUserInfo;
import cn.sqwsy.health365interface.dao.sql.DepartmentMidrUserInfoOldSqlFactory;

public interface DepartmentMidrUserInfoOldMapper {

	@Select("SELECT * FROM departmentmidruserinfo WHERE userinfoid = #{userinfoid} and departmentid=#{departmentid}")
	DepartmentMidrUserInfo getDepartmentMidrUserInfoByUserId(Map<String,Object> parms);

	@InsertProvider(type = DepartmentMidrUserInfoOldSqlFactory.class, method = "setDepartmentMidrUserInfo")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setDepartmentMidrUserInfo(DepartmentMidrUserInfo departmentMidrUserInfo);
	
	@UpdateProvider(type=DepartmentMidrUserInfoOldSqlFactory.class,method="updateDepartmentMidrUserInfo")
	void updateDepartmentMidrUserInfo(DepartmentMidrUserInfo departmentMidrUserInfo);
}