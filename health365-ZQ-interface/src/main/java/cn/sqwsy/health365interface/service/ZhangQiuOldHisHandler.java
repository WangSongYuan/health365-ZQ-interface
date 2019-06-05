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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.sqwsy.health365interface.dao.entity.DepartAndChron;
import cn.sqwsy.health365interface.dao.entity.Department;
import cn.sqwsy.health365interface.dao.entity.Diseaselibrary;
import cn.sqwsy.health365interface.dao.entity.RzzyyJbgl;
import cn.sqwsy.health365interface.dao.mapper.DepartAndChronOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DepartMentOldMapper;
import cn.sqwsy.health365interface.dao.mapper.DiseaselibraryOldMapper;
import cn.sqwsy.health365interface.dao.mapper.RzzyyJbglOldMapper;
import cn.sqwsy.health365interface.service.utils.CardNumUtil;
import cn.sqwsy.health365interface.service.utils.DateUtil;
import cn.sqwsy.health365interface.service.utils.ValidateUtil;

@Component
public class ZhangQiuOldHisHandler {
	@Autowired
	private RzzyyJbglOldMapper rzzyyJbglMapper;

	@Autowired
	private DiseaselibraryOldMapper diseaselibraryOldMapper;

	@Autowired
	private DepartMentOldMapper departMentOldMapper;

	@Autowired
	private DepartAndChronOldMapper departAndChronOldMapper;

	@Scheduled(fixedDelay = 600000)
	public void fixedRateJob() {
		Connection conn = getHisConn();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			for (int i = 0; i <= 3; i++) {
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
						String outhospitaltype = rst.getString("lyfs");
						rj.setOuthospitaltype(outhospitaltype);
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
						currentaddressbf.append(province).append(city).append(area).append(street);
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
			}
			pstmt.close();
			conn.close();
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
}
