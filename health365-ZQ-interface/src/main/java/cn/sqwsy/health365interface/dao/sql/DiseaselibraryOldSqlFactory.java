package cn.sqwsy.health365interface.dao.sql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class DiseaselibraryOldSqlFactory {
	public String getDiseaseLibraryByEMR(Map<String, Object> para){
		SQL sql = new SQL();
        sql.SELECT("chronid");
        sql.FROM("diseaselibrary");
        if(para.get("diagnosenames")!=null){
        	@SuppressWarnings("unchecked")
			List<String> list = (List<String>) para.get("diagnosenames");
        	for(int i=0;i<list.size();i++){
        		sql.WHERE("diagnosename ='"+list.get(i)+"'");
        		if(i!=list.size()-1){
        			sql.OR();
        		}
        	}
        }
        return sql.toString();
	}
}
