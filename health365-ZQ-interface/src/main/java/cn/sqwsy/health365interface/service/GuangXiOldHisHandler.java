package cn.sqwsy.health365interface.service;

import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
@SuppressWarnings("all")
public class GuangXiOldHisHandler {
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

	@Scheduled(fixedDelay = 1200000)
	public void fixedRateJob() {
		for (int i = 0; i <= 6; i++) {
			Calendar outStartCal = Calendar.getInstance(); 
			outStartCal.add(Calendar.DAY_OF_MONTH,-i);
			outStartCal.set(Calendar.HOUR_OF_DAY, 0); 
			outStartCal.set(Calendar.SECOND, 0); 
			outStartCal.set(Calendar.MINUTE, 0); 
			Date outSstartTime = outStartCal.getTime();
			String outDateHomeStart = DateUtil.format(outSstartTime, "yyyy-MM-dd");
			System.out.println("院后开始时间=【"+outDateHomeStart+"】");
			//数据抓取结束时间
			Calendar endCal = Calendar.getInstance(); 
			endCal.add(Calendar.DAY_OF_MONTH,-i);
			endCal.set(Calendar.HOUR_OF_DAY, 23); 
			endCal.set(Calendar.SECOND, 59); 
			endCal.set(Calendar.MINUTE, 59); 
			Date endTime = endCal.getTime();
			String dateHomeEnd = DateUtil.format(endTime, "yyyy-MM-dd");
			
			StringBuffer outnXmlbf = new StringBuffer("<request><starttime>");
			outnXmlbf.append(outDateHomeStart).append("</starttime><endtime>").append(dateHomeEnd).append("</endtime><ctloc></ctloc></request>");
			String object =  callinginterface("getOuthospitalList",outnXmlbf);
			if(ValidateUtil.isNotNull(object)){
				toXml(object,2);
			}else{
				continue;
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
			Map<String,Object> para= new HashMap<>();
			para.put("visitnum", rj.getVisitnum());
			Outofthehospitalinhospitalinformation outofthehospitalinhospitalinformation = outofthehospitalinhospitalinformationOldMapper.getOut(para);
			setOutHospital(rj, department, p,outofthehospitalinhospitalinformation);
			rj.setIspigeonhole(2);
			rzzyyJbglMapper.updateRzzyyJbgl(rj);
		}
	}
	
	private void toXml(String object, Integer status){
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(object), "utf-8");
		} catch (Exception e) {
			return;
		}
		
		if(document!=null){
			try {
				Element root = document.getRootElement();
				Element out = root.element("RECODE");
				if(out==null){
					return;
				}
				List<Element> list = out.elements("Item");
				for (int i = 0; i < list.size(); i++) {
					try {
						Element dataElements = list.get(i);
						// 住院号
						String inhospitalid = dataElements.element("inhospitalid").getText();
						// 住院次数
						String inhospitalcount = dataElements.element("inhospitalcount").getText();
						
						// 就诊号
						String visitnum = dataElements.elementText("visitnum");
						if (ValidateUtil.isNull(visitnum)) {
							continue;
						}
						
						RzzyyJbgl rj = new RzzyyJbgl();
						Map<String, Object> para = new HashMap<>();
						para.put("visitnum", visitnum);
						RzzyyJbgl rzzyyJbgl = rzzyyJbglMapper.getRzzyyJbgl(para);
						if (rzzyyJbgl != null) {
							rj = rzzyyJbgl;
						}
						// 姓名
						String name = dataElements.elementText("name");
						// 性别
						String sex = dataElements.elementText("sex");
						if(status==2){
							//院后
							
							//出院科室
							String outhospitaldepartment = dataElements.element("outhospitaldepartment").getText();
							rj.setOuthospitaldepartment(outhospitaldepartment);
							//出院科室ID
							String outhospitaldepartmentid = dataElements.element("outhospitaldepartmentid").getText();
							rj.setOuthospitaldepartmentid(outhospitaldepartmentid);
							//首页出院日期(患者离院日期)
							String outhospitaldatehome = dataElements.element("outhospitaldatehome").getText();
							Date parse = DateUtil.parse(outhospitaldatehome, "yyyy-MM-dd hh:mm:ss");
							Timestamp timesTamp = DateUtil.getSqlTimestamp(parse);
							rj.setOuthospitaldatehome(timesTamp);
							//结算出院日期
							String outhospitaldateclose = dataElements.element("outhospitaldateclose").getText();
							if (ValidateUtil.isNotNull(outhospitaldateclose)) {
								Date inhospitaldateDate = DateUtil.parse(outhospitaldateclose, "yyyy-MM-dd hh:mm:ss");
								Timestamp outhospitaldatecloseTimesTamp = DateUtil.getSqlTimestamp(inhospitaldateDate);
								rj.setOuthospitaldateclose(outhospitaldatecloseTimesTamp);
							}else{
								rj.setOuthospitaldateclose(timesTamp);
							}
							//出院诊断
							String outhospitaldiagnose = dataElements.element("outhospitaldiagnose").getText();
							rj.setOuthospitaldiagnose(outhospitaldiagnose);
							//出院诊断ICD码
							String outhospitaldiagnoseicd = dataElements.element("outhospitaldiagnoseicd").getText();
							rj.setOuthospitaldiagnoseicd(outhospitaldiagnoseicd);
							//出院情况
							String outhospitinfo = dataElements.element("outhospitinfo").getText();
							rj.setOuthospitinfo(outhospitinfo);
							//离院方式
							String outhospitaltype = dataElements.element("outhospitaltype").getText();
							//rj.setOuthospitaltype(outhospitaltype);
							//档案号
							String filenumber = dataElements.element("filenumber").getText();
							rj.setFilenumber(filenumber);
							//出院中医诊断疾病名称
							String outhospitalchinadoctordiagnosediseasname = dataElements.element("outhospitalchinadoctordiagnosediseasname").getText();
							//出院中医诊断疾病名称编码
							String outhospitalchinadoctordiagnosediseascode = dataElements.element("outhospitalchinadoctordiagnosediseascode").getText();
							//出院中医诊断证型
							String outhospitalchinadoctordiagnosecardname = dataElements.element("outhospitalchinadoctordiagnosecardname").getText();
							//出院中医诊断证型编码
							String outhospitalchinadoctordiagnosecardcode = dataElements.element("outhospitalchinadoctordiagnosecardcode").getText();
							//主要手术名称
							String mainoperationname = dataElements.element("mainoperationname").getText();
							//主要手术编码
							String mainoperationcode = dataElements.element("mainoperationcode").getText();
							//出院其他诊断名1
							String outhospitalotherdiagnosenameone = dataElements.element("outhospitalotherdiagnosenameone").getText();
							rj.setOuthospitalotherdiagnosenameone(outhospitalotherdiagnosenameone);
							String outhospitalotherdiagnosecodeone = dataElements.element("outhospitalotherdiagnosecodeone").getText();
							rj.setOuthospitalotherdiagnosecodeone(outhospitalotherdiagnosecodeone);
							String outhospitalotherdiagnosenametwo = dataElements.element("outhospitalotherdiagnosenametwo").getText();
							rj.setOuthospitalotherdiagnosenametwo(outhospitalotherdiagnosenametwo);
							String outhospitalotherdiagnosecodetwo = dataElements.element("outhospitalotherdiagnosecodetwo").getText();
							rj.setOuthospitalotherdiagnosecodetwo(outhospitalotherdiagnosecodetwo);
							String outhospitalotherdiagnosenamethree = dataElements.element("outhospitalotherdiagnosenamethree").getText();
							rj.setOuthospitalotherdiagnosenamethree(outhospitalotherdiagnosenamethree);
							String outhospitalotherdiagnosecodethree = dataElements.element("outhospitalotherdiagnosecodethree").getText();
							rj.setOuthospitalotherdiagnosecodethree(outhospitalotherdiagnosecodethree);
							String outhospitalotherdiagnosenamefour = dataElements.element("outhospitalotherdiagnosenamefour").getText();
							rj.setOuthospitalotherdiagnosenamefour(outhospitalotherdiagnosenamefour);
							String outhospitalotherdiagnosecodefour = dataElements.element("outhospitalotherdiagnosecodefour").getText();
							rj.setOuthospitalotherdiagnosecodefour(outhospitalotherdiagnosecodefour);
							String outhospitalotherdiagnosenamefive = dataElements.element("outhospitalotherdiagnosenamefive").getText();
							rj.setOuthospitalotherdiagnosenamefive(outhospitalotherdiagnosenamefive);
							String outhospitalotherdiagnosecodefive = dataElements.element("outhospitalotherdiagnosecodefive").getText();
							rj.setOuthospitalotherdiagnosecodefive(outhospitalotherdiagnosecodefive);
							//电子病历或病案首页或出院记录或出院小结
//							String leavehospitalcontent  = dataElements.element("leavehospitalcontent").getText();
							
							//String leavehospitalid =inhospitalid+inhospitalcount;
							String leavehospitalid =visitnum;
							
							//出院记录编号
							rj.setOuthospitrecordid(leavehospitalid);
							//获取电子病历
							String emrXmlBf = dataElements.elementText("leavehospitalcontent");
							String emrXml[] = emrXmlBf.split("\\|");
							StringBuffer emrBf = new StringBuffer();
							for(String a:emrXml){
								emrBf.append("<p style='font-size:16px;margin:12px'>").append(a).append("</p>");
							}
							setRzzyyEmr(emrBf.toString(),leavehospitalid);
							// 住院天数
							String inhospitaldays = dataElements.elementText("inhospitaldays");
							if (ValidateUtil.isNotNull(inhospitaldays)) {
								rj.setInhospitaldays(Integer.parseInt(inhospitaldays));
							}
							
							//就诊卡号
							String patNo = dataElements.elementText("PatNo");
							if(ValidateUtil.isNotNull(patNo)){
								rj.setPatNo(patNo);
							}
							//登记号
							String CardNo = dataElements.elementText("CardNo");
							if(ValidateUtil.isNotNull(CardNo)){
								rj.setCardNo(CardNo);
							}
						}
						// 住院科室
						String inhospitaldepartment = dataElements.elementText("inhospitaldepartment");
						rj.setInhospitaldepartment(inhospitaldepartment);
						// 住院科室ID
						String inhospitaldepartmentid = dataElements.elementText("inhospitaldepartmentid");
						rj.setInhospitaldepartmentid(inhospitaldepartmentid);
						// 入院日期
						String inhospitaldate = dataElements.elementText("inhospitaldate");
						if (ValidateUtil.isNotNull(inhospitaldate)) {
							Date inhospitaldateDate = DateUtil.parse(inhospitaldate, "yyyy-MM-dd hh:mm:ss");
							Timestamp inhospitaldateTimesTamp = DateUtil.getSqlTimestamp(inhospitaldateDate);
							rj.setInhospitaldate(inhospitaldateTimesTamp);
						}
						// 结算类型
						 String closetype = dataElements.elementText("closetype");
						 //rj.setClosetype(closetype);//正常
						 
						// 总花费
						 String totalcost = dataElements.elementText("totalcost");
						 if (ValidateUtil.isNotNull(totalcost)) {
							 rj.setTotalcost(Double.valueOf(totalcost));
						 }
						// 主管医师
						String maindoctorname = dataElements.elementText("maindoctorname");
						rj.setMaindoctorname(maindoctorname);
						// 主管医生id
						String maindoctorid = dataElements.elementText("maindoctorid");
						rj.setMaindoctorid(maindoctorid);
						// 门诊医师
						String doordoctorname = dataElements.elementText("doordoctorname");
						rj.setDoordoctorname(doordoctorname);
						// 门诊医师ID
						String doordoctorid = dataElements.elementText("doordoctorid");
						rj.setDoordoctorid(doordoctorid);
						// 费用类型
						String costtype = dataElements.elementText("costtype");
						rj.setCosttype(costtype);//新农村合作医疗
						
						//住院号
						rj.setInhospitalid(inhospitalid);
					
						rj.setVisitnum(visitnum);
						/*缺少该字段*/
						
						// 住院次数
						if (ValidateUtil.isNotNull(inhospitalcount)) {
							rj.setInhospitalcount(Integer.parseInt(inhospitalcount));
						}
						// 住院标识
						String inhospitaltag = dataElements.elementText("inhospitaltag");
						//rj.setInhospitaltag(inhospitaltag);//门诊
						
						// 电子病历编号或病案首页编号或出院记录编号或出院小结编号
						//String outhospitrecordid = dataElements.elementText("outhospitrecordid");//没这个字段
						
						// 有无药物过敏
						String drugallergy = dataElements.elementText("drugallergy");
						rj.setDrugallergy(drugallergy);//0
						// 过敏药物
						String allergydrug = dataElements.elementText("allergydrug");
						rj.setAllergydrug(allergydrug);
						// RH（阴阳性）
						String rh = dataElements.elementText("rh");
						rj.setRh(rh);
						// 病理诊断名称
						String pathologydiagnosename = dataElements.elementText("pathologydiagnosename");
						rj.setPathologydiagnosename(pathologydiagnosename);
						// 病理诊断码
						String pathologydiagnosecode = dataElements.elementText("pathologydiagnosecode");
						rj.setPathologydiagnosecode(pathologydiagnosecode);
						// 入院途径
						String inhospitalway = dataElements.elementText("inhospitalway");
						rj.setInhospitalway(inhospitalway);
						// 主要手术名称
						String mainoperationname = dataElements.elementText("mainoperationname");
						rj.setMainoperationname(mainoperationname);
						// 主要手术编码
						String mainoperationcode = dataElements.elementText("mainoperationcode");
						rj.setMainoperationcode(mainoperationcode);
						//其它手术名称1
						String otheroperationnameone = dataElements.elementText("otheroperationnameone");
						rj.setOtheroperationnameone(otheroperationnameone);
						//其它手术编码1
						String otheroperationcodeone = dataElements.elementText("otheroperationcodeone");
						rj.setOtheroperationcodeone(otheroperationcodeone);
						//其它手术名称2
						String otheroperationnametwo = dataElements.elementText("otheroperationnametwo");
						rj.setOtheroperationnametwo(otheroperationnametwo);
						//其它手术编码2
						String otheroperationcodetwo = dataElements.elementText("otheroperationcodetwo");
						rj.setOtheroperationcodetwo(otheroperationcodetwo);
						//其它手术名称3
						String otheroperationnamethree = dataElements.elementText("otheroperationnamethree");
						rj.setOtheroperationnamethree(otheroperationnamethree);
						//其它手术编码3
						String otheroperationcodethree = dataElements.elementText("otheroperationcodethree");
						rj.setOtheroperationcodethree(otheroperationcodethree);
						//其它手术名称4
						String otheroperationnamefour = dataElements.elementText("otheroperationnamefour");
						rj.setOtheroperationnamefour(otheroperationnamefour);
						//其它手术编码4
						String otheroperationcodefour = dataElements.elementText("otheroperationcodefour");
						rj.setOtheroperationcodefour(otheroperationcodefour);
						// 血型
						String bloodtype = dataElements.elementText("bloodtype");
						rj.setBloodtype(bloodtype);
						// 自费金额
						String owncost = dataElements.elementText("owncost");
						if(ValidateUtil.isNotNull(owncost)){
							rj.setOwncost(Double.valueOf(owncost));
						}
						// 医保金额
						String healthinsurancecost = dataElements.elementText("healthinsurancecost");
						if(ValidateUtil.isNotNull(healthinsurancecost)){
							rj.setHealthinsurancecost(Double.valueOf(healthinsurancecost));
						}
						// 年龄单位
						String ageunit = dataElements.elementText("ageunit");
						rj.setAgeunit(ageunit);
						// 年龄
						String age = dataElements.elementText("age");
						if(ValidateUtil.isNotNull(age)){
							rj.setAge(Integer.parseInt(age));
						}
						// 名族
						String nation = dataElements.elementText("nation");
						rj.setNation(nation);
						
						rj.setName(name);
						// 病人电话
						String patientphone = dataElements.elementText("patientphone");
						rj.setPatientphone(patientphone);
						// 病人单位电话
						String companyphone = dataElements.elementText("companyphone");
						rj.setCompanyphone(companyphone);
						// 联系人电话
						String relationphone = dataElements.elementText("relationphone");
						rj.setRelationphone(relationphone);
						// 生日
						String birthday = dataElements.elementText("birthday");
						if (ValidateUtil.isNotNull(birthday)) {
							Timestamp newbirthday = Timestamp.valueOf(birthday + " 00:00:00");
							rj.setBirthday(newbirthday);
						}
						rj.setSex(sex);
						// 婚姻状况
						String marry = dataElements.elementText("marry");
						rj.setMarry(marry);
						// 职业
						String profession = dataElements.elementText("profession");
						rj.setProfession(profession);
						// 现住址
						String currentaddress = dataElements.elementText("currentaddress");
						rj.setCurrentaddress(currentaddress);
						// 联系住址
						String teladdress = dataElements.elementText("teladdress");
						rj.setTeladdress(teladdress);
						// 工作单位
						String company = dataElements.elementText("company");
						rj.setCompany(company);
						// 联系人名称
						String telname = dataElements.elementText("telname");
						rj.setTelname(telname);
						// 与联系人关系
						String relation = dataElements.elementText("relation");
						rj.setRelation(relation);
						// 教育程度
						String education = dataElements.elementText("education");
						rj.setEducation(education);
						// 身份证号
						String cardnum = dataElements.elementText("cardnum");
						rj.setCardnum(cardnum);
						// 主治医生的HIS唯一标识
						String jobnum = dataElements.elementText("jobnum");
						if (ValidateUtil.isNotNull(jobnum)) {
							rj.setJobnum(jobnum);
						}
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
			} catch (Exception e) {
				
			}
		}
	}
	
	private String callinginterface(String method,StringBuffer sb) {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://10.9.100.188/csp/dhcens/DHC.HMSS.BS.HisService.cls?wsdl");
		HTTPConduit conduit = (HTTPConduit) client.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		long timeout = 10 * 60 * 1000;
		policy.setConnectionTimeout(timeout);
		policy.setReceiveTimeout(timeout);
		conduit.setClient(policy);
		Object[] objects = new Object[0];
		try {
			//System.out.println(sb.toString());
			objects = client.invoke(method, sb.toString());
			client.close();
			System.out.println(objects[0].toString().substring(0, 200));
			return objects[0].toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 修改错误提示状态
	 * @author WangSongYuan
	 * @param rj
	 */
	private void setErrorMessageStatus(RzzyyJbgl rj){
		Map<String,Object> para= new HashMap<>();
		//para.put("thirdpartyhisid", rj.getId());
		//wangsongyuan 广西二附院唯一标识 20191029
		para.put("zy_code", rj.getVisitnum());
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
			//para.put("thirdpartyhisid", rj.getId());
			//wangsongyuan 广西二附院唯一标识 20191029
			para.put("zy_code", rj.getVisitnum());
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
			hiserr.setZy_code(rj.getVisitnum());
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
	
	private void setOutHospital(RzzyyJbgl rj,Department department,Patient p,Outofthehospitalinhospitalinformation outofthehospitalinhospitalinformation){
		Outofthehospitalinhospitalinformation outh = new Outofthehospitalinhospitalinformation();
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
		
		//出院护士站结算时间为空时设为出院时间 
		if(rj.getOuthospitaldateclose()!=null){
			outh.setOuthospitaldateclose(rj.getOuthospitaldateclose());
		}else{
			outh.setOuthospitaldateclose(rj.getOuthospitaldatehome());
		}
		
		outh.setClosetype(rj.getClosetype());
		outh.setTotalcost(rj.getTotalcost());
		outh.setMaindoctorname(rj.getMaindoctorname());
		//添加出院表中用户的id
		Map<String,Object> para= new HashMap<>();
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
		//新增状态值
		outh.setHisrecordstate(rj.getIsStatus());
		//HIS患者ID 
		if(ValidateUtil.isNotNull(rj.getPatientid_his())){
			outh.setPatientid_his(rj.getPatientid_his());
		}
		
		//广西医科大学第二附属医院新增字段
		if(ValidateUtil.isNotNull(rj.getPatNo())){
			outh.setPatNo(rj.getPatNo());//登记号
		}
		if(ValidateUtil.isNotNull(rj.getCardNo())){
			outh.setCardNo(rj.getCardNo());//就诊卡号
		}
		if(ValidateUtil.isNotNull(rj.getVisitnum())){
			outh.setVisitnum(rj.getVisitnum());//就诊号
		}
		
		if(rj.getIsStatus()==1&&outh.getHealthrecordstate()!=2){
			//outh.setHealthrecordstate(1);
			outh.setHealthrecordstate(2);//临时改成2
		}
		//科室专访状态 
		if(rj.getExclusiveInterview()!=null){
			outh.setExclusiveInterview(rj.getExclusiveInterview());
		}
		//疾病病种
		if(rj.getChronicDiseaseId()!=null){
			outh.setChronicDiseaseId(rj.getChronicDiseaseId());
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
			outofthehospitalinhospitalinformationOldMapper.setOut(outh);
		}
	}
	
	private void setRzzyyEmr(String xml,String leavehospitalid){
		if(ValidateUtil.isNotNull(xml)){
			RzzyyEmr re = new RzzyyEmr();
			Map<String,Object> map = new HashMap<>();
			map.put("leavehospitalid", leavehospitalid);
			RzzyyEmr rzzyyEmr = rzzyyEmrOldMapper.getEMR(map);
			if(rzzyyEmr != null){
				re = rzzyyEmr;
			}
			re.setLeavehospitalcontent(xml);
			re.setLeavehospitalid(leavehospitalid);
			if(rzzyyEmr != null){//存在就更新
				rzzyyEmrOldMapper.updateEMR(rzzyyEmr);
			}else{
				rzzyyEmrOldMapper.setEMR(re);
			}
		}
	}
}