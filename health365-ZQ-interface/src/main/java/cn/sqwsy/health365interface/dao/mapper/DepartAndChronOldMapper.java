package cn.sqwsy.health365interface.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import cn.sqwsy.health365interface.dao.entity.DepartAndChron;

public interface DepartAndChronOldMapper {
	
	/*StringBuffer sql = new StringBuffer("select new Map(d.id as id) from DepartAndChron d where departid=")
			.append(department.getId()).append(" and chronid=").append(chronid);*/
	@Select("SELECT id FROM DepartAndChron WHERE departid = #{departid} and chronid=#{chronid}")
	List<DepartAndChron> getDepartAndChronList(Map<String,Object> para);
}
