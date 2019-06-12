package cn.sqwsy.health365interface.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.sqwsy.health365interface.dao.entity.Followup;
import cn.sqwsy.health365interface.dao.sql.FollowupOldSqlFactory;

public interface FollowupOldMapper {
	@SelectProvider(type=FollowupOldSqlFactory.class,method="getFollowupList")
	List<Followup> getFollowupList(Map<String,Object> para);

	@UpdateProvider(type=FollowupOldSqlFactory.class,method="updateFollowup")
	void updateFollowup(Followup followup);
}