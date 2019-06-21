package cn.sqwsy.health365interface.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.sqwsy.health365interface.dao.entity.DepartAndChron;
import cn.sqwsy.health365interface.dao.entity.Department;
import cn.sqwsy.health365interface.dao.entity.DepartmentMidrUserInfo;
import cn.sqwsy.health365interface.dao.entity.Diseaselibrary;
import cn.sqwsy.health365interface.dao.entity.Followup;
import cn.sqwsy.health365interface.dao.entity.HisError;
import cn.sqwsy.health365interface.dao.entity.Outofthehospitalinhospitalinformation;
import cn.sqwsy.health365interface.dao.entity.Patient;
import cn.sqwsy.health365interface.dao.entity.RzzyyJbgl;
import cn.sqwsy.health365interface.dao.entity.UserInfo;
import cn.sqwsy.health365interface.dao.mapper.DepartAndChronOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DepartMentOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DepartmentMidrUserInfoOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DiseaselibraryOldMapper;
import cn.sqwsy.health365interface.dao.mapper.FollowupOldMapper;
import cn.sqwsy.health365interface.dao.mapper.HisErrorOldMapper;
import cn.sqwsy.health365interface.dao.mapper.OutOldMapper;
import cn.sqwsy.health365interface.dao.mapper.PatientOldMapper;
import cn.sqwsy.health365interface.dao.mapper.RzzyyJbglOldMapper;
import cn.sqwsy.health365interface.dao.mapper.UserInfoOldMapper;
import cn.sqwsy.health365interface.service.utils.CardNumUtil;
import cn.sqwsy.health365interface.service.utils.DateUtil;
import cn.sqwsy.health365interface.service.utils.HashUtil;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

@Component
public class ZhangQiuOldHisHandler {
	private static final int AGE = 9;
	
	@Autowired
	private RzzyyJbglOldMapper rzzyyJbglMapper;

	@Autowired
	private DiseaselibraryOldMapper diseaselibraryOldMapper;

	@Autowired
	private DepartMentOldMapper departMentOldMapper;
	
	@Autowired
	private DepartmentMidrUserInfoOldMapper departmentMidrUserInfoOldMapper;

	@Autowired
	private DepartAndChronOldMapper departAndChronOldMapper;
	
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

	@Scheduled(fixedDelay = 600000)
	public void fixedRateJob() {
		Connection conn = getHisConn();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			for (int i = 0; i <= 1; i++) {
				Calendar outStartCal = Calendar.getInstance();
				outStartCal.add(Calendar.DAY_OF_MONTH, -i);
				outStartCal.set(Calendar.HOUR_OF_DAY, 0);
				outStartCal.set(Calendar.SECOND, 0);
				outStartCal.set(Calendar.MINUTE, 0);
				Date outSstartTime = outStartCal.getTime();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(outSstartTime);
				String sql = "SELECT * FROM jkgl.rzzyy_zyxx t, jkgl.rzzyy_jbgl b WHERE to_char ( t.discharge_date_time, 'yyyy-mm-dd' ) = '"
						+ date + "' AND t.patient_id = b.patient_id AND t.visit_id = b.visit_id";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				while (rst.next()) {
					try {
						// 住院号√
						String inhospitalid = rst.getString("inp_no");
						// 住院次数√
						String inhospitalcount = rst.getString("zycs");
						String patientid_his = rst.getString("patient_id");

						if (ValidateUtil.isNull(inhospitalid) && ValidateUtil.isNull(inhospitalcount)) {
							continue;
						}
						RzzyyJbgl rj = new RzzyyJbgl();
						Map<String, Object> para = new HashMap<>();
						para.put("patientid_his", patientid_his);
						para.put("inhospitalcount", Integer.valueOf(inhospitalcount));
						//System.out.println(patientid_his+"	"+inhospitalcount);
						RzzyyJbgl rzzyyJbgl = rzzyyJbglMapper.getRzzyyJbgl(para);
						if (rzzyyJbgl != null) {
							rj = rzzyyJbgl;
						}
						// 姓名√
						String name = rst.getString("name");
						// 性别√
						String sex = rst.getString("sex");

						// *住院科室ID√
						String inhospitaldepartmentid = rst.getString("dept_discharge_from");
						rj.setInhospitaldepartmentid(inhospitaldepartmentid);

						// *住院科室
						String inhospitaldepartment = getDepartmentName(inhospitaldepartmentid);
						rj.setInhospitaldepartment(inhospitaldepartment);

						// 院后
						// *出院科室ID√
						String outhospitaldepartmentid = rst.getString("dept_discharge_from");
						rj.setOuthospitaldepartmentid(outhospitaldepartmentid);
						// *出院科室√
						rj.setOuthospitaldepartment(inhospitaldepartment);
						// *首页出院日期(患者离院日期)√
						String outhospitaldatehome = rst.getString("discharge_date_time");
						if (ValidateUtil.isNotNull(outhospitaldatehome)) {
							Date parse = DateUtil.parse(outhospitaldatehome, "yyyy-MM-dd hh:mm:ss");
							Timestamp timesTamp = DateUtil.getSqlTimestamp(parse);
							rj.setOuthospitaldatehome(timesTamp);
						}
						// *结算出院日期√
						String outhospitaldateclose = rst.getString("jsrq");
						if (ValidateUtil.isNotNull(outhospitaldateclose)) {
							Date inhospitaldateDate = DateUtil.parse(outhospitaldateclose, "yyyy-MM-dd hh:mm:ss");
							Timestamp outhospitaldatecloseTimesTamp = DateUtil.getSqlTimestamp(inhospitaldateDate);
							rj.setOuthospitaldateclose(outhospitaldatecloseTimesTamp);
						}

						// 出院诊断ICD码
						/*
						 * String outhospitaldiagnoseicd =
						 * rst.getString("DIAGNOSIS_CODE1");
						 * rj.setOuthospitaldiagnoseicd(outhospitaldiagnoseicd);
						 */
						// 出院情况
						// String outhospitinfo =
						// rst.getString("outhospitinfo");
						// 离院方式
						//String outhospitaltype = rst.getString("lyfs");
						//rj.setOuthospitaltype(outhospitaltype);
						/**
						 * 离院方式改为调用电子病历系统
						 */
						Map<String,Integer> map = getOutHositalType(patientid_his, inhospitalcount);
						if(map!=null && !map.isEmpty()){
							//24小时内入出院
							switch (map.get("IN_FLAG")) {
							case 0:
								//正常
								if(map.get("OUTSTATUS")!=null){
									switch (map.get("OUTSTATUS")) {
									case 1:
										rj.setOuthospitaltype(1);
										break;
									case 2:
										rj.setOuthospitaltype(2);
										break;
									case 3:
										rj.setOuthospitaltype(3);
										break;
									case 4:
										rj.setOuthospitaltype(4);
										break;
									case 5:
										rj.setOuthospitaltype(5);
										break;
									case 9:
										rj.setOuthospitaltype(7);
										break;
									}
								}else{
									rj.setOuthospitaltype(0);//未知
								}
								break;
							case 1:		
								//24小时内入出院
								rj.setOuthospitaltype(6);
								break;
							}
						}else{
							rj.setOuthospitaltype(0);//未知
						}
						// 档案号
						// String filenumber = rst.getString("filenumber");
						// 出院中医诊断疾病名称
						// String outhospitalchinadoctordiagnosediseasname =
						// rst.getString("outhospitalchinadoctordiagnosediseasname");
						// 出院中医诊断疾病名称编码
						// String outhospitalchinadoctordiagnosediseascode =
						// rst.getString("outhospitalchinadoctordiagnosediseascode");
						// 出院中医诊断证型
						// String outhospitalchinadoctordiagnosecardname =
						// rst.getString("outhospitalchinadoctordiagnosecardname");
						// 出院中医诊断证型编码
						// String outhospitalchinadoctordiagnosecardcode =
						// rst.getString("outhospitalchinadoctordiagnosecardcode");
						// 主要手术名称
						// String mainoperationname =
						// rst.getString("mainoperationname");
						// rj.setMainoperationname(mainoperationname);
						// 主要手术编码
						// String mainoperationcode =
						// rst.getString("mainoperationcode");
						// rj.setMainoperationcode(mainoperationcode);
						// 出院其他诊断名1
						// String outhospitalotherdiagnosenameone =
						// rst.getString("outhospitalotherdiagnosenameone");

						// String outhospitalotherdiagnosecodeone =
						// rst.getString("outhospitalotherdiagnosecodeone");
						//
						// String outhospitalotherdiagnosenametwo =
						// rst.getString("outhospitalotherdiagnosenametwo");
						//
						// String outhospitalotherdiagnosecodetwo =
						// rst.getString("outhospitalotherdiagnosecodetwo");
						//
						// String outhospitalotherdiagnosenamethree =
						// rst.getString("outhospitalotherdiagnosenamethree");
						//
						// String outhospitalotherdiagnosecodethree =
						// rst.getString("outhospitalotherdiagnosecodethree");
						//
						// String outhospitalotherdiagnosenamefour =
						// rst.getString("outhospitalotherdiagnosenamefour");
						//
						// String outhospitalotherdiagnosecodefour =
						// rst.getString("outhospitalotherdiagnosecodefour");
						//
						// String outhospitalotherdiagnosenamefive =
						// rst.getString("outhospitalotherdiagnosenamefive");
						//
						// String outhospitalotherdiagnosecodefive =
						// rst.getString("outhospitalotherdiagnosecodefive");
						// 电子病历或病案首页或出院记录或出院小结
						// String leavehospitalcontent =
						// rst.getString("leavehospitalcontent");

						String leavehospitalid = inhospitalid + inhospitalcount;

						// 出院记录编号
						rj.setOuthospitrecordid(leavehospitalid);

						// 获取电子病历...
						/*
						 * HongZeJbgHandler sj = new HongZeJbgHandler();
						 * Connection co = sj.conn; CallableStatement pstmt =
						 * co.prepareCall("{call dbo.usp_jk_getCyxj(?)}");
						 * pstmt.setString(1, inhospitalid);
						 * pstmt.executeQuery(); ResultSet rs =
						 * pstmt.getResultSet(); setRzzyyEmr(rs,
						 * leavehospitalid);
						 */

						/************************************************************************************************************************************
						 * wangsongyuan 章丘科室根据病种进行科室专访接口 20190110
						 * 
						 * 根据出院科室查询是否需要专访
						 * Department(科室表)exclusivestatus(专访状态)判断是否需要专访(0无需专访
						 * 1部分专访 2全部专访)分以下几种情况：
						 * 
						 * 1、无需专访
						 * ：根据出院记录的出院诊断ICD码，如果有诊断码则去Diseaselibrary(诊断表)查找所属病种，
						 * 如果匹配成功，则返回病种ID并设置RzzyyJbgl表的chronicDiseaseId(病种库表ID)
						 * 字段值 。 如果没有诊断码，则根据HIS患者ID和住院次数去电子病历接口查询。再与诊断表逐一比对。
						 * 匹配失败chronicDiseaseId均为空。
						 * 两种情况均将RzzyyJbgl表的exclusiveInterview(随访类别)值为1(
						 * 疾病与健康管理中心随访)。
						 * 
						 * 2、部分专访：根据出院记录的出院诊断ICD码，如果有诊断码则去Diseaselibrary(诊断表)
						 * 查找所属病种，
						 * 如果匹配成功，则返回病种ID并设置RzzyyJbgl表的chronicDiseaseId(病种库表ID)
						 * 字段值 。 根据出院科室去DepartAndChron(科室和病种类型关联表)查找需要专访病种。
						 * 如果没有诊断码，则根据HIS患者ID和住院次数去电子病历接口查询。再与诊断表逐一比对。
						 * 匹配失败chronicDiseaseId均为空。
						 * 如果匹配成功病种包括科室所管理的病种，则设置RzzyyJbgl表的exclusiveInterview(
						 * 随访类别)值为0(科室随访)，否则设为1(疾病与健康管理中心随访)。
						 * 
						 * 3、全部专访：根据出院记录的出院诊断ICD码，如果有诊断码则去Diseaselibrary(诊断表)
						 * 查找所属病种， 如果匹配成功，
						 * 则返回病种ID并设置RzzyyJbgl表的chronicDiseaseId(病种库表ID)字段值
						 * 。匹配失败chronicDiseaseId均为空。
						 * RzzyyJbgl表的exclusiveInterview(随访类别)值为0(科室随访)。
						 ************************************************************************************************************************************/
						Department dt = departMentOldMapper.getDepartmentByHisId(outhospitaldepartmentid);
						// 出院诊断ICD码
						String newIcd = rst.getString("DIAGNOSIS_CODE2");
						if (ValidateUtil.isNotNull(newIcd)) {
							rj.setOuthospitaldiagnoseicd(newIcd);
						}
						if(rj.getChronicDiseaseId()==null){
							if (dt != null) {
								if (dt.getExclusivestatus() != null) {
									if (dt.getExclusivestatus() == 0) {
										// 无需专访
										judgeICD(inhospitalcount, patientid_his, rj, newIcd);
										rj.setExclusiveInterview(1);
									} else if (dt.getExclusivestatus() == 1) {
										// 部分专访
										judgeICD(inhospitalcount, patientid_his, rj, newIcd, dt);
									} else if (dt.getExclusivestatus() == 2) {
										// 全部专访
										judgeICD(inhospitalcount, patientid_his, rj, newIcd);
										rj.setExclusiveInterview(0);
									}
								}
							}
						}
						/**
						 * *****************************************************
						 * *****专访end*******************************************
						 * *******************
						 */

						// 自费金额√
						String owncost = rst.getString("zf");
						if (ValidateUtil.isNotNull(owncost)) {
							rj.setOwncost(Double.valueOf(owncost));
						}

						// *总花费√
						String totalcost = rst.getString("TOTAL_COSTS");
						if (ValidateUtil.isNotNull(totalcost)) {
							rj.setTotalcost(Double.valueOf(totalcost));
						}

						// *费用类型√
						String costtype = rst.getString("CHARGE_TYPE");
						rj.setCosttype(costtype);// 新农村合作医疗

						// 住院标识
						/*
						 * int inhospitaltag = rst.getInt("zybs");
						 * rj.setInhospitaltag(inhospitaltag);
						 */

						// 过敏药物
						String allergydrug = rst.getString("ALERGY_DRUGS");
						if (ValidateUtil.isNotNull(allergydrug)) {
							rj.setAllergydrug(allergydrug);
						} else {
							rj.setAllergydrug("不详");
						}

						// RH（阴阳性）
						String rh = rst.getString("BLOOD_TYPE_RH");
						if (ValidateUtil.isNotNull(rh)) {
							rj.setRh(rh);
						} else {
							rj.setRh("不详");
						}

						// 入院途径√
						String inhospitalway = rst.getString("PATIENT_CLASS_NAME");
						rj.setInhospitalway(inhospitalway);

						// 血型√
						String bloodtype = rst.getString("BLOOD_TYPE");
						if (ValidateUtil.isNotNull(bloodtype)) {
							rj.setBloodtype(bloodtype);
						} else {
							rj.setBloodtype("不详");
						}

						// 出院诊断/门诊诊断√
						String outhospitaldiagnose = rst.getString("diagnosis_desc1");
						rj.setOuthospitaldiagnose(outhospitaldiagnose);

						// *住院天数
						/*
						 * String inhospitaldays =
						 * rst.getString("inhospitaldays"); if
						 * (ValidateUtil.isNotNull(inhospitaldays)) {
						 * rj.setInhospitaldays(Integer.parseInt(inhospitaldays)
						 * ); }
						 */
						// *入院日期√
						String inhospitaldate = rst.getString("admission_date_time");
						if (ValidateUtil.isNotNull(inhospitaldate)) {
							Date inhospitaldateDate = DateUtil.parse(inhospitaldate, "yyyy-MM-dd");
							Timestamp inhospitaldateTimesTamp = DateUtil.getSqlTimestamp(inhospitaldateDate);
							rj.setInhospitaldate(inhospitaldateTimesTamp);
						}

						// 结算类型
						// String closetype = rst.getString("closetype");
						// rj.setClosetype(closetype);//正常

						// *主管医生id√
						String maindoctorid = rst.getString("doctorid");
						rj.setMaindoctorid(maindoctorid);

						// *主管医师姓名√
						// if(ValidateUtil.isNotNull(maindoctorid)&&status==2){
						String maindoctorname = rst.getString("MAINDOCTORNAME");
						rj.setMaindoctorname(maindoctorname);
						// }

						// *门诊医师ID√
						// String doordoctorid =
						// rst.getString("CONSULTING_DOCTOR");
						// rj.setDoordoctorid(doordoctorid);
						// *门诊医师
						// String doordoctorname =
						// getDoordoctNameById(doordoctorid);
						// rj.setDoordoctorname(doordoctorname);

						// *住院号
						rj.setInhospitalid(inhospitalid);
						// 就诊号
						// String visitnum = rst.getString("visitnum");
						/* 缺少该字段 */

						// *住院次数
						if (ValidateUtil.isNotNull(inhospitalcount)) {
							rj.setInhospitalcount(Integer.parseInt(inhospitalcount));
						}

						// 电子病历编号或病案首页编号或出院记录编号或出院小结编号
						// String outhospitrecordid =
						// rst.getString("outhospitrecordid");//没这个字段

						// 有无药物过敏
						// String drugallergy = rst.getString("drugallergy");
						// rj.setDrugallergy(drugallergy);//0

						/*
						 * switch (rh) { case 0: rj.setRh("未查"); break; case 1:
						 * rj.setBloodtype("阴"); break; case 2:
						 * rj.setBloodtype("阳"); break; case 3:
						 * rj.setBloodtype("不详"); break; default:
						 * rj.setBloodtype("不详"); break; }
						 */
						// *病理诊断名称
						// String pathologydiagnosename =
						// rst.getString("pathologydiagnosename");
						// rj.setPathologydiagnosename(pathologydiagnosename);
						// *病理诊断码
						// String pathologydiagnosecode =
						// rst.getString("pathologydiagnosecode");
						// rj.setPathologydiagnosecode(pathologydiagnosecode);

						// *主要手术名称
						/*
						 * String mainoperationname =
						 * rst.getString("mainoperationname");
						 * rj.setMainoperationname(mainoperationname); //
						 * *主要手术编码 String mainoperationcode =
						 * rst.getString("mainoperationcode");
						 * rj.setMainoperationcode(mainoperationcode); //其它手术名称1
						 * String otheroperationnameone =
						 * rst.getString("otheroperationnameone");
						 * rj.setOtheroperationnameone(otheroperationnameone);
						 * //其它手术编码1 String otheroperationcodeone =
						 * rst.getString("otheroperationcodeone");
						 * rj.setOtheroperationcodeone(otheroperationcodeone);
						 * //其它手术名称2 String otheroperationnametwo =
						 * rst.getString("otheroperationnametwo");
						 * rj.setOtheroperationnametwo(otheroperationnametwo);
						 * //其它手术编码2 String otheroperationcodetwo =
						 * rst.getString("otheroperationcodetwo");
						 * rj.setOtheroperationcodetwo(otheroperationcodetwo);
						 * //其它手术名称3 String otheroperationnamethree =
						 * rst.getString("otheroperationnamethree");
						 * rj.setOtheroperationnamethree(otheroperationnamethree
						 * ); //其它手术编码3 String otheroperationcodethree =
						 * rst.getString("otheroperationcodethree");
						 * rj.setOtheroperationcodethree(otheroperationcodethree
						 * ); //其它手术名称4 String otheroperationnamefour =
						 * rst.getString("otheroperationnamefour");
						 * rj.setOtheroperationnamefour(otheroperationnamefour);
						 * //其它手术编码4 String otheroperationcodefour =
						 * rst.getString("otheroperationcodefour");
						 * rj.setOtheroperationcodefour(otheroperationcodefour);
						 */

						/*
						 * int bloodtype = rst.getInt("blood_type"); switch
						 * (bloodtype) { case 0: rj.setBloodtype("未查"); break;
						 * case 1: rj.setBloodtype("A"); break; case 2:
						 * rj.setBloodtype("B"); break; case 3:
						 * rj.setBloodtype("O"); break; case 4:
						 * rj.setBloodtype("AB"); break; case 5:
						 * rj.setBloodtype("不详"); break; default:
						 * rj.setBloodtype("不详"); break; }
						 */

						// 医保金额
						/*
						 * String healthinsurancecost =
						 * rst.getString("healthinsurancecost");
						 * if(ValidateUtil.isNotNull(healthinsurancecost)){
						 * rj.setHealthinsurancecost(Double.valueOf(
						 * healthinsurancecost)); }
						 */
						// *年龄单位
						rj.setAgeunit("岁");

						// *生日√
						Timestamp newbirthday = null;
						String birthday = rst.getString("date_of_birth");
						if (ValidateUtil.isNotNull(birthday)) {
							SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
							DateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
							birthday = sf2.format(sf1.parse(birthday));
							birthday = birthday + " 00:00:00";
							newbirthday = Timestamp.valueOf(birthday);
						}

						// *身份证号√
						String cardnum = rst.getString("id_no");

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
						} else {
							rj.setNation("不详");
						}

						// *姓名√
						rj.setName(name);
						rj.setCardnum(cardnum);// 设置身份证号码
						rj.setBirthday(newbirthday);// 设置生日

						// *病人电话√
						String patientphone = rst.getString("phone_number_home");
						rj.setPatientphone(patientphone);
						// *病人单位电话√
						String companyphone = rst.getString("PHONE_NUMBER_BUSINESS");
						rj.setCompanyphone(companyphone);
						// *联系人电话√
						String relationphone = rst.getString("NEXT_OF_KIN_PHONE");
						rj.setRelationphone(relationphone);

						// *年龄
						rj.setAge(getAgeByCardNum(cardnum, newbirthday));

						// *性别
						rj.setSex(sex);
						// 婚姻状况√
						String marry = rst.getString("Marital_Status");
						if (ValidateUtil.isNotNull(marry)) {
							rj.setMarry(marry);
						} else {
							rj.setMarry("不详");
						}
						// 职业√
						String profession = rst.getString("occupation_name");
						rj.setProfession(profession);
						// 现住址√
						/*
						 * wangsongyuan 新增省市区字段 20190214
						 */
						String province = rst.getString("BIRTH_PLACE_PROVINCE");
						String city = rst.getString("BIRTH_PLACE_CITY");
						String area = rst.getString("BIRTH_PLACE_AREA");
						String street = rst.getString("BIRTH_PLACE_STREET");
						String currentaddress = rst.getString("MAILING_ADDRESS");
						StringBuffer currentaddressbf = new StringBuffer();
						if(ValidateUtil.isNotNull(province)){
							currentaddressbf.append(province);
						}
						if(ValidateUtil.isNotNull(city)){
							currentaddressbf.append(city);
						}
						if(ValidateUtil.isNotNull(area)){
							currentaddressbf.append(area);
						}
						if(ValidateUtil.isNotNull(street)){
							currentaddressbf.append(street);
						}
						if (ValidateUtil.isNotNull(currentaddress)) {
							currentaddressbf.append("(").append(currentaddress).append(")");
						}
						rj.setCurrentaddress(currentaddressbf.toString());
						// 联系住址√
						String teladdress = rst.getString("NEXT_OF_KIN_ADDR");
						rj.setTeladdress(teladdress);
						// 工作单位√
						String company = rst.getString("UNIT_IN_CONTRACT");
						if (ValidateUtil.isNotNull(company)) {
							rj.setCompany(company);
						} else {
							rj.setCompany("不详");
						}
						// 联系人名称
						String telname = rst.getString("next_of_kin");
						rj.setTelname(telname);
						// 与联系人关系
						String relation = rst.getString("relationship_name");
						rj.setRelation(relation);
						// 教育程度
						/*
						 * String education = rst.getString("education");
						 * rj.setEducation(education);
						 */

						// *主治医生的HIS唯一标识
						String jobnum = rst.getString("doctorid");
						if (ValidateUtil.isNotNull(jobnum)) {
							rj.setJobnum(jobnum);
						}

						// 章丘人民医院新增HIS患者ID字段
						if (ValidateUtil.isNotNull(patientid_his)) {
							rj.setPatientid_his(patientid_his);
						}

						// 院后2
						rj.setIsStatus(2);
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
				/***
				 * 大表分小表start
				 */
				Map<String,Object> map = new HashMap<>();
				map.put("isStatus", 2);
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
					Department department = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
					if(!ValidateUtil.isEquals("需要", department.getNeedfollowup())){
						rj.setIspigeonhole(2);
						rzzyyJbglMapper.updateRzzyyJbgl(rj);
						continue;
					}
					setOutHospital(rj, department, p);
					rj.setIspigeonhole(2);
					rzzyyJbglMapper.updateRzzyyJbgl(rj);
				}
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
	
	/**
	 * 修改错误提示状态
	 * @author WangSongYuan
	 * @param rj
	 */
	private void setErrorMessageStatus(RzzyyJbgl rj){
		Map<String,Object> para= new HashMap<>();
		para.put("patientid_his", rj.getPatientid_his());
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
			para.put("patientid_his", rj.getPatientid_his());
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
			return true;
		}
		//新增未分配管床医生
		if(ValidateUtil.isNull(rj.getMaindoctorid())){
			ap.append("</br>未分配管床医生</br>");
			return true;
		}
		//如果不是小孩，且身份证号为空
		if(ValidateUtil.isNull(rj.getCardnum()) && ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE){
			ap.append("</br>身份证号为空</br>");
			return true;
		}else{
			if(ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE && !CardNumUtil.isLength(rj.getCardnum())){
				ap.append("</br>身份证号长度不为18位</br>");
				return true;
			}
			if(ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE && !CardNumUtil.isCard17(rj.getCardnum())){
				ap.append("</br>身份证号前17位不为纯数字</br>");
				return true;
			}
			if(ValidateUtil.isEquals("岁", rj.getAgeunit()) && rj.getAge()>AGE && !CardNumUtil.isCheckCode(rj.getCardnum())){
				ap.append("</br>身份证号校验码错误</br>");
				return true;
			}
		}
		//手机号空
		if(ValidateUtil.isNull(rj.getPatientphone()) && ValidateUtil.isNull(rj.getRelationphone())){
			ap.append("</br>手机号为空</br>");
			return true;
		}else{
			//手机号错误
			if(!isNum(rj.getPatientphone()) && !isNum(rj.getRelationphone())){
				ap.append("</br>手机号填写错误</br>");
				return true;
			}
		}
		//传入的HIS科室ID为空
		if(ValidateUtil.isNull(rj.getInhospitaldepartmentid())){
			ap.append("</br>HIS传入的科室ID为空</br>");
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

	/**
	 * 根据科室ID查询科室名称
	 * 
	 * @param id
	 * @return
	 */
	private static String getDepartmentName(String id) {
		Connection conn = getHisConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT OFFICE FROM t_office WHERE STOPFLAG = 1 AND officeid = '" + id + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			String office = "";
			while (rs.next()) {
				office = rs.getString("office");
			}
			stmt.close();
			rs.close();
			return office;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
            if(rs !=null)rs.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        try {
	            if(stmt !=null)stmt.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        try {
	            if(conn !=null)conn.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		return "";
	}

	/**
	 * 判断诊断ICD
	 * 
	 * @param inhospitalcount
	 * @param patientid_his
	 * @param rj
	 * @param newIcd
	 */
	private void judgeICD(String inhospitalcount, String patientid_his, RzzyyJbgl rj, String newIcd) {
		if (newIcd != null && !newIcd.equals("null")) {
			Diseaselibrary diseaselibrary = diseaselibraryOldMapper.getDiseaselibraryByIcd(newIcd);
			if (diseaselibrary != null && !newIcd.equals("null")) {
				// 匹配成功
				rj.setChronicDiseaseId(diseaselibrary.getChronid());
			} else {
				ppError(inhospitalcount, patientid_his, rj);
			}
		} else {
			ppError(inhospitalcount, patientid_his, rj);
		}
	}

	/**
	 * 部分专访判断
	 * 
	 * @param inhospitalcount
	 * @param patientid_his
	 * @param rj
	 * @param newIcd
	 */
	private void judgeICD(String inhospitalcount, String patientid_his, RzzyyJbgl rj, String newIcd,
			Department department) {
		if (newIcd != null && !newIcd.equals("null")) {
			Diseaselibrary diseaselibrary = diseaselibraryOldMapper.getDiseaselibraryByIcd(newIcd);
			if (diseaselibrary != null) {
				// 匹配成功
				rj.setChronicDiseaseId(diseaselibrary.getChronid());
				isExclusiveInterview(rj, department, diseaselibrary.getChronid());
			} else {
				ppError(inhospitalcount, patientid_his, rj, department);
			}
		} else {
			ppError(inhospitalcount, patientid_his, rj, department);
		}
	}

	/**
	 * 匹配失败(部分专访)
	 * 
	 * @param inhospitalcount
	 * @param patientid_his
	 * @param rj
	 * @param department
	 */
	private void ppError(String inhospitalcount, String patientid_his, RzzyyJbgl rj, Department department) {
		// 匹配失败
		List<String> bzList = getIcdName(patientid_his, inhospitalcount);
		if (bzList.size() > 0) {
			Map<String,Object> para = new HashMap<>();
			para.put("diagnosenames", bzList);
			List<String> bdbzList = diseaselibraryOldMapper.getDiseaseLibraryByEMR(para);
			// wangsongyuan 因为电子病历接口只有诊断名称所以哪个先匹配上就算哪个病种 20190111
			if (!bdbzList.isEmpty()) {
				for (String d : bdbzList) {
					Integer chronid = Integer.valueOf(d);
					rj.setChronicDiseaseId(chronid);
					isExclusiveInterview(rj, department, chronid);
					break;
				}
			} else {
				// wangsongyuan 都没匹配上就由疾病管理中心访 20190111
				rj.setExclusiveInterview(1);
			}
		}
	}

	/**
	 * 匹配失败
	 * 
	 * @param inhospitalcount
	 * @param patientid_his
	 * @param rj
	 * @param state
	 */
	private void ppError(String inhospitalcount, String patientid_his, RzzyyJbgl rj) {
		// 匹配失败
		List<String> bzList = getIcdName(patientid_his, inhospitalcount);
		if (bzList.size() > 0) {
			Map<String,Object> para = new HashMap<>();
			para.put("diagnosenames", bzList);
			List<String> bdbzList = diseaselibraryOldMapper.getDiseaseLibraryByEMR(para);
			// wangsongyuan 因为电子病历接口只有诊断名称所以哪个先匹配上就算哪个病种 20190111
			for (String d : bdbzList) {
				rj.setChronicDiseaseId(Integer.valueOf(d));
				break;
			}
		}
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
	 * 判断是否科室随访
	 * 
	 * @param rj
	 * @param department
	 * @param chronid
	 */
	private void isExclusiveInterview(RzzyyJbgl rj, Department department, int chronid) {
		Map<String, Object> para = new HashMap<>();
		para.put("departid", department.getId());
		para.put("chronid", chronid);
		List<DepartAndChron> bdbzList = departAndChronOldMapper.getDepartAndChronList(para);
		if (bdbzList.isEmpty()) {
			// 疾病与健康管理中心随访
			rj.setExclusiveInterview(1);
		} else {
			// 科室随访
			rj.setExclusiveInterview(0);
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

	private List<String> getIcdName(String patientHisId, String inhospitalcount) {
		Connection conn = getEMRConn();
		List<String> list = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select diagnosename from outhospital where tpatientid_his = '" + patientHisId
					+ "' and inhospitalcount = '" + inhospitalcount + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString("DIAGNOSENAME"));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
            if(rs !=null)rs.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        try {
	            if(stmt !=null)stmt.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        try {
	            if(conn !=null)conn.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		
		}
		return list;
	}

	private Map<String,Integer> getOutHositalType(String patientHisId, String inhospitalcount) {
		Connection conn = getNewEMRConn();
		Statement stmt = null;
		ResultSet rs = null;
		Map<String,Integer> map = new HashMap<String, Integer>();
		try {
			String sql = "SELECT OUTSTATUS, IN_FLAG FROM InOrOuthospitalinfoList_bl WHERE PATIENTID_HIS = '"+patientHisId+"' AND IN_COUNT = '"+inhospitalcount+"' AND IN_STATUS = 2";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				if(ValidateUtil.isNotNull(rs.getString("OUTSTATUS"))){
					map.put("OUTSTATUS", rs.getInt("OUTSTATUS"));
				}else{
					map.put("OUTSTATUS", null);
				}
				if(ValidateUtil.isNotNull(rs.getString("IN_FLAG"))){
					map.put("IN_FLAG", rs.getInt("IN_FLAG"));
				}else{
					map.put("IN_FLAG", null);
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
            if(rs !=null)rs.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        try {
	            if(stmt !=null)stmt.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        try {
	            if(conn !=null)conn.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		
		}
		return null;
	}
	
	private static Connection getNewEMRConn() {
		String ip = "192.168.1.13";
		String sid = "JHEMR";
		String port = "1521";
		String dbUser = "jhdisease";
		String dbPassword = "jhdisease";
		String driver = "oracle.jdbc.driver.OracleDriver";
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
	
	private static Connection getEMRConn() {
		String ip = "192.168.1.21";
		String sid = "jhemr";
		String port = "1521";
		String dbUser = "jhemr";
		String dbPassword = "jhmk";
		String driver = "oracle.jdbc.driver.OracleDriver";
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
	 * 将数据插入科室表
	 * @author WangSongYuan
	 * @param rj
	 */
	private void setDepartment(RzzyyJbgl rj){
			//院后
			Department dt = departMentOldMapper.getDepartmentByHisId(rj.getOuthospitaldepartmentid());
			if(dt==null){
				Department d = new Department();//科室
				if(ValidateUtil.isNotNull(rj.getOuthospitaldepartmentid())){
					d.setThirdpartyhisid(rj.getOuthospitaldepartmentid());
				}
				d.setName(rj.getOuthospitaldepartment());//科室名称
				//wangsongyuan 新增科室排期随访 默认值 20170719
				d.setNeedfollowup("需要");
				//wangsongyuan 新增科室类别 20180608
				d.setStatus(1);
				departMentOldMapper.setDepartment(d);
				//往中间表里添加数据
			}else{
				dt.setName(rj.getOuthospitaldepartment());//科室名称
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
		para.put("patientid_his", rj.getPatientid_his());
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
		if(rj.getIsStatus()==1&&outh.getHealthrecordstate()!=2){
			//outh.setHealthrecordstate(1);
			outh.setHealthrecordstate(2);//章丘临时改成2
		}
		//wangsongyuan 科室专访状态 20181115
		outh.setExclusiveInterview(rj.getExclusiveInterview());
		//wangsongyuan 疾病病种 20181115
		outh.setChronicDiseaseId(rj.getChronicDiseaseId());
		if(outofthehospitalinhospitalinformation != null){
			//未排期且非医嘱离院、医嘱转院、死亡、24小时内出院，直接设置为勿访，并标记勿访原因，更改操作时间
			if(outh.getSchedulingstate()==1&&(rj.getOuthospitaltype()==2||rj.getOuthospitaltype()==4||rj.getOuthospitaltype()==5||rj.getOuthospitaltype()==6)){
				outh.setSchedulingstate(3);
				outh.setDonotvisitthecause("非管理患者，系统自动设置为勿访！");
			}
			/*if(rj.getOuthospitaldateclose() != null && !rj.getOuthospitaldateclose().equals(oldOuthospitaldateclose)){
				outh.setOuthospitaldateclose(oldOuthospitaldateclose);
			}*/
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
			//非医嘱离院、医嘱转院、死亡、24小时内出院，直接设置为勿访，并标记勿访原因，更改操作时间
			if(rj.getOuthospitaltype()==2||rj.getOuthospitaltype()==4||rj.getOuthospitaltype()==5||rj.getOuthospitaltype()==6){
				outh.setSchedulingstate(3);
				outh.setDonotvisitthecause("非管理患者，系统自动设置为勿访！");
			}
			outofthehospitalinhospitalinformationOldMapper.setOut(outh);
		}
	}
}