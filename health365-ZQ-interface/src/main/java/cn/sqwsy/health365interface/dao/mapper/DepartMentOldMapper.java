package cn.sqwsy.health365interface.dao.mapper;

import org.apache.ibatis.annotations.Select;

import cn.sqwsy.health365interface.dao.entity.Department;

public interface DepartMentOldMapper {

	@Select("SELECT * FROM department WHERE id = #{id}")
	Department getDepartmentById(int id);

	@Select("SELECT * FROM department WHERE thirdpartyhisid = #{thirdpartyhisid}")
	Department getDepartmentByHisId(String thirdpartyhisid);
}
