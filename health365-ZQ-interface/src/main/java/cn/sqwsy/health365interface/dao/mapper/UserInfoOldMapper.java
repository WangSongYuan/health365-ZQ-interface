package cn.sqwsy.health365interface.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import cn.sqwsy.health365interface.dao.entity.UserInfo;
import cn.sqwsy.health365interface.dao.sql.UserInfoOldlSqlFactory;


public interface UserInfoOldMapper {
	@SelectProvider(type=UserInfoOldlSqlFactory.class,method="getUserInfo")
	UserInfo getUserInfo(Map<String, Object> para);

	@InsertProvider(type = UserInfoOldlSqlFactory.class, method = "setUserInfo")
    @Options(useGeneratedKeys = true, keyProperty = "id")
	void setUserInfo(UserInfo userInfo);
}