package cn.sqwsy.health365interface.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import cn.sqwsy.health365interface.dao.entity.UserInfo;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

public class UserInfoOldlSqlFactory {
	public String getUserInfo(Map<String, Object> para){
		 SQL sql = new SQL();
	        sql.SELECT("*");
	        sql.FROM("user_info");
	        if(para.get("id")!=null&&!para.get("id").equals("")){
	        	sql.WHERE("id='"+para.get("id")+"'");
	        }
	        if(para.get("thirdpartyhisid")!=null&&!para.get("thirdpartyhisid").equals("")){
	        	sql.WHERE("thirdpartyhisid='"+para.get("thirdpartyhisid")+"'");
	        }
	        return sql.toString();
	}
	
	public String setUserInfo(UserInfo userInfo){
		 SQL sql = new SQL();
	        sql.INSERT_INTO("user_info");
	        if(ValidateUtil.isNotNull(userInfo.getThirdpartyhisid())){
	    		sql.VALUES("thirdpartyhisid", "#{thirdpartyhisid}");
	    	}
	        if(userInfo.getRoleid()!=null){
	    		sql.VALUES("roleid", "#{roleid}");
	    	}
	        if(ValidateUtil.isNotNull(userInfo.getName())){
	    		sql.VALUES("name", "#{name}");
	    	}
	        if(ValidateUtil.isNotNull(userInfo.getJobnum())){
	    		sql.VALUES("jobnum", "#{jobnum}");
	    	}
	        if(ValidateUtil.isNotNull(userInfo.getPassword())){
	    		sql.VALUES("password", "#{password}");
	    	}
	    	sql.VALUES("createtime", "#{createtime}");
	        return sql.toString();
	}
}
