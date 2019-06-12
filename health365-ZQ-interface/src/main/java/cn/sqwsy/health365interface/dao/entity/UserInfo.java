package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;

public class UserInfo extends PO {

	private Integer id;//用户ID
	private String name;//用户名
	private String password;//登录密码
	private String viewpassword;//查看病人隐私密码
	private String phone;//用户手机号
	private Integer roleid;//用户角色表id
	private String jobnum;//工号(登录号)
	private String linenum;//线路号
	private transient String departmentids;//科室id
	private String thirdpartyhisid;//第三方hisid
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	private transient String departmentnames;//科室名称
	private transient String rolename;//角色名称
	private transient boolean selected = false;
	private Integer wxState=0;//wangsongyuan 新增章丘微信小程序注册状态 20190531 0代表未注册 1代表已注册
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getViewpassword() {
		return viewpassword;
	}
	public void setViewpassword(String viewpassword) {
		this.viewpassword = viewpassword;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	public String getJobnum() {
		return jobnum;
	}
	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}
	public String getLinenum() {
		return linenum;
	}
	public void setLinenum(String linenum) {
		this.linenum = linenum;
	}
	public String getDepartmentids() {
		return departmentids;
	}
	public void setDepartmentids(String departmentids) {
		this.departmentids = departmentids;
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
	public String getDepartmentnames() {
		return departmentnames;
	}
	public void setDepartmentnames(String departmentnames) {
		this.departmentnames = departmentnames;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getThirdpartyhisid() {
		return thirdpartyhisid;
	}
	public void setThirdpartyhisid(String thirdpartyhisid) {
		this.thirdpartyhisid = thirdpartyhisid;
	}
	public Integer getWxState() {
		return wxState;
	}
	public void setWxState(Integer wxState) {
		this.wxState = wxState;
	}
}