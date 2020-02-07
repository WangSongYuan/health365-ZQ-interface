package cn.sqwsy.health365interface.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.sqwsy.health365interface.dao.entity.Department;
import cn.sqwsy.health365interface.dao.entity.DepartmentMidrUserInfo;
import cn.sqwsy.health365interface.dao.entity.Followup;
import cn.sqwsy.health365interface.dao.entity.HisError;
import cn.sqwsy.health365interface.dao.entity.Outofthehospitalinhospitalinformation;
import cn.sqwsy.health365interface.dao.entity.Patient;
import cn.sqwsy.health365interface.dao.entity.RzzyyEmr;
import cn.sqwsy.health365interface.dao.entity.RzzyyJbgl;
import cn.sqwsy.health365interface.dao.entity.UserInfo;
import cn.sqwsy.health365interface.dao.mapper.DepartMentOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DepartmentMidrUserInfoOldMapper;
import cn.sqwsy.health365interface.dao.mapper.FollowupOldMapper;
import cn.sqwsy.health365interface.dao.mapper.HisErrorOldMapper;
import cn.sqwsy.health365interface.dao.mapper.OutOldMapper;
import cn.sqwsy.health365interface.dao.mapper.PatientOldMapper;
import cn.sqwsy.health365interface.dao.mapper.RzzyyEmrOldMapper;
import cn.sqwsy.health365interface.dao.mapper.RzzyyJbglOldMapper;
import cn.sqwsy.health365interface.dao.mapper.UserInfoOldMapper;
import cn.sqwsy.health365interface.service.utils.CardNumUtil;
import cn.sqwsy.health365interface.service.utils.DateUtil;
import cn.sqwsy.health365interface.service.utils.HashUtil;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

@Component
public class JiXianOldHisHandler {
	private static final int AGE = 9;
	
	@Autowired
	private RzzyyJbglOldMapper rzzyyJbglMapper;

	@Autowired
	private DepartMentOldMapper departMentOldMapper;
	
	@Autowired
	private DepartmentMidrUserInfoOldMapper departmentMidrUserInfoOldMapper;

	@Autowired
	private HisErrorOldMapper hisErrorOldMapper;
	
	@Autowired
	private UserInfoOldMapper userInfoOldMapper;
	
	@Autowired
	private PatientOldMapper patientOldMapper;
	
	@Autowired
	private OutOldMapper outofthehospitalinhospitalinformationOldMapper;
	
	@Autowired
	private FollowupOldMapper followupOldMapper;
	
	@Autowired
	private RzzyyEmrOldMapper rzzyyEmrOldMapper;

	@Scheduled(fixedDelay = 1800000)
	public void fixedRateJob() {
		Connection conn = getHisConn();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			
			//院中数据
			String sql ="SELECT * FROM getInhospitalList";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while(rst.next()){
				setRzzyyJbgl(rst,1);
			}
			
			//院后数据
			for (int i = 0; i <= 2; i++) {
				Calendar outStartCal = Calendar.getInstance(); 
				outStartCal.add(Calendar.DAY_OF_MONTH,-i);
				outStartCal.set(Calendar.HOUR_OF_DAY, 0); 
				outStartCal.set(Calendar.SECOND, 0); 
				outStartCal.set(Calendar.MINUTE, 0); 
				Date outSstartTime = outStartCal.getTime();
				String starttime = DateUtil.format(outSstartTime, "yyyy-MM-dd");

				Calendar endCal = Calendar.getInstance(); 
				endCal.add(Calendar.DAY_OF_MONTH,-i+1);
				endCal.set(Calendar.HOUR_OF_DAY, 23); 
				endCal.set(Calendar.SECOND, 59); 
				endCal.set(Calendar.MINUTE, 59); 
				Date endTime = endCal.getTime();
				String endtime = DateUtil.format(endTime, "yyyy-MM-dd");
				
				sql = "SELECT * FROM getOuthospitalList WHERE outhospitaldatehome BETWEEN '"+starttime+"' AND '"+endtime+"'";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				setRzzyyJbgl(rst,2);
			}
			
			
			/***
			 * 大表分小表start
			 */
			Map<String,Object> map = new HashMap<>();
			map.put("ispigeonhole", 1);
			List<RzzyyJbgl> rzList = rzzyyJbglMapper.getRzzyyJbglList(map);
			for(RzzyyJbgl rj:rzList){
				StringBuffer ap = new StringBuffer();
				if(getErrorMessage(rj,ap)){
					setErrorMessage(rj, ap);
					rj.setIspigeonhole(2);
					rzzyyJbglMapper.updateRzzyyJbgl(rj);
					continue;
				}
				setErrorMessageStatus(rj);
				//将数据插入科室表
				setDepartment(rj);
				//将数据插入用户表
				setUserInfo(rj);
				Patient p = setPatient(rj);
				Department department=null;
				if(rj.getIsStatus()==1){
					department = departMentOldMapper.getDepartmentByHisId(rj.getInhospitaldepartmentid());
				}else if(rj.getIsStatus()==2){
					department = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
				}
				if(!ValidateUtil.isEquals("需要", department.getNeedfollowup())){
					rj.setIspigeonhole(2);
					rzzyyJbglMapper.updateRzzyyJbgl(rj);
					continue;
				}
				setOutHospital(rj, department, p);
				rj.setIspigeonhole(2);
				rzzyyJbglMapper.updateRzzyyJbgl(rj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rst != null)
					rst.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setRzzyyJbgl(ResultSet rst,int status) throws SQLException {
		while (rst.next()) {
			try {
				// 住院号√
				String inhospitalid = rst.getString("inhospitalid");
				//解决集贤住院号xxxxxx的问题
				if(!isNum(inhospitalid)){
					continue;
				}
				// 住院次数√
				String inhospitalcount = rst.getString("inhospitalcount");

				if (ValidateUtil.isNull(inhospitalid) && ValidateUtil.isNull(inhospitalcount)) {
					continue;
				}
				RzzyyJbgl rj = new RzzyyJbgl();
				Map<String, Object> para = new HashMap<>();
				para.put("inhospitalid", inhospitalid);
				para.put("inhospitalcount", Integer.valueOf(inhospitalcount));

				RzzyyJbgl rzzyyJbgl = rzzyyJbglMapper.getRzzyyJbgl(para);
				if (rzzyyJbgl != null) {
					rj = rzzyyJbgl;
				}
				// 姓名√
				String name = rst.getString("name");

				// *住院科室ID√
				String inhospitaldepartmentid = rst.getString("inhospitaldepartmentid");
				rj.setInhospitaldepartmentid(inhospitaldepartmentid);

				// *住院科室
				String inhospitaldepartment = rst.getString("inhospitaldepartment");
				rj.setInhospitaldepartment(inhospitaldepartment);
				
				if(status==2){			
					// 院后
					// *出院科室ID√
					String outhospitaldepartmentid = rst.getString("outhospitaldepartmentid");
					rj.setOuthospitaldepartmentid(outhospitaldepartmentid);
					// *出院科室√
					rj.setOuthospitaldepartment(inhospitaldepartment);
					// *首页出院日期(患者离院日期)√
					String outhospitaldatehome = rst.getString("outhospitaldatehome");
					if (ValidateUtil.isNotNull(outhospitaldatehome)) {
						Date parse = DateUtil.parse(outhospitaldatehome, "yyyy-MM-dd hh:mm:ss");
						Timestamp timesTamp = DateUtil.getSqlTimestamp(parse);
						rj.setOuthospitaldatehome(timesTamp);
					}
					// *结算出院日期√
					String outhospitaldateclose = rst.getString("outhospitaldateclose");
					if (ValidateUtil.isNotNull(outhospitaldateclose)) {
						Date inhospitaldateDate = DateUtil.parse(outhospitaldateclose, "yyyy-MM-dd hh:mm:ss");
						Timestamp outhospitaldatecloseTimesTamp = DateUtil.getSqlTimestamp(inhospitaldateDate);
						rj.setOuthospitaldateclose(outhospitaldatecloseTimesTamp);
					}
	
					// 出院诊断ICD码
					String outhospitaldiagnoseicd = rst.getString("outhospitaldiagnoseicd");
					rj.setOuthospitaldiagnoseicd(outhospitaldiagnoseicd);
					
					// 出院情况
					String outhospitinfo = rst.getString("outhospitinfo");
					rj.setOuthospitinfo(outhospitinfo);
					
					// 离院方式
					//String outhospitaltype = rst.getString("outhospitaltype");
					//rj.setOuthospitaltype(outhospitaltype);
					
					
					// 自费金额√
					String owncost = rst.getString("owncost");
					if (ValidateUtil.isNotNull(owncost)) {
						rj.setOwncost(Double.valueOf(owncost));
					}
					
					// 电子病历或病案首页或出院记录或出院小结
					String leavehospitalcontent = rst.getString("leavehospitalcontent");
					
					// 出院记录编号
					//String leavehospitalid = inhospitalid + inhospitalcount;
					String outhospitrecordid = rst.getString("outhospitrecordid");
					rj.setOuthospitrecordid(outhospitrecordid);
					
					Map<String,Object> map = new HashMap<>();
					map.put("leavehospitalid", outhospitrecordid);
					RzzyyEmr rzzyyEmrOld = rzzyyEmrOldMapper.getEMR(map);
					if(rzzyyEmrOld!=null){
						rzzyyEmrOld.setUpdatetime(new Timestamp(System.currentTimeMillis()));
						rzzyyEmrOld.setLeavehospitalcontent(leavehospitalcontent);
					}else{
						RzzyyEmr rzzyyEmr = new RzzyyEmr();
						rzzyyEmr.setLeavehospitalid(outhospitrecordid);
						rzzyyEmr.setLeavehospitalcontent(leavehospitalcontent);
						rzzyyEmrOldMapper.setEMR(rzzyyEmr);
					}
					
					// 出院诊断
					String outhospitaldiagnose = rst.getString("outhospitaldiagnose");
					rj.setOuthospitaldiagnose(outhospitaldiagnose);
				}

				// *总花费√
				String totalcost = rst.getString("totalcost");
				if (ValidateUtil.isNotNull(totalcost)) {
					rj.setTotalcost(Double.valueOf(totalcost));
				}

				// *费用类型√
				String costtype = rst.getString("costtype");
				rj.setCosttype(costtype);// 新农村合作医疗

				// 住院标识
				/*
				 * int inhospitaltag = rst.getInt("zybs");
				 * rj.setInhospitaltag(inhospitaltag);
				 */

				// 过敏药物
				String allergydrug = rst.getString("allergydrug");
				if (ValidateUtil.isNotNull(allergydrug)) {
					rj.setAllergydrug(allergydrug);
				}

				// RH（阴阳性）
				String rh = rst.getString("rh");
				if (ValidateUtil.isNotNull(rh)) {
					rj.setRh(rh);
				}

				// 入院途径√
				String inhospitalway = rst.getString("inhospitalway");
				rj.setInhospitalway(inhospitalway);

				// 血型√
				String bloodtype = rst.getString("bloodtype");
				if (ValidateUtil.isNotNull(bloodtype)) {
					rj.setBloodtype(bloodtype);
				}

				// *住院天数					
				String inhospitaldays = rst.getString("inhospitaldays");
				if (ValidateUtil.isNotNull(inhospitaldays)) {
					rj.setInhospitaldays(Integer.parseInt(inhospitaldays)); 
				}
				
				// *入院日期
				String inhospitaldate = rst.getString("inhospitaldate");
				if (ValidateUtil.isNotNull(inhospitaldate)) {
					Date inhospitaldateDate = DateUtil.parse(inhospitaldate, "yyyy-mm-dd hh:mm:ss");
					Timestamp inhospitaldateTimesTamp = DateUtil.getSqlTimestamp(inhospitaldateDate);
					rj.setInhospitaldate(inhospitaldateTimesTamp);
				}

				// 结算类型
				// String closetype = rst.getString("closetype");
				// rj.setClosetype(closetype);//正常

				// *主管医生id√
				String maindoctorid = rst.getString("maindoctorid");
				rj.setMaindoctorid(maindoctorid);

				// *主管医师姓名√
				// if(ValidateUtil.isNotNull(maindoctorid)&&status==2){
				String maindoctorname = rst.getString("maindoctorname");
				rj.setMaindoctorname(maindoctorname);
				// }

				// *门诊医师ID√
				String doordoctorid = rst.getString("doordoctorid");
				rj.setDoordoctorid(doordoctorid);
				// *门诊医师
				String doordoctorname = rst.getString("doordoctorname");
				rj.setDoordoctorname(doordoctorname);

				// *住院号
				rj.setInhospitalid(inhospitalid);
				
				// *就诊号
				String visitnum = rst.getString("visitnum");
				rj.setVisitnum(visitnum);

				// *住院次数
				if (ValidateUtil.isNotNull(inhospitalcount)) {
					rj.setInhospitalcount(Integer.parseInt(inhospitalcount));
				}
				
				// 医保金额
				String healthinsurancecost = rst.getString("healthinsurancecost");
				if(ValidateUtil.isNotNull(healthinsurancecost)){
					 rj.setHealthinsurancecost(Double.valueOf(healthinsurancecost)); 
				}

				// *年龄单位
				String ageunit = rst.getString("ageunit");
				rj.setAgeunit(ageunit);

				// *生日√
				Timestamp newbirthday = null;
				String birthday = rst.getString("birthday");
				if (ValidateUtil.isNotNull(birthday)) {
					SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
					DateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
					birthday = sf2.format(sf1.parse(birthday));
					birthday = birthday + " 00:00:00";
					newbirthday = Timestamp.valueOf(birthday);
				}

				// *身份证号√
				String cardnum = rst.getString("cardnum");

				// 如没有身份证号 就生成一个临时身份证号
				/*
				 * if((ValidateUtil.isNull(cardnum)||
				 * (cardnum.length()!=15 && cardnum.length()!=18))){
				 * if(newbirthday==null){ cardnum =
				 * "temp"+HashUtil.MD5Hashing(name); }else{ cardnum =
				 * "temp"+HashUtil.MD5Hashing(name+DateUtil.format(
				 * newbirthday, "yyyyMMdd")); } }
				 */

				// *民族√
				String nation = rst.getString("nation");
				if (ValidateUtil.isNotNull(nation)) {
					rj.setNation(nation);
				}

				// *姓名√
				rj.setName(name);
				rj.setCardnum(cardnum);// 设置身份证号码
				rj.setBirthday(newbirthday);// 设置生日

				// *病人电话√
				String patientphone = rst.getString("patientphone");
				rj.setPatientphone(patientphone);
				// *病人单位电话√
				String companyphone = rst.getString("companyphone");
				rj.setCompanyphone(companyphone);
				// *联系人电话√
				String relationphone = rst.getString("relationphone");
				rj.setRelationphone(relationphone);

				// *年龄
				String age = rst.getString("age");
				rj.setAge(Integer.valueOf(age));

				// *性别
				String sex = rst.getString("sex");
				rj.setSex(sex);
				
				// 婚姻状况√
				String marry = rst.getString("marry");
				if (ValidateUtil.isNotNull(marry)) {
					rj.setMarry(marry);
				}

				// 职业√
				String profession = rst.getString("profession");
				rj.setProfession(profession);
				// 现住址√
				
				
				// 现住址
				String currentaddress = rst.getString("currentaddress");
				rj.setCurrentaddress(currentaddress);
				// 联系住址
				String teladdress = rst.getString("teladdress");
				rj.setTeladdress(teladdress);
				// 工作单位
				String company = rst.getString("company");
				if (ValidateUtil.isNotNull(company)) {
					rj.setCompany(company);
				}
				
				// 联系人名称
				String telname = rst.getString("telname");
				rj.setTelname(telname);
				// 与联系人关系
				String relation = rst.getString("relation");
				rj.setRelation(relation);
				
				// 教育程度					
				String education = rst.getString("education");
				rj.setEducation(education);

				// *主治医生的HIS唯一标识
				String jobnum = rst.getString("jobnum");
				if (ValidateUtil.isNotNull(jobnum)) {
					rj.setJobnum(jobnum);
				}
				
				// 床号
				String bednum = rst.getString("bednum");
				rj.setBedNum(bednum);

				rj.setIsStatus(status);
				if (rzzyyJbgl != null) {
					rj.setIspigeonhole(1);
					rj.setUpdatetime(new Timestamp(System.currentTimeMillis()));
					rzzyyJbglMapper.updateRzzyyJbgl(rj);
				} else {
					rzzyyJbglMapper.setRzzyyJbgl(rj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * 修改错误提示状态
	 * @author WangSongYuan
	 * @param rj
	 */
	private void setErrorMessageStatus(RzzyyJbgl rj){
		Map<String,Object> para= new HashMap<>();
		para.put("inhospitalid", rj.getInhospitalid());
		para.put("inhospitalcount",rj.getInhospitalcount());
		HisError hisError = hisErrorOldMapper.getHisError(para);
		if(hisError != null){
			hisError.setStatus(1);
			hisError.setIsmakeup(1);
			hisError.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			hisError.setPatientid_his(rj.getPatientid_his());
			hisError.setInhospitalcount(rj.getInhospitalcount());
			hisErrorOldMapper.updateHisError(hisError);
		}
	}
	
	/**
	 * 添加错误信息
	 * @author WangSongYuan
	 * @param rj
	 * @param ap
	 */
	private void setErrorMessage(RzzyyJbgl rj,StringBuffer ap){
		try {
			HisError hiserr = new HisError();
			Map<String,Object> para= new HashMap<>();
			para.put("inhospitalid", rj.getInhospitalid());
			para.put("inhospitalcount",rj.getInhospitalcount());
			HisError hisError = hisErrorOldMapper.getHisError(para);
			if(hisError != null){
				hiserr = hisError;
			}
			hiserr.setMsg(ap.toString());
			hiserr.setThirdpartyhisid(rj.getId());
			hiserr.setMaindoctorid(rj.getMaindoctorid());
			hiserr.setJobnum(rj.getJobnum());
			hiserr.setPatientid_his(rj.getPatientid_his());
			hiserr.setInhospitalcount(rj.getInhospitalcount());
			hiserr.setStatus(0);
			if(hisError != null){
				hiserr.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				hisErrorOldMapper.updateHisError(hiserr);
			}else{
				hisErrorOldMapper.setHisError(hiserr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取出错信息
	 * @author WangSongYuan
	 * @param rj
	 * @param ap
	 * @return
	 */
	private boolean getErrorMessage(RzzyyJbgl rj,StringBuffer ap){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式  注意身份证信息为空
		ap.append( "当前时间："+df.format(new Date())+"您有新的消息:</br>科室:"+rj.getInhospitaldepartment()+",主治医师:"+rj.getMaindoctorname()+",患者姓名:"+rj.getName()+"</br>住院号:"+rj.getInhospitalid()+",住院次数:"+rj.getInhospitalcount()+"</br>错误信息如下:</br>");
		if(ValidateUtil.isNull(rj.getJobnum())){
			ap.append("</br>工号为空</br>");
			ap.append("</br>请联系信息科核实！</br>");
			return true;
		}
		//新增未分配管床医生
		if(ValidateUtil.isNull(rj.getMaindoctorid())){
			ap.append("</br>未分配管床医生</br>");
			ap.append("</br>请联系信息科核实！</br>");
			return true;
		}
		//如果不是小孩，且身份证号为空
		if(ValidateUtil.isNull(rj.getCardnum()) && ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE){
			ap.append("</br>身份证号为空</br>");
			ap.append("</br>请联系住院处补充身份证号码，补充后等待数据同步。在病案首页补充无效！</br>");
			return true;
		}else{
			if(ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE && !CardNumUtil.isLength(rj.getCardnum())){
				ap.append("</br>身份证号长度不为18位</br>");
				ap.append("</br>请和患者确认正确的身份证号码，并联系住院处修改身份证号码，修改后等待数据同步。在病案首页修改无效！</br>");
				return true;
			}
			if(ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE && !CardNumUtil.isCard17(rj.getCardnum())){
				ap.append("</br>身份证号前17位不为纯数字</br>");
				ap.append("</br>请和患者确认正确的身份证号码，并联系住院处修改身份证号码，修改后等待数据同步。在病案首页修改无效！</br>");
				return true;
			}
			if(ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE && !CardNumUtil.isCheckCode(rj.getCardnum())){
				ap.append("</br>身份证号校验码错误</br>");
				ap.append("</br>请和患者确认正确的身份证号码，并联系住院处修改身份证号码，修改后等待数据同步。在病案首页修改无效！</br>");
				return true;
			}
		}
		//手机号空
		if(ValidateUtil.isNull(rj.getPatientphone()) && ValidateUtil.isNull(rj.getRelationphone())){
			ap.append("</br>手机号为空</br>");
			ap.append("</br>请联系住院处补充患者的手机号码，补充后等待数据同步。在病案首页补充无效！</br>");
			return true;
		}else{
			//手机号错误
			if(!isNum(rj.getPatientphone()) && !isNum(rj.getRelationphone())){
				ap.append("</br>手机号填写错误</br>");
				ap.append("</br>请和患者确认正确的手机号码，并联系住院处修改手机号码，修改后等待数据同步。在病案首页修改无效！</br>");
				return true;
			}
		}
		//传入的HIS科室ID为空
		if(ValidateUtil.isNull(rj.getInhospitaldepartmentid())){
			ap.append("</br>HIS传入的科室ID为空</br>");
			ap.append("</br>请联系信息科核实！</br>");
			return true;
		}	
		return false;
	}
	
	private static boolean isNum(String str) {
		if(str==null||str.equals("")){
			return false;
		}
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	    return pattern.matcher(str).matches();  
	}

	private static Connection getHisConn() {
		String dbUser = "jk_jjgl_user";
		String dbPassword = "123456";
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://192.168.10.240:1433;DatabaseName=Emr_Hospital;integratedSecurity=false";
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 将数据插入科室表
	 * @author WangSongYuan
	 * @param rj
	 */
	private void setDepartment(RzzyyJbgl rj){
		   String hisId=null;
			//院后
			if(rj.getIsStatus()==1){
				hisId = rj.getInhospitaldepartmentid();
			}else if(rj.getIsStatus()==2){
				hisId = rj.getOuthospitaldepartmentid();
			}
			Department dt = departMentOldMapper.getDepartmentByHisId(hisId);
			if(dt==null){
				Department d = new Department();//科室
				if(rj.getIsStatus()==1){
					if(ValidateUtil.isNotNull(rj.getInhospitaldepartmentid())){
						d.setThirdpartyhisid(rj.getInhospitaldepartmentid());
						d.setName(rj.getInhospitaldepartment());//科室名称
					}
				}else if(rj.getIsStatus()==2){
					if(ValidateUtil.isNotNull(rj.getOuthospitaldepartmentid())){
						d.setThirdpartyhisid(rj.getOuthospitaldepartmentid());
						d.setName(rj.getOuthospitaldepartment());//科室名称
					}
				}
				//wangsongyuan 新增科室排期随访 默认值 20170719
				d.setNeedfollowup("需要");
				//wangsongyuan 新增科室类别 20180608
				d.setStatus(1);
				departMentOldMapper.setDepartment(d);
				//往中间表里添加数据
			}else{
				if(rj.getIsStatus()==1){
					dt.setName(rj.getInhospitaldepartment());//科室名称
				}else if(rj.getIsStatus()==2){
					dt.setName(rj.getOuthospitaldepartment());//科室名称
				}
				dt.setStatus(1);//科室类别
				departMentOldMapper.updateDepartment(dt);
			}
	}
	
	/**
	 * 将数据插入用户表
	 * @author WangSongYuan
	 * @param rj
	 */
	public void setUserInfo(RzzyyJbgl rj){
		Map<String, Object> para = new HashMap<>();
		para.put("thirdpartyhisid", rj.getMaindoctorid());
		UserInfo ui = userInfoOldMapper.getUserInfo(para);
		if(ui==null){
			UserInfo uif = new UserInfo();
			if(ValidateUtil.isNotNull(rj.getMaindoctorid())){
				uif.setThirdpartyhisid(rj.getMaindoctorid());
			}
			uif.setName(rj.getMaindoctorname());
			if(ValidateUtil.isNotNull(rj.getMaindoctorid())){//当主管医师的id不为空的时候，讲roleid设置成3位专科医生
				uif.setRoleid(3);
				uif.setJobnum(rj.getJobnum());
				uif.setPassword("123456");
			}
			userInfoOldMapper.setUserInfo(uif);
			DepartmentMidrUserInfo dmrinfo = new DepartmentMidrUserInfo();
			Department department = null;
			if(rj.getIsStatus()==1){
				department = departMentOldMapper.getDepartmentByHisId(rj.getInhospitaldepartmentid());
			}else if(rj.getIsStatus()==2){
				department = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
			}
			dmrinfo.setUserinfoid(uif.getId());
			dmrinfo.setDepartmentid(department.getId());
			departmentMidrUserInfoOldMapper.setDepartmentMidrUserInfo(dmrinfo);
		}else{
			Department department = null;
			if(rj.getIsStatus()==1){
				department = departMentOldMapper.getDepartmentByHisId(rj.getInhospitaldepartmentid());
			}else if(rj.getIsStatus()==2){
				department = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
			}
			//新科室id
			Integer departmentId = department.getId();
			//根据用户id获取用户和科室的中间表
			Map<String,Object> parms = new HashMap<>(3);
			parms.put("userinfoid", ui.getId());
			parms.put("departmentid", departmentId);
			DepartmentMidrUserInfo dmrinfo = departmentMidrUserInfoOldMapper.getDepartmentMidrUserInfoByUserId(parms);
			if(dmrinfo==null){
				DepartmentMidrUserInfo newdmrinfo = new DepartmentMidrUserInfo();
				newdmrinfo.setUserinfoid(ui.getId());
				newdmrinfo.setDepartmentid(department.getId());
				departmentMidrUserInfoOldMapper.setDepartmentMidrUserInfo(newdmrinfo);
			}
		}
	}
	
	private Patient setPatient(RzzyyJbgl rj){
		//将数据插入患者表
		String cardnum = rj.getCardnum();
		//如果为儿童
		if(((ValidateUtil.isEquals("岁", rj.getAgeunit())) && rj.getAge()<=AGE)||(!ValidateUtil.isEquals("岁", rj.getAgeunit()))){
			String cardnumold = "";
			if(rj.getBirthday()==null){ 
				cardnumold = "temp"+HashUtil.MD5Hashing(rj.getName());
			}else{
				cardnumold = "temp"+HashUtil.MD5Hashing(rj.getName()+DateUtil.format(rj.getBirthday(), "yyyyMMdd"));
			}
			Map<String,Object> para = new HashMap<>();
			para.put("cardnum", cardnumold);
			Patient pt = patientOldMapper.getPatient(para);
			//儿童且身份证正确
			if(CardNumUtil.isValidate18Idcard(cardnum)){
				//如果是儿童且患者表存在(临时身份证)
				if(pt != null){
					para = new HashMap<>();
					para.put("cardnum", cardnum);
					Patient cp = patientOldMapper.getPatient(para);
					//老数据无匹配身份证儿童患者
					if(cp==null){
						setPatientData(rj, cardnum, pt);
						patientOldMapper.updatePatient(pt);
						//将所有以前关联MD5的出院表的身份证号替换成新的身份证号
						para = new HashMap<>();
						para.put("cardnum", cardnumold);
						List<Outofthehospitalinhospitalinformation> outDataList = outofthehospitalinhospitalinformationOldMapper.getOutList(para);
						for (Outofthehospitalinhospitalinformation out : outDataList) {
							out.setCardnum(cardnum);
							outofthehospitalinhospitalinformationOldMapper.updateOut(out);
							//查询对应出院的随访列表集合
							para = new HashMap<>();
							para.put("outofthehospitalinhospitalinformationid", out.getId());
							para.put("datasources", "医院患者");
							List<Followup> followupupdataList = followupOldMapper.getFollowupList(para);
							for (Followup followupupData : followupupdataList) {
								//替换成新的患者ID
								followupupData.setPatientid(pt.getId());
								followupOldMapper.updateFollowup(followupupData);
							}
						}
						return pt;
					}else{	
						//老数据有身份证儿童患者
						setPatientData(rj, cardnum, cp);
						patientOldMapper.updatePatient(cp);
						//将所有以前关联MD5的出院表的身份证号替换成新的身份证号
						para = new HashMap<>();
						para.put("cardnum", cardnumold);
						List<Outofthehospitalinhospitalinformation> outDataList = outofthehospitalinhospitalinformationOldMapper.getOutList(para);
						for (Outofthehospitalinhospitalinformation out : outDataList) {
							out.setCardnum(cardnum);
							outofthehospitalinhospitalinformationOldMapper.updateOut(out);
							//查询对应出院的随访列表集合
							para = new HashMap<>();
							para.put("outofthehospitalinhospitalinformationid", out.getId());
							para.put("datasources", "医院患者");
							List<Followup> followupupdataList = followupOldMapper.getFollowupList(para);
							for (Followup followupupData : followupupdataList) {
								//替换成新的患者ID
								followupupData.setPatientid(cp.getId());
								followupOldMapper.updateFollowup(followupupData);
							}
						}
						return cp;
					}
				}else{//如果儿童患者表不存在(临时身份证)患者
					para = new HashMap<>();
					para.put("cardnum", cardnum);
					Patient cp = patientOldMapper.getPatient(para);
					if(cp==null){
						cp = new Patient();
						setPatientData(rj, cardnum, cp);
						patientOldMapper.setPatient(cp);
					}else{
						//老数据存在(身份证)患者 直接返回
						setPatientData(rj, cardnum, cp);
						patientOldMapper.updatePatient(cp);
					}
					return cp;
				}
			}else{
				//如果是儿童身份证不正确
				if(pt !=null){
					//存在更新数据（身份证号不更新）
					setPatientData(rj, cardnumold, pt);
					patientOldMapper.updatePatient(pt);
					return pt;
				}else{
					//如果不存在则创建临时身份证患者
					Patient p = new Patient();
					setPatientData(rj, cardnumold, p);
					patientOldMapper.setPatient(p);
					return p;
				}
			}
		}else{//非儿童患者
			Patient p = new Patient();//患者实体
			Map <String,Object> para = new HashMap<>();
			para.put("cardnum", cardnum);
			Patient pt = patientOldMapper.getPatient(para);
			if(pt != null){
				p = pt;
			}
			setPatientData(rj, cardnum, p);
			if(pt == null){
				patientOldMapper.setPatient(p);
			}else{//存在就更新
				patientOldMapper.updatePatient(p);
			}
			return p;
		}
	}

	private void setPatientData(RzzyyJbgl rj, String cardnum, Patient p) {
		p.setCardnum(cardnum);
		p.setAge(rj.getAge());
		p.setNation(rj.getNation());
		p.setName(rj.getName());
		p.setCompanyphone(rj.getCompanyphone());
		p.setRelationphone(rj.getRelationphone());
		p.setBirthday(rj.getBirthday());
		p.setSex(rj.getSex());
		p.setMarry(rj.getMarry());
		p.setCurrentaddress(rj.getCurrentaddress());
		p.setCompany(rj.getCompany());
		p.setTelname(rj.getTelname());
		p.setRelation(rj.getRelation());
		p.setEducation(rj.getEducation());
		p.setAgeunit(rj.getAgeunit());
		p.setTeladdress(rj.getTeladdress());
		p.setProfession(rj.getProfession());
		p.setPhoneone(rj.getPatientphone());
		//wangsongyuan 新增章丘HIS患者ID 20180912
		p.setPatientHisId(rj.getPatientid_his());
	}
	
	private void setOutHospital(RzzyyJbgl rj,Department department,Patient p){
		Outofthehospitalinhospitalinformation outh = new Outofthehospitalinhospitalinformation();
		Map<String,Object> para= new HashMap<>();
		para.put("inhospitalid", rj.getInhospitalid());
		para.put("inhospitalcount", rj.getInhospitalcount());
		Outofthehospitalinhospitalinformation outofthehospitalinhospitalinformation = outofthehospitalinhospitalinformationOldMapper.getOut(para);
		if(outofthehospitalinhospitalinformation != null){
			outh = outofthehospitalinhospitalinformation;
		}
		outh.setPatient(p);
		outh.setCardnum(p.getCardnum());
		//添加出院表中科室的id
		outh.setDepartmentid(department.getId());
		outh.setInhospitaldepartment(rj.getInhospitaldepartment());
		outh.setInhospitaldepartmentid(rj.getInhospitaldepartmentid());
		outh.setInhospitaldays(rj.getInhospitaldays());
		outh.setInhospitaldate(rj.getInhospitaldate());
		outh.setOuthospitaldate(rj.getOuthospitaldatehome());
		outh.setOuthospitaldateclose(rj.getOuthospitaldateclose());
		outh.setClosetype(rj.getClosetype());
		outh.setTotalcost(rj.getTotalcost());
		outh.setMaindoctorname(rj.getMaindoctorname());
		//添加出院表中用户的id
		para= new HashMap<>();
		para.put("thirdpartyhisid", rj.getMaindoctorid());
		UserInfo userinfo = userInfoOldMapper.getUserInfo(para);
		outh.setMaindoctorid(userinfo.getId().toString());
		outh.setDoordoctorname(rj.getDoordoctorname());
		outh.setDoordoctorid(rj.getDoordoctorid());
		outh.setCosttype(rj.getCosttype());
		outh.setInhospitalid(rj.getInhospitalid());
		outh.setInhospitalcount(rj.getInhospitalcount());
		outh.setInhospitaltag(rj.getInhospitaltag());
		outh.setFilenumber(rj.getFilenumber());
		outh.setOuthospitaldiagnose(rj.getOuthospitaldiagnose());
		
		//拼接所有的诊断
		String outhospitaldiagnoseall = new String();
		if (ValidateUtil.isNotNull(rj.getOuthospitaldiagnose())) {
			outhospitaldiagnoseall="出院诊断:"+rj.getOuthospitaldiagnose()+"--";
		}
		if (ValidateUtil.isNotNull(rj.getOuthospitalotherdiagnosenameone())) {
			outhospitaldiagnoseall=outhospitaldiagnoseall+"其他诊断1:"+rj.getOuthospitalotherdiagnosenameone()+"--";
		}
		if (ValidateUtil.isNotNull(rj.getOuthospitalotherdiagnosenametwo())) {
			outhospitaldiagnoseall=outhospitaldiagnoseall+"其他诊断2:"+rj.getOuthospitalotherdiagnosenametwo()+"--";
		}
		if (ValidateUtil.isNotNull(rj.getOuthospitalotherdiagnosenamethree())) {
			outhospitaldiagnoseall=outhospitaldiagnoseall+"其他诊断3:"+rj.getOuthospitalotherdiagnosenamethree()+"--";
		}
		if (ValidateUtil.isNotNull(rj.getOuthospitalotherdiagnosenamefour())) {
			outhospitaldiagnoseall=outhospitaldiagnoseall+"其他诊断4:"+rj.getOuthospitalotherdiagnosenamefour()+"--";
		}
		if (ValidateUtil.isNotNull(rj.getOuthospitalotherdiagnosenamefive())) {
			outhospitaldiagnoseall=outhospitaldiagnoseall+"其他诊断5:"+rj.getOuthospitalotherdiagnosenamefive();
		}
		outh.setOuthospitaldiagnoseall(outhospitaldiagnoseall);
		outh.setOuthospitaldiagnoseicd(rj.getOuthospitaldiagnoseicd());
		outh.setOuthospitinfo(rj.getOuthospitinfo());
		outh.setOuthospitrecordid(rj.getOuthospitrecordid());
		outh.setDrugallergy(rj.getDrugallergy());
		outh.setAllergydrug(rj.getAllergydrug());
		outh.setBloodtype(rj.getBloodtype());
		outh.setRh(rj.getRh());
		outh.setOuthospitaltype(rj.getOuthospitaltype());
		outh.setPathologydiagnosename(rj.getPathologydiagnosename());
		outh.setPathologydiagnosecode(rj.getPathologydiagnosecode());
		outh.setInhospitalway(rj.getInhospitalway());
		outh.setOuthospitalchinadoctordiagnosediseasname(rj.getOuthospitalchinadoctordiagnosediseasname());
		outh.setOuthospitalchinadoctordiagnosediseascode(rj.getOuthospitalchinadoctordiagnosediseascode());
		outh.setOuthospitalchinadoctordiagnosecardname(rj.getOuthospitalchinadoctordiagnosecardname());
		outh.setOuthospitalchinadoctordiagnosecardcode(rj.getOuthospitalchinadoctordiagnosecardcode());
		outh.setMainoperationname(rj.getMainoperationname());
		outh.setMainoperationcode(rj.getMainoperationcode());
		outh.setOtheroperationcodeone(rj.getOtheroperationcodeone());
		outh.setOtheroperationnameone(rj.getOtheroperationnameone());
		outh.setOtheroperationcodetwo(rj.getOtheroperationcodetwo());
		outh.setOtheroperationnametwo(rj.getOtheroperationnametwo());
		outh.setOtheroperationcodethree(rj.getOtheroperationcodethree());
		outh.setOtheroperationnamethree(rj.getOtheroperationnamethree());
		outh.setOtheroperationcodefour(rj.getOtheroperationcodefour());
		outh.setOtheroperationnamefour(rj.getOtheroperationnamefour());
		outh.setOuthospitalotherdiagnosecodeone(rj.getOuthospitalotherdiagnosecodeone());
		outh.setOuthospitalotherdiagnosenameone(rj.getOuthospitalotherdiagnosenameone());
		outh.setOuthospitalotherdiagnosecodetwo(rj.getOuthospitalotherdiagnosecodetwo());
		outh.setOuthospitalotherdiagnosenametwo(rj.getOuthospitalotherdiagnosenametwo());
		outh.setOuthospitalotherdiagnosecodethree(rj.getOuthospitalotherdiagnosecodethree());
		outh.setOuthospitalotherdiagnosenamethree(rj.getOuthospitalotherdiagnosenamethree());
		outh.setOuthospitalotherdiagnosecodefour(rj.getOuthospitalotherdiagnosecodefour());
		outh.setOuthospitalotherdiagnosenamefour(rj.getOuthospitalotherdiagnosenamefour());
		outh.setOuthospitalotherdiagnosecodefive(rj.getOuthospitalotherdiagnosecodefive());
		outh.setOuthospitalotherdiagnosenamefive(rj.getOuthospitalotherdiagnosenamefive());
		outh.setOwncost(rj.getOwncost());
		outh.setHealthinsurancecost(rj.getHealthinsurancecost());
		//离院方式
		outh.setOuthospitaltype(rj.getOuthospitaltype());
		//wangsongyuan 新增状态值  2018-5-11
		outh.setHisrecordstate(rj.getIsStatus());
		//wangsongyuan HIS患者ID 2018-5-11
		outh.setPatientid_his(rj.getPatientid_his());
		
		//默认院中
		if(outh.getHealthrecordstate()==0){
			outh.setHealthrecordstate(1);
		}
		if(outofthehospitalinhospitalinformation != null){
			outh.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			outofthehospitalinhospitalinformationOldMapper.updateOut(outh);
			if(outh.getSchedulingstate() == 2){
				para = new HashMap<>();
				para.put("outofthehospitalinhospitalinformationid", outh.getId());
				para.put("datasources","医院患者");
				List<Followup> followupList =followupOldMapper.getFollowupList(para);
				for (Followup followup : followupList) {
					followup.setPatientid(p.getId());
					followupOldMapper.updateFollowup(followup);
				}
			}
		}else{
			outh.setTaskstatus(1);
			outofthehospitalinhospitalinformationOldMapper.setOut(outh);
		}
	}
}