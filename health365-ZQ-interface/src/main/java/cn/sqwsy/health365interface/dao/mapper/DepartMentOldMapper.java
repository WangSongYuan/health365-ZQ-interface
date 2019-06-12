package cn.sqwsy.health365interface.dao.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.Department;
import cn.sqwsy.health365interface.dao.sql.DepartmentOldSqlFactory;

public interface DepartMentOldMapper {
	@Select("SELECT * FROM department WHERE thirdpartyhisid = #{thirdpartyhisid}")
	Department getDepartmentByHisId(String thirdpartyhisid);
	
	@InsertProvider(type = DepartmentOldSqlFactory.class, method = "setDepartment")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setDepartment(Department department);
	
	@UpdateProvider(type=DepartmentOldSqlFactory.class,method="updateDepartment")
	void updateDepartment(Department department);
}
