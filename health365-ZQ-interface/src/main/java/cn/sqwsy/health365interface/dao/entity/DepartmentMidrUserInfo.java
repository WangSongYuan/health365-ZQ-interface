package cn.sqwsy.health365interface.dao.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sqwsy.health365interface.service.utils.DateUtil;

public class DepartmentMidrUserInfo {
	private Integer id;//用户ID
	private Integer departmentid;//科室表id
	private Integer userinfoid;//用户表id
	private Date createtime = DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"yyyy-MM-dd");//用户创建时间
	private Date updatetime = DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"yyyy-MM-dd");//用户信息更新时间
	private transient boolean selected = false;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Integer getUserinfoid() {
		return userinfoid;
	}
	public void setUserinfoid(Integer userinfoid) {
		this.userinfoid = userinfoid;
	}
}