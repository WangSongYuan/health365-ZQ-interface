package cn.sqwsy.health365interface.dao.entity;

public class DepartAndChron extends PO {
	private Integer id;//主键
	
	private Integer departid;//科室表主键唯一id
	private Integer chronid;//病种表主键唯一id

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDepartid() {
		return departid;
	}

	public void setDepartid(Integer departid) {
		this.departid = departid;
	}

	public Integer getChronid() {
		return chronid;
	}

	public void setChronid(Integer chronid) {
		this.chronid = chronid;
	}


}
