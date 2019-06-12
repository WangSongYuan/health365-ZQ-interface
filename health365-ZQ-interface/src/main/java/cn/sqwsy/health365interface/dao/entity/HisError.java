package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;


public class HisError extends PO{

	private Integer id;//用户ID
	private Integer thirdpartyhisid;//his表主键id
	private String msg;//错误信息
	private String maindoctorid;//His表主治医师id
	private Integer status = 0;//状态0代表没有被展示，默认值为0，1为展示过的。
	private Integer ismakeup = 0;//状态0代表没有补录，1代表已经补录
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	private String jobnum;//对应userinfo表jobnum字段
	private transient boolean selected = false;
	private String patientid_his;//HIS患者ID
	private Integer inhospitalcount;//住院次数
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the createtime
	 */
	public Timestamp getCreatetime() {
		return createtime;
	}
	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	/**
	 * @return the updatetime
	 */
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	/**
	 * @param updatetime the updatetime to set
	 */
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Integer getThirdpartyhisid() {
		return thirdpartyhisid;
	}
	public void setThirdpartyhisid(Integer thirdpartyhisid) {
		this.thirdpartyhisid = thirdpartyhisid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsmakeup() {
		return ismakeup;
	}
	public void setIsmakeup(Integer ismakeup) {
		this.ismakeup = ismakeup;
	}
	public String getJobnum() {
		return jobnum;
	}
	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}
	public String getMaindoctorid() {
		return maindoctorid;
	}
	public void setMaindoctorid(String maindoctorid) {
		this.maindoctorid = maindoctorid;
	}
	public String getPatientid_his() {
		return patientid_his;
	}
	public void setPatientid_his(String patientid_his) {
		this.patientid_his = patientid_his;
	}
	public Integer getInhospitalcount() {
		return inhospitalcount;
	}
	public void setInhospitalcount(Integer inhospitalcount) {
		this.inhospitalcount = inhospitalcount;
	}
}