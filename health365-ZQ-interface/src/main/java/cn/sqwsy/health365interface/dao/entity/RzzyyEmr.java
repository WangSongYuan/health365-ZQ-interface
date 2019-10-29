package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

public class RzzyyEmr {
	private Integer id;//主键
	
	private String leavehospitalid;//出院记录编号
	
	private String leavehospitalcontent;//出院记录内容
	
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//最后修改时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLeavehospitalid() {
		return leavehospitalid;
	}
	public void setLeavehospitalid(String leavehospitalid) {
		this.leavehospitalid = leavehospitalid;
	}
	public String getLeavehospitalcontent() {
		return leavehospitalcontent;
	}
	public void setLeavehospitalcontent(String leavehospitalcontent) {
		this.leavehospitalcontent = leavehospitalcontent;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
}