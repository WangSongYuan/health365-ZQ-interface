package cn.sqwsy.health365interface.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import cn.sqwsy.health365interface.dao.entity.Diseaselibrary;
import cn.sqwsy.health365interface.dao.sql.DiseaselibraryOldSqlFactory;

public interface DiseaselibraryOldMapper {
	@Select("SELECT * FROM diseaselibrary WHERE diagnosecode = #{icd}")
	Diseaselibrary getDiseaselibraryByIcd(String icd);
	
	@SelectProvider(type=DiseaselibraryOldSqlFactory.class,method="getDiseaseLibraryByEMR")
	List<String> getDiseaseLibraryByEMR(Map<String,Object> para);
}
