package cn.sqwsy.health365interface.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import cn.sqwsy.health365interface.dao.entity.Department;
import cn.sqwsy.health365interface.dao.entity.DepartmentMidrUserInfo;
import cn.sqwsy.health365interface.dao.entity.OutpatientServiceInfo;
import cn.sqwsy.health365interface.dao.entity.Patient;
import cn.sqwsy.health365interface.dao.entity.ReservationReminder;
import cn.sqwsy.health365interface.dao.entity.RzzyyJbgl;
import cn.sqwsy.health365interface.dao.entity.UserInfo;
import cn.sqwsy.health365interface.dao.mapper.DepartMentOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DepartmentMidrUserInfoOldMapper;
import cn.sqwsy.health365interface.dao.mapper.MzOldMapper;
import cn.sqwsy.health365interface.dao.mapper.PatientOldMapper;
import cn.sqwsy.health365interface.dao.mapper.ReservationReminderOldMapper;
import cn.sqwsy.health365interface.dao.mapper.UserInfoOldMapper;
import cn.sqwsy.health365interface.service.utils.CardNumUtil;
import cn.sqwsy.health365interface.service.utils.DateUtil;
import cn.sqwsy.health365interface.service.utils.HashUtil;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;
@Component
public class ZhangQiuOldMzHandler {
	
	@Bean
	public TaskScheduler taskScheduler() {
	    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	    taskScheduler.setPoolSize(50);
	    return taskScheduler;
	}
	private static final int AGE = 9;
	
	@Autowired
	private DepartMentOldMapper departMentOldMapper;
	
	@Autowired
	private DepartmentMidrUserInfoOldMapper departmentMidrUserInfoOldMapper;

	@Autowired
	private UserInfoOldMapper userInfoOldMapper;
	
	@Autowired
	private PatientOldMapper patientOldMapper;
	
	@Autowired
	private MzOldMapper mzOldMapper;
	
	@Autowired
    private ReservationReminderOldMapper reservationReminderOldMapper;
	
	@Scheduled(fixedDelay = 600000)
	public void fixedRateJob() {
		System.out.println("zhangqiu mz start");
		Connection conn = getHisConn();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			Calendar outStartCal = Calendar.getInstance(); 
			outStartCal.add(Calendar.DAY_OF_MONTH,0);
			outStartCal.set(Calendar.HOUR_OF_DAY, 0); 
			outStartCal.set(Calendar.SECOND, 0); 
			outStartCal.set(Calendar.MINUTE, 0); 
			Date outSstartTime = outStartCal.getTime();
			String starttime = DateUtil.format(outSstartTime, "yyyy-MM-dd");
			String sql = "SELECT * FROM rzzyy_MZInfo z WHERE TO_CHAR (z.VISIT_DATE, 'yyyy-mm-dd') = '"+starttime+"'";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {

				//是否有效
				boolean status = true;
				
				OutpatientServiceInfo mz = new OutpatientServiceInfo();
				//门诊日期√
				String visit_date = rst.getString("VISIT_DATE");
				
				//门诊就诊序号√
				String visit_no = rst.getString("VISIT_NO");
				
				//患者hisId√	
				String patient_HisId = rst.getString("PATIENT_ID");
				
				StringBuffer sb = new StringBuffer();
				sb.append("</br>错误信息如下:</br>");				
				if(ValidateUtil.isNull(patient_HisId)){
					sb.append("</br>HIS患者ID为空！</br>");
					status = false;
				}else if(ValidateUtil.isNull(visit_no)){
					sb.append("</br>门诊就诊序号为空！</br>");
					status = false;
				}
				
				Timestamp visit_dateTimesTamp = null;
				if (ValidateUtil.isNull(visit_date)) {
					sb.append("</br>门诊日期为空！</br>");
					status = false;
				}else{
					Date visit_dateDate = DateUtil.parse(visit_date, "yyyy-MM-dd");
					visit_dateTimesTamp = DateUtil.getSqlTimestamp(visit_dateDate);
					mz.setVisit_date(visit_dateTimesTamp);
				}
				
				Map<String, Object> para = new HashMap<>();
				para.put("visit_date", visit_dateTimesTamp);
				para.put("visit_no", visit_no);
				para.put("patient_HisId", patient_HisId);
				OutpatientServiceInfo mzOld = mzOldMapper.getMz(para);
				if (mzOld != null) {
					//系统已经存在这条数据
					//y++;
					//System.out.println("已有数据"+y+"	"+rst.getString("PATIENT_ID"));
					mz = mzOld;
				}else{
					//排期状态
					mz.setSchedulingState(1);
				}
				
				//门诊卡号√
				String card_no = rst.getString("CARD_NO");
				mz.setCard_no(card_no);
				
				/**
				 * 患者信息
				 */
				
				//身份证号√
				String cardnum = rst.getString("ID_NO");
				
				// 姓名√
				String name = rst.getString("NAME");
				
				//生日√
				Timestamp newbirthday =null;
				String birthday = rst.getString("DATE_OF_BIRTH");
				if (ValidateUtil.isNotNull(birthday)) {
					SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
			        DateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");   
			        birthday = sf2.format(sf1.parse(birthday));
			        birthday = birthday+ " 00:00:00";
			        newbirthday = Timestamp.valueOf(birthday);
				}	
				
				//年龄√
				int age  = getAgeByCardNum(cardnum,newbirthday);
				
				Patient patient = null;
				Patient patientOld =null;
				Patient patientNew =null;
				//如果是儿童
				if(age<=AGE){
					String oldCardNum="";
					if(newbirthday==null){
						oldCardNum = "temp"+HashUtil.MD5Hashing(name);
					}else{
						oldCardNum = "temp"+HashUtil.MD5Hashing(name+DateUtil.format(newbirthday, "yyyyMMdd"));
					}
					//身份证正确
					if(CardNumUtil.isValidate18Idcard(cardnum)){
						para = new HashMap<>();
						para.put("cardnum", cardnum);
						patientOld = patientOldMapper.getPatient(para);
						//存在
						if(patientOld!=null){
							para = new HashMap<>();
							para.put("cardnum", cardnum);
							patientNew = patientOldMapper.getPatient(para);
							if(patientNew==null){
								patient=patientOld;
								setPatientData(rst, cardnum, name, newbirthday, age, patient);
							}else{
								//身份证儿童匹配成功
								patient=patientNew;
								setPatientData(rst, cardnum, name, newbirthday, age, patient);
							}
						
						}else{//不存在
							para = new HashMap<>();
							para.put("cardnum", cardnum);
							patientNew = patientOldMapper.getPatient(para);
							if(patientNew==null){
								patient = new Patient();
								setPatientData(rst, cardnum, name, newbirthday, age, patient);
							}else{
								patient = patientNew;
								setPatientData(rst, cardnum, name, newbirthday, age, patient);
							}
						}
					}else{//身份证不正确
						para = new HashMap<>();
						para.put("cardnum", oldCardNum);
						patientOld = patientOldMapper.getPatient(para);
						if(patientOld!=null){
							patient = patientOld;
							setPatientData(rst, oldCardNum, name, newbirthday, age, patient);
						}else{
							patient = new Patient();
							setPatientData(rst, oldCardNum, name, newbirthday, age, patient);
						}
					}
				}else{
					//如果不是小孩且身份证号为空
					if(ValidateUtil.isNull(cardnum)){
						sb.append("</br>身份证号码为空！</br>");
						cardnum="";
						status = false;
					}else if(!CardNumUtil.isValidate18Idcard(cardnum)&&age>AGE ){
						sb.append("</br>门诊患者身份证校验失败！</br>");
						status = false;
					}else{
						para = new HashMap<>();
						para.put("cardnum", cardnum);
						patientOld = patientOldMapper.getPatient(para);
						if (patientOld != null) {
							patient = patientOld;
							setPatientData(rst, cardnum, name, newbirthday, age, patient);
						}else{
							patient = new Patient();
							setPatientData(rst, cardnum, name, newbirthday, age, patient);
						}	
					}
				}
				
				//手机号为空
				if(ValidateUtil.isNull(rst.getString("PHONE_NUMBER_HOME")) && ValidateUtil.isNull(rst.getString("NEXT_OF_KIN_PHONE"))){
					sb.append("</br>手机号为空</br>");
					status = false;
				}else{
					//手机号错误
					if(!isNum(rst.getString("PHONE_NUMBER_HOME")) && !isNum(rst.getString("NEXT_OF_KIN_PHONE"))){
						sb.append("</br>手机号填写错误</br>");
						status = false;
					}
				}
				
				//门诊医师ID√
				String doctor_no = rst.getString("DOCTOR_NO");
				
				//门诊医师姓名√
				String doctor_name =rst.getString("DOCTOR");
				
				//门诊科室Id(第三方)HISID √
				String visit_dept = rst.getString("VISIT_DEPT");
				mz.setVisit_dept(visit_dept);
				
				/*
				 * 门诊科室√
				 */
				Department department = departMentOldMapper.getDepartmentByHisId(visit_dept);
				Department d = new Department();
				
				if(department!=null){//如果科室存在
					d = department;
					d.setName(rst.getString("DEPT_NAME"));
					d.setStatus(2);
					departMentOldMapper.updateDepartment(d);
				}else{//如果科室不存在
					d.setThirdpartyhisid(visit_dept);
					d.setName(rst.getString("DEPT_NAME"));//科室名称
					d.setNeedfollowup("需要");
					d.setStatus(2);//wangsongyuan 新增科室类别 20180608
					departMentOldMapper.setDepartment(d);
				}
				
				/*
				 * 门诊医生√
				 */
				UserInfo doctor = new UserInfo();
				para = new HashMap<>();
				para.put("thirdpartyhisid", doctor_no);
				UserInfo oldDoctor = userInfoOldMapper.getUserInfo(para);
				if (oldDoctor != null) {//如果医生存在
					doctor = oldDoctor;
					//根据用户id获取用户和科室的中间表
					DepartmentMidrUserInfo dmrinfo = departmentMidrUserInfoOldMapper.getDepartmentMidrUserInfoByUserId(doctor.getId());
					if(dmrinfo==null){//科室权限不存在就创建
						DepartmentMidrUserInfo newdmrinfo = new DepartmentMidrUserInfo();
						newdmrinfo.setUserinfoid(doctor.getId());
						newdmrinfo.setDepartmentid(d.getId());
						departmentMidrUserInfoOldMapper.setDepartmentMidrUserInfo(newdmrinfo);
					}else{//科室权限存在就更新
						dmrinfo.setDepartmentid(d.getId());
						departmentMidrUserInfoOldMapper.updateDepartmentMidrUserInfo(dmrinfo);
					}
				}else if(oldDoctor == null){//如果医生不存在
					doctor.setThirdpartyhisid(doctor_no);
					doctor.setName(doctor_name);
					doctor.setRoleid(9);//门诊疾病管理师
					doctor.setJobnum(doctor_no);
					doctor.setPassword("123456");
					//doctor.setViewpassword("接口");
					userInfoOldMapper.setUserInfo(doctor);//创建新医生
					DepartmentMidrUserInfo dmrinfo = new DepartmentMidrUserInfo();
					dmrinfo.setUserinfoid(doctor.getId());
					dmrinfo.setDepartmentid(d.getId());
					departmentMidrUserInfoOldMapper.setDepartmentMidrUserInfo(dmrinfo);//创建新科室权限
				}
				mz.setDoctor_no(doctor_no);//门诊医师Id(第三方)HIS
				mz.setDoctorinfo(doctor);////用户ID(也是门诊医生信息数据)
				
				//创建时间√
				/*String createtime = rst.getString("createtime");
				if (ValidateUtil.isNotNull(createtime)) {
					Date createtimeDate = DateUtil.parse(createtime, "yyyy-MM-dd HH:mm:ss");
					Timestamp createtimeDateTimesTamp = DateUtil.getSqlTimestamp(createtimeDate);
					patient.setCreatetime(createtimeDateTimesTamp);
				}*/

				//更新时间√
				/*String updatetime = rst.getString("updatetime");
				if (ValidateUtil.isNotNull(updatetime)) {
					Date updatetimeDate = DateUtil.parse(updatetime, "yyyy-MM-dd HH:mm:ss");
					Timestamp updatetimeDateTimesTamp = DateUtil.getSqlTimestamp(updatetimeDate);
					patient.setUpdatetime(updatetimeDateTimesTamp);
				}*/
				
				//总花费√
				String costs = rst.getString("COSTS");
				mz.setCosts(costs);
				
				//门诊诊断√
				String diag_desc = rst.getString("DIAG_DESC");
				mz.setDiag_desc(diag_desc);
				
				mz.setVisit_no(visit_no);//门诊就诊序号
				
				mz.setPatient_HisId(patient_HisId);//HIS患者ID
				
				mz.setClinic_label(rst.getString("CLINIC_LABEL"));//号别
				
				mz.setDepartment(d);
				
				Timestamp updateTime = DateUtil.getSqlTimestamp(new Date());//更新时间

				//数据有效时再更新信息
				if(status==false){//无效
					mz.setErrorMsg(sb.toString());
					mz.setIsValid(0);
				}else if(status==true){//有效
					mz.setIsValid(1);
					if (patientOld != null||patientNew!=null) {
						patient.setUpdatetime(updateTime);
						patientOldMapper.updatePatient(patient);
					}else {
						patientOldMapper.setPatient(patient);
					}
					mz.setPatient(patient);
				}

				mz.setUserinfo(doctor);
				
				if(mzOld!=null){
					mz.setUpdateTime(updateTime);
					mzOldMapper.updateMz(mz);
				}else{
					mzOldMapper.setMz(mz);
				}
				//wangsongyuan 数据有效时更新预约状态 20190524
				if(status==true){
					//wangsongyuan	预约管理更改预约状态 	20180731
					//List<ReservationReminder>  list = ManagerConstants.iCommonService.findList(ReservationReminder.class, 0, Integer.MAX_VALUE, "reservationTime:desc", Factor.create("patient.id", C.Eq, patient.getId()),Factor.create("fakeStatus", C.Eq, 1));
					para = new HashMap<>();
					para.put("patientId", patient.getId());
					para.put("fakeStatus", 1);
					List<ReservationReminder> list = reservationReminderOldMapper.getReservationReminderList(para);
					for(ReservationReminder reservationReminder:list){
							if(visit_dateTimesTamp.getTime()==reservationReminder.getReservationTime().getTime()){
								reservationReminder.setStatus(2);//已复诊
								reservationReminder.setFakeStatus(2);
							}else if(visit_dateTimesTamp.getTime()>reservationReminder.getReservationTime().getTime()){
								reservationReminder.setStatus(3);//未复诊
								reservationReminder.setFakeStatus(2);//已复诊
							}else if(visit_dateTimesTamp.getTime()<reservationReminder.getReservationTime().getTime()){
								reservationReminder.setStatus(3);//未复诊
							}
							reservationReminderOldMapper.updateReservationReminder(reservationReminder);
					}
				}
				
				/*//数据有效时更新预约状态
				if(status==true){
					//wangsongyuan	预约管理更改预约状态 	20180731
					List<ReservationReminder>  list = ManagerConstants.iCommonService.findList(ReservationReminder.class, 0, Integer.MAX_VALUE, "reservationTime:desc", Factor.create("patient.id", C.Eq, patient.getId()));
					if(list != null && !list.isEmpty()){
						ReservationReminder reservationReminder = list.get(0);
						if(reservationReminder!=null){
							Date reservaationTime = reservationReminder.getReservationTime();
							Calendar startCal = Calendar.getInstance(); 
							startCal.setTime(reservaationTime);
							startCal.add(Calendar.DAY_OF_MONTH,-2);
							startCal.set(Calendar.HOUR_OF_DAY, 0); 
							startCal.set(Calendar.SECOND, 0); 
							startCal.set(Calendar.MINUTE, 0); 
							Date startTime = startCal.getTime();
							Calendar endCal = Calendar.getInstance(); 
							endCal.setTime(reservaationTime);
							endCal.add(Calendar.DAY_OF_MONTH,+2);
							endCal.set(Calendar.HOUR_OF_DAY, 0); 
							endCal.set(Calendar.SECOND, 0); 
							endCal.set(Calendar.MINUTE, 0); 
							Date endTime = endCal.getTime();
							
							if(isEffectiveDate(visit_dateTimesTamp, startTime, endTime)){
								reservationReminder.setStatus(2);//已复诊
								ManagerConstants.iCommonService.update(reservationReminder);
							}
						}
					}
				}*/
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
	
	private static boolean isNum(String str) {
		if(str==null||str.equals("")){
			return false;
		}
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	    return pattern.matcher(str).matches();  
	}

	/**
	 * 根据身份证号码获取年龄
	 * 
	 * @param CardNum
	 * @param newbirthday
	 * @return
	 */
	public static int getAgeByCardNum(String CardNum, Date newbirthday) {
		int age = 0;
		try {
			if (CardNumUtil.isValidate18Idcard(CardNum) && !CardNum.substring(4).equals("temp")) {
				String year = CardNum.substring(6).substring(0, 4);// 得到年份
				String yue = CardNum.substring(10).substring(0, 2);// 得到月份
				Date date = new Date();// 得到当前的系统时间
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String fyear = format.format(date).substring(0, 4);// 当前年份
				String fyue = format.format(date).substring(5, 7);// 月份
				if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
					age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
				} else {// 当前用户还没过生
					age = Integer.parseInt(fyear) - Integer.parseInt(year);
				}
			} else {
				age = getAgeByBirth(newbirthday);
			}
			return age;
		} catch (Exception e) {
			// System.out.println("身份证号码不合法：【"+CardNum+"】");
			e.printStackTrace();
			age = getAgeByBirth(newbirthday);
			return age;
			// System.out.println("尝试使用生日获取年龄...【"+age+"】");
		}
	}

	/**
	 * 根据生日获取年龄
	 * 
	 * @param birthday
	 * @return
	 */
	public static int getAgeByBirth(Date birthday) {
		int age = 0;
		try {
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());// 当前时间

			Calendar birth = Calendar.getInstance();
			birth.setTime(birthday);

			if (birth.after(now)) {// 如果传入的时间，在当前时间的后面，返回0岁
				age = 0;
			} else {
				age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
				if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
					age += 1;
				}
			}
			return age;
		} catch (Exception e) {// 兼容性更强,异常后返回数据
			e.printStackTrace();
			return 0;
		}
	}

	private static Connection getHisConn() {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String ip = "192.168.1.3";
		String sid = "oracle";
		String port = "1521";
		String dbUser = "jkgl";
		String dbPassword = "jkgl";
		String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
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
			Department department = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
			dmrinfo.setUserinfoid(uif.getId());
			dmrinfo.setDepartmentid(department.getId());
			departmentMidrUserInfoOldMapper.setDepartmentMidrUserInfo(dmrinfo);
		}else{
			Department department = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
			//新科室id
			Integer departmentId = department.getId();
			//根据用户id获取用户和科室的中间表
			DepartmentMidrUserInfo dmrinfo = departmentMidrUserInfoOldMapper.getDepartmentMidrUserInfoByUserId(ui.getId());
			if(dmrinfo==null){
				DepartmentMidrUserInfo newdmrinfo = new DepartmentMidrUserInfo();
				newdmrinfo.setUserinfoid(ui.getId());
				newdmrinfo.setDepartmentid(department.getId());
				departmentMidrUserInfoOldMapper.setDepartmentMidrUserInfo(newdmrinfo);
			}else{
				dmrinfo.setDepartmentid(departmentId);
				departmentMidrUserInfoOldMapper.updateDepartmentMidrUserInfo(dmrinfo);
			}
		}
	}
	
	private static void setPatientData(ResultSet rst, String cardnum, String name, Timestamp newbirthday, int age,
			Patient patient) throws SQLException {
		//年龄√
		patient.setAge(age);
			
		patient.setName(name.trim());//设置患者姓名
		patient.setCardnum(cardnum);//设置患者身份证号码			
		patient.setBirthday(newbirthday);//生日
		
		// 民族√
		String nation = rst.getString("NATION");
		if(nation!=null&&nation.equals("01")){
			patient.setNation("汉族");
		}else if(nation!=null&&!nation.equals("")){
			patient.setNation(nation);
		}else{
			patient.setNation("不详");
		}		
		
		//性别√
		patient.setSex(rst.getString("SEX"));
		
		//婚姻状况X
		/*String marry = rst.getString("marry");
		if(marry!=null&&!marry.equals("")){
			patient.setMarry(marry);
		}else{
			patient.setMarry("不详");
		}*/
		
		//职业√
		String profession = rst.getString("IDENTITY");
		if(profession!=null&&!profession.equals("")){
			patient.setProfession(profession);
		}else{
			patient.setProfession("不详");
		}
		
		//现住址√
		String currentaddress = rst.getString("MAILING_ADDRESS");
		patient.setCurrentaddress(currentaddress);
		
		//联系住址X
		/*String teladdress = rst.getString("NEXT_OF_KIN_ADDR");
		if(teladdress!=null&&!teladdress.equals("")){
			patient.setTeladdress(teladdress);
		}else{
			patient.setTeladdress("不详");
		}*/
		
		//工作单位X
		/*String company = rst.getString("UNIT_IN_CONTRACT");
		if(company!=null&&!company.equals("")){
			patient.setCompany(company);
		}else{
			patient.setCompany("不详");
		}*/
		
		//教育程度×
		/*String education = rst.getString("education");
		if(education!=null&&!education.equals("")){
			patient.setEducation(education);
		}else{
			patient.setEducation("不详");
		}*/

		//年龄单位√
		patient.setAgeunit("岁");
		
		//病人电话√
		String phoneOne = rst.getString("PHONE_NUMBER_HOME");
		if(ValidateUtil.isNotNull(phoneOne)){
			patient.setPhoneone(phoneOne);
		}

		//联系人电话√
		String relationPhone = rst.getString("NEXT_OF_KIN_PHONE");
		if(ValidateUtil.isNotNull(relationPhone)){
			patient.setRelationphone(relationPhone);
		}
		
		//单位电话√
		String companyphone = rst.getString("PHONE_NUMBER_BUSINESS");
		if(companyphone!=null&&!companyphone.equals("")){
			patient.setCompanyphone(companyphone);
		}
	}
}
