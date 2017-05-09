package com.miniProject.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miniProject.model.AgencyScheduleVerifyForm;
import com.miniProject.model.UserInfo;
import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.model.subAgency;
import com.miniProject.service.AgencyScheduleServiceI;
import com.miniProject.service.HouseServiceI;
import com.miniProject.service.PushServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.RetMapUtils;


@Controller
@RequestMapping("/agencyschedule")
public class AgencyScheduleController {

	private static final Logger logger = LoggerFactory.getLogger(AgencyScheduleController.class);
	
	@Autowired
	private AgencyScheduleServiceI agencyscheduleservice;
	
	@Autowired
	private HouseServiceI houseService;
	
	@Autowired
	private PushServiceI pushService;

	
	////////////////////////////////////////////////////房源端获取直播日程//////////////////////////////////////////////////////////
	/****
	 * 查询当前房源发布者的直播预约信息
	 *@param userId 房源发布者id
	 *@param pageIndex 查询的分页号 
	 **/	
	@RequestMapping(value="verifyList")
	@ResponseBody
	public Object getScheduleVerifyList(HttpServletRequest request){
		Map<String, Object> result = null;	
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
	
		result=new HashMap<>();
		Map<String, Object> dataMap=new HashMap<String , Object>();
		List<AgencyScheduleVerifyForm> list = agencyscheduleservice.getAgencyScheduleVerifyByUserId(auth.getUserId());
		if(list == null || list.isEmpty()){
			logger.error("getAgencyScheduleVerifyByUserId failed");
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}		
		
		
		List<subAgency> list1 = agencyscheduleservice.getAgencyScheduleUser(auth.getUserId());
		if(list1 == null || list1.isEmpty()){
			logger.error("getAgencyScheduleUser failed");
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}		
		for(subAgency subList1 : list1){
			List<UserInfo> userList = new ArrayList<UserInfo>();
			for(AgencyScheduleVerifyForm subList2 : list){
				if(subList1.getHouseId().equals(subList2.getHouseId()) && subList1.getSubBeginTime().equals(subList2.getSubBeginTime())){
					UserInfo userinfo = new UserInfo();
					userinfo.setUserId(subList2.getSubUserId());
					userinfo.setUserName(subList2.getSubUserName());
					userinfo.setPhoneNumber(subList2.getSubPhoneNumber());
					userinfo.setPortraitFileName(subList2.getPortraitFileName());
					userList.add(userinfo);
				}
			}
			subList1.setUserlist(userList);
			subList1.setHousePic(houseService.getPicListByHouseId(subList1.getHouseId()));
			if(!RetMapUtils.affirmNull(subList1.getHousePic()) && !subList1.getHousePic().isEmpty()){
				subList1.setHouseShowPic(subList1.getHousePic().get(0));	
			}
			else{
				subList1.setHouseShowPic("./image/default.jpg");
			}
		}
				
		dataMap.put("vals", list1);
		result.put("code", 8200);
		result.put("data", dataMap);
		return result;
		
	}
	
	/***
	 * 
	 *房屋发布者取消直播预约 
	 * @throws ParseException 
	 **/
	@RequestMapping("/cancelSubscription")
	@ResponseBody
	public Object cancelUserSubcHouseLive(HttpServletRequest request) throws ParseException{

//      测试代码		
//		AuthInfo auth = new AuthInfo("" , "623950d6-974b-4c51-a69f-bf8e1ae55d03" ,1);		
//		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date subBeginTime = df1.parse("2016-05-02 19:30:00");
		
		String time = request.getParameter("subBeginTime");
		logger.info("subBeginTime is {}" , time);
		if(RetMapUtils.affirmNull(time)){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		Date subBeginTime = new Date(Long.parseLong(time));
		
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}

		Map<String, Object>result=new HashMap<>();
		UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
		record.setPubUserId(auth.getUserId());
		record.setSubBeginTime(subBeginTime);
		record.setVerifyFlag(1);
				
		List<UserSubcHouseLiveForm> list = agencyscheduleservice.getSubInfoByliveUserAndTime(record);

		List<Map<String, Object>> subList = new ArrayList<>();

		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size(); i++){
				Map<String , Object> subResult = new HashMap<>();
				subResult.put("liveSubInfoId" ,list.get(i).getLiveSubInfoId() );
				subResult.put("houseId" , list.get(i).getHouseId());
				subResult.put("pubUserId" , list.get(i).getPubUserId());
				subResult.put("subUserId" , list.get(i).getSubUserId());
				subResult.put("subBeginTime" , list.get(i).getSubBeginTime());
//				subResult.put("subEndTime" , list.get(i).getSubEndTime());
				subResult.put("verifyFlag" , 2);
				subList.add(subResult);
			}
		}else{
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}
		
		result.put("code", 8200);
		result.put("data", agencyscheduleservice.cancelorRejectHouseLive(record));
		pushService.pubUser2SubUser(subList);
		return result;
	}
	
	
	
	/***
	 * 
	 *房屋发布者拒绝直播预约 
	 * @throws ParseException 
	 **/
	@RequestMapping("/rejectSubscription")
	@ResponseBody
	public Object rejectUserSubcHouseLive(HttpServletRequest request) throws ParseException{

		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}

		String time = request.getParameter("subBeginTime");
		logger.info("subBeginTime is {}" , time);
		if(RetMapUtils.affirmNull(time)){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		
		Date subBeginTime = new Date(Long.parseLong(time));
		
		Map<String, Object>result=new HashMap<>();
		
		UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
		record.setPubUserId(auth.getUserId());
		record.setSubBeginTime(subBeginTime);
		record.setVerifyFlag(0);
		
		List<UserSubcHouseLiveForm> list = agencyscheduleservice.getSubInfoByliveUserAndTime(record);
		List subList = new ArrayList();

		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size(); i++){
				Map<String , Object> subResult = new HashMap<>();
				subResult.put("liveSubInfoId" ,list.get(i).getLiveSubInfoId() );
				subResult.put("houseId" , list.get(i).getHouseId());
				subResult.put("pubUserId" , list.get(i).getPubUserId());
				subResult.put("subUserId" , list.get(i).getSubUserId());
				subResult.put("subBeginTime" , list.get(i).getSubBeginTime());
//              subResult.put("subEndTime" , list.get(i).getSubEndTime());
				subResult.put("verifyFlag" , 2);
				subList.add(subResult);
			}
		}else{
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}
		result.put("code", 8200);
		result.put("data", agencyscheduleservice.cancelorRejectHouseLive(record));
		
		pushService.pubUser2SubUser(subList);

		return result;
	}
	
	/***
	 * 
	 *房源发布者确认直播预约 
	 * @throws ParseException 
	 **/
	@RequestMapping("/verifySubscription")
	@ResponseBody
	public Object verifyUserSubcHouseLive(HttpServletRequest request) throws ParseException{

//      测试代码
//		AuthInfo auth = new AuthInfo("" , "920bd6e0-efd9-481d-b0cb-26cfd560908e" ,1);		
//		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//     	Date subBeginTime = df1.parse("2016-05-02 10:30:00");
		
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		String time = request.getParameter("subBeginTime");
		logger.info("subBeginTime is {}" , time);
		if(RetMapUtils.affirmNull(time)){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		/**
		 *未校验时间参数 
		 **/
		Date subBeginTime = new Date(Long.parseLong(time));
		
		Map<String, Object>result=new HashMap<>();
		UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
		record.setPubUserId(auth.getUserId());
		record.setSubBeginTime(subBeginTime);
		record.setVerifyFlag(0);
		result.put("code", 8200);
		List<UserSubcHouseLiveForm> list = agencyscheduleservice.getSubInfoByliveUserAndTime(record);
		List subList = new ArrayList();

		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size(); i++){
				Map<String , Object> subResult = new HashMap<>();
				subResult.put("liveSubInfoId" ,list.get(i).getLiveSubInfoId() );
				subResult.put("houseId" , list.get(i).getHouseId());
				subResult.put("pubUserId" , list.get(i).getPubUserId());
				subResult.put("subUserId" , list.get(i).getSubUserId());
				subResult.put("subBeginTime" , list.get(i).getSubBeginTime());
//			    subResult.put("subEndTime" , list.get(i).getSubEndTime());
				subResult.put("verifyFlag" , 1);
				subList.add(subResult);
			}
		}else{
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}
		
		result.put("data", agencyscheduleservice.verifyAgencySubcHouseLive(record));
		
		pushService.pubUser2SubUser(subList);

		return result;
	}
	
	
	
	/****
	 * 查询房源发布者等待确认的直播预约信息
	 *@param userId 房源发布者id
	 *@param pageIndex 查询的分页号 
	 **/	
	@RequestMapping(value="waitList")
	@ResponseBody
	public Object getScheduleWaitList(HttpServletRequest request){
		Map<String, Object> result = null;	

		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		
		result=new HashMap<>();
		Map<String, Object> dataMap=new HashMap<String , Object>();
		List<AgencyScheduleVerifyForm> list = agencyscheduleservice.getAgencyWaitScheduleVerifyByUserId(auth.getUserId());
		if(list == null || list.isEmpty()){
			logger.error("getAgencyWaitScheduleVerifyByUserId failed");
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}
		List<subAgency> list1 = agencyscheduleservice.getAgencyWaitScheduleUser(auth.getUserId());
		if(list1 == null || list1.isEmpty()){
			logger.error("getAgencyWaitScheduleUser failed");
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}
		for(subAgency subList1 : list1){
			List<UserInfo> userList = new ArrayList<UserInfo>();
			for(AgencyScheduleVerifyForm subList2 : list){
				if(subList1.getHouseId().equals(subList2.getHouseId()) && subList1.getSubBeginTime().equals(subList2.getSubBeginTime())){
					UserInfo userinfo = new UserInfo();
					userinfo.setUserId(subList2.getSubUserId());
					userinfo.setUserName(subList2.getSubUserName());
					userinfo.setPhoneNumber(subList2.getSubPhoneNumber());
					userinfo.setPortraitFileName(subList2.getPortraitFileName());
					userList.add(userinfo);
				}
			}
			subList1.setUserlist(userList);
			subList1.setHousePic(houseService.getPicListByHouseId(subList1.getHouseId()));
			if(!RetMapUtils.affirmNull(subList1.getHousePic()) && !subList1.getHousePic().isEmpty()){
				subList1.setHouseShowPic(subList1.getHousePic().get(0));				
			}
			else{
				subList1.setHouseShowPic("./image/default.jpg");				
			}
		}
		
		
		dataMap.put("vals", list1);
		result.put("code", 8200);
		result.put("data", dataMap);		
		return result;
	}
	
	//中介端直播排期
	@RequestMapping("houseLiveTimeList")
	@ResponseBody
	public Object houseLiveVerifyTimeList(HttpServletRequest request) throws ParseException{

		Map<String , Object> result = new HashMap<String , Object>();	
		int intervaltime = -1;
		String houseId ; 
		Map<String , Object> dataMap = new HashMap<String , Object>();	
		
	    AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}

		if(RetMapUtils.affirmNull(request.getParameter("intervaltime"))){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		
		intervaltime = Integer.parseInt(request.getParameter("intervaltime"));
		houseId = request.getParameter("houseId");
		
		if(intervaltime<0||intervaltime>6){
			return RetMapUtils.createRetMap(8400, "请求参数不正确");
		}

		List<UserSubcHouseLiveForm> list = agencyscheduleservice.getOwnSubcHouseLiveList(houseId , intervaltime);
		if(list == null || list.size() < 1){
			return RetMapUtils.createRetMap(8404, "查询记录不存在 ");
		}
		Set<Date> set = new HashSet<Date>();
		for(UserSubcHouseLiveForm subList : list){
			set.add(subList.getSubBeginTime());
		}
		String virtualUserId="00000000-0000-0000-0000-000000000000";
		List<Map<String, Object>> subList = new ArrayList<>();
		for(Date subSet : set){
			int i = 1;		
			Map<String , Object> subResult = new HashMap<>();
			for(UserSubcHouseLiveForm subList1 : list){
				if(subSet.equals(subList1.getSubBeginTime())){
					subResult.put("subBeginTime" , subSet);
					subResult.put("count" , i++);
					subList.add(subResult);
				}
			}
			logger.info("subBeginTime is {} and count is {}" ,subSet , i);			
		}
		List<UserSubcHouseLiveForm> timeList=agencyscheduleservice.getAgencySelfScheduleByUserId(auth.getUserId(), intervaltime);
		//List<Date>selfDate=new ArrayList<>();
		List<Date>otherDate=new ArrayList<>();
		//String virtualUserId="00000000-0000-0000-0000-000000000000";
		if(timeList!=null&&timeList.size()>0)
		for(UserSubcHouseLiveForm record:timeList){
			/*if(virtualUserId.equalsIgnoreCase(record.getSubUserId())){
				selfDate.add(record.getSubBeginTime());
			}else{
				otherDate.add(record.getSubBeginTime());
			}*/
			if(!houseId.equalsIgnoreCase(record.getHouseId())&&!virtualUserId.equalsIgnoreCase(record.getSubUserId())){
				otherDate.add(record.getSubBeginTime());
			}
		}
		dataMap.put("vals",subList);
		dataMap.put("oths",otherDate);
		result.put("code", 8200);
		result.put("data", dataMap);
		return result;
	}
	/**
	 *房源发布端新增一个房源直播
	 *1.当前有房客预约（将房客预约直播设置为1） 
	 *2.当前没有房客预约 
	 **/
	@RequestMapping("addNewHouseLive")
	@ResponseBody
	public Object addNewHouseLive(HttpServletRequest request){
		
		Map<String , Object> result = new HashMap<String , Object>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			String houseId= request.getParameter("houseId");
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNullString(houseId)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			String timeStamp=request.getParameter("beginTime");
			if(RetMapUtils.affirmNullString(timeStamp)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			Date beginTime=convertToDateFormate(timeStamp);
			if(RetMapUtils.expirationTime(beginTime)){
				return RetMapUtils.createRetMap(8400, "新增直播时间不正确");
			}
			/**
			 * 在插入房东的新直播前，先查看是否有该房源该时段的预约，有的话就更新状态
			 * UserSubcHouseLiveForm record
			 * pub_user_id
			 * sub_begin_time
			 * verify_flag
			 **/
			UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
			record.setLiveSubInfoId(UUID.randomUUID().toString());
			record.setPubUserId(auth.getUserId());
			record.setSubBeginTime(beginTime);
			record.setHouseId(houseId);
			record.setVerifyFlag(1);
			/**
			 *查看表中是否已存在预约记录 
			 ***/
			List<UserSubcHouseLiveForm>list=agencyscheduleservice.getSubInfoByliveUserAndTimeAndHouse(record);
			record.setVerifyFlag(0);
			if(list!=null&&list.size()>0){
				return RetMapUtils.createRetMap(8405, "非法请求");
				//查看表中是否有未确认的直播预约记录
			}else if(agencyscheduleservice.verifyAgencySubcHouseLive(record)){
				result.put("data", true);
			}else{
				record.setVerifyFlag(1);
				record.setSubUserId("00000000-0000-0000-0000-000000000000");
				//插入一条虚拟的预约记录
				int num=agencyscheduleservice.addNewHouseLiveByAgency(record);
				result.put("data", (num>0)?true:false);
			}
		}catch(Exception ex){
			if(ex instanceof NumberFormatException){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			return RetMapUtils.createRetMap(8888, ex.getMessage());
		}
		result.put("code", 8200);
		return result;
	}
	
	
	private static int getIntervalTime(Date now,Date dd){
    	int dayTimeMills=24*3600*1000;
    	long nowTimeMills=now.getTime();
    	long ddTimeMills=dd.getTime();
    	nowTimeMills-=nowTimeMills%dayTimeMills;
    	ddTimeMills-=ddTimeMills%dayTimeMills;
    	if(nowTimeMills>ddTimeMills){
    		return -1;
    	}
    	return (int)((ddTimeMills-nowTimeMills)/dayTimeMills);
    }
	private static Date convertToDateFormate(String timeStamp){
		long stamp=Long.parseLong(timeStamp);
		return new Date(stamp);
	}
}

