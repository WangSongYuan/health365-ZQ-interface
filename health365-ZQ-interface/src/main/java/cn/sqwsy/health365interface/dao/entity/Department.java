/**
 * 
 */
package cn.sqwsy.health365interface.dao.entity;

import java.sql.Timestamp;
/**
 * @title : 科室实体类
 * @description : 
 * @version 1.0
 * @email : sunpeng917@163.com
 * @author : sunpeng
 * @createtime : 2015年8月2日 下午8:26:27
 */

public class Department extends PO {
	private Integer id;//用户ID
	private String name;//科室名称
	private String needfollowup;//排期随访 需要和不需要
	private String phone;//电话
	private String address;//门牌号(位置)
	private String content;//描述
	private String thirdpartyhisid;//第三方id
	private Integer status;//状态 1代表出院患者，2.门诊患者3.健康快车
	private Integer exclusivestatus;//专访状态：0无需专访 1部分专访 2全部专访
	private Timestamp createtime = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatetime = new Timestamp(System.currentTimeMillis());//用户信息更新时间

	private transient boolean selected = false;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the needfollowup
	 */

	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	public String getNeedfollowup() {
		return needfollowup;
	}
	public void setNeedfollowup(String needfollowup) {
		this.needfollowup = needfollowup;
	}
	public String getThirdpartyhisid() {
		return thirdpartyhisid;
	}
	public void setThirdpartyhisid(String thirdpartyhisid) {
		this.thirdpartyhisid = thirdpartyhisid;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getExclusivestatus() {
		return exclusivestatus;
	}
	public void setExclusivestatus(Integer exclusivestatus) {
		this.exclusivestatus = exclusivestatus;
	}
}
