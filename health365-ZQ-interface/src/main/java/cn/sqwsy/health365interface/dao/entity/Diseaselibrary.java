package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

public class Diseaselibrary extends PO{
	private Integer id;//用户ID
	private String diagnosename;//诊断名称
	private String diagnosecode;//诊断编码
	private Integer chronid;//病种类型 1,心脏病 对应病种类型表主键唯一id
	private String seriously;//是否重点病种
	private String explanation;//解释
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	private transient Integer yesornoselect=0;//用于展示慢病病种标签显示时的字段 1.勾选 0.不勾选
	
	public Integer getYesornoselect() {
		return yesornoselect;
	}
	public void setYesornoselect(Integer yesornoselect) {
		this.yesornoselect = yesornoselect;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDiagnosename() {
		return diagnosename;
	}
	public void setDiagnosename(String diagnosename) {
		this.diagnosename = diagnosename;
	}
	public String getDiagnosecode() {
		return diagnosecode;
	}
	public void setDiagnosecode(String diagnosecode) {
		this.diagnosecode = diagnosecode;
	}
	public String getSeriously() {
		return seriously;
	}
	public void setSeriously(String seriously) {
		this.seriously = seriously;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}
	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public Integer getChronid() {
		return chronid;
	}
	public void setChronid(Integer chronid) {
		this.chronid = chronid;
	}
}
