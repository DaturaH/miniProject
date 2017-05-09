package com.miniProject.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.miniProject.model.UserRecordForm;
import com.miniProject.model.UserScheduleVerifyForm;
import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.service.HouseServiceI;
import com.miniProject.service.PushServiceI;
import com.miniProject.service.UserScheduleServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.RetMapUtils;

@Controller
@RequestMapping("/userSchedule")
public class UserScheduleController {
	@Resource
	private UserScheduleServiceI userScheduleService;
	
	@Resource
	private HouseServiceI houseService;
	
	@Resource
	private PushServiceI pushService;
	////////////////////////////////////////////////////租客端获取直播日程//////////////////////////////////////////////////////////
	/****
	 *查询当前用户被确认的直播预约信息
	 *@param userId 用户id
	 *@param pageIndex 查询的分页号 
	 **/
	@RequestMapping("/verifyList")
	@ResponseBody
	public Object getScheduleVerifyList(HttpServletRequest request){
		Map<String, Object>retValue=null;
		int pageIndex=1;
		int countIndex=0;
		int total=-1;
		/**
		 *mysql中从0开始计算  limit pageIndex,pageNum
		 **/
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		if(!RetMapUtils.affirmNullString(request.getParameter("pageIndex"))){
			pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		}else{
			pageIndex=-1;
		}
		if(!RetMapUtils.affirmNullString(request.getParameter("countIndex"))){
			countIndex=Integer.parseInt(request.getParameter("countIndex"));
		}else{
			countIndex=-1;
		}
		total=userScheduleService.getUserScheduleVerifyCountByUserId(auth.getUserId());
		if(pageIndex>-1)
			retValue=RetMapUtils.affirmIllageDigit((pageIndex-1)*10, 0, total-1, "请求参数pageIndex超出范围", 8430);
		if(countIndex>-1)
			retValue=RetMapUtils.affirmIllageDigit(countIndex, 0, total-1, "请求参数countIndex超出范围", 8430);
		if(retValue!=null)return retValue;
		retValue=new HashMap<>();
		Map<String, Object>dataMap=new HashMap<>();
		List<UserScheduleVerifyForm>records=null;
		if(pageIndex>-1){
			//按页码索引
			records=userScheduleService.getUserScheduleVerifyByUserId(auth.getUserId(),(pageIndex-1)*10);
		}else if(countIndex>-1){
			//按条数索引
			records=userScheduleService.getUserScheduleVerifyByUserId(auth.getUserId(),countIndex);
		}else{
			//记录全查
			records=userScheduleService.getUserAllScheduleVerifyByUserId(auth.getUserId());
		}
		if(records!=null&&records.size()>0)
			for(UserScheduleVerifyForm record:records){
				record.setHousePic(houseService.getPicListByHouseId(record.getHouseId()));
				if(!RetMapUtils.affirmNull(record.getHousePic())&&record.getHousePic().size()>0)
					record.setHouseShowPic(record.getHousePic().get(0));
				else
					record.setHouseShowPic("./image/default.jpg");
			}
		dataMap.put("list",records);
		dataMap.put("total",total);
		retValue.put("code", 8200);
		retValue.put("data", dataMap);
		return retValue;
	}
	
	
	/**
	 *查询当前用户未被确认的直播预约信息
	 *@param userId 用户id(后端获取)
	 *@param pageIndex 查询的分页号 
	 **/
	@RequestMapping("/toBeVerifiedList")
	@ResponseBody
	public Object getScheduleToBeConfirmedList(HttpServletRequest request){
		Map<String, Object>retValue=null;
		int countIndex=0;
		int pageIndex=1;
		int total=-1;
		/***
		 *mysql中从0开始计算  limit pageIndex,pageNum
		 **/
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		if(!RetMapUtils.affirmNullString(request.getParameter("pageIndex"))){
			pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		}else{
			pageIndex=-1;
		}
		if(!RetMapUtils.affirmNullString(request.getParameter("countIndex"))){
			countIndex=Integer.parseInt(request.getParameter("countIndex"));
		}else{
			countIndex=-1;
		}
		//*****
		total=userScheduleService.getUserScheduleToBeVerifiedCountByUserId(auth.getUserId());
		if(pageIndex>-1)
			retValue=RetMapUtils.affirmIllageDigit((pageIndex-1)*10, 0, total-1, "请求参数pageIndex超出范围", 8430);
		if(countIndex>-1)
			retValue=RetMapUtils.affirmIllageDigit(countIndex, 0, total-1, "请求参数countIndex超出范围", 8430);
		//retValue=RetMapUtils.affirmIllageDigit((pageIndex-1)*10, 0, total, "请求参数pageIndex超出范围", 8430);
		if(retValue!=null)return retValue;
		retValue=new HashMap<>();
		Map<String, Object>dataMap=new HashMap<>();
		
		List<UserScheduleVerifyForm>records=null;
		if(pageIndex>-1){
			//按页码索引
			records=userScheduleService.getUserScheduleToBeVerifiedByUserId(auth.getUserId(),(pageIndex-1)*10);
		}else if(countIndex>-1){
			//按条数索引
			records=userScheduleService.getUserScheduleToBeVerifiedByUserId(auth.getUserId(),countIndex);
		}else{
			records=userScheduleService.getUserAllScheduleToBeVerifiedByUserId(auth.getUserId());
		}
		if(records!=null&&records.size()>0)
			for(UserScheduleVerifyForm record:records){
				record.setHousePic(houseService.getPicListByHouseId(record.getHouseId()));
				if(!RetMapUtils.affirmNull(record.getHousePic())&&record.getHousePic().size()>0)
					record.setHouseShowPic(record.getHousePic().get(0));
				else
					record.setHouseShowPic("./image/default.jpg");
			}
		dataMap.put("list",records);
		if(total>-1){
			dataMap.put("total",total);
		}
		retValue.put("code", 8200);
		retValue.put("data", dataMap);
		return retValue;
	}
	/***
	 * 
	 *用户取消自己的直播预约 
	 *包括 预约未确认的以及预约已确认的
	 *@param liveSubInfoId String 用于预约直播信息id
	 **/
	@RequestMapping("/cancelSubscription")
	@ResponseBody
	public Object cancelUserSubcHouseLive(HttpServletRequest request){
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		String liveSubInfoId=request.getParameter("liveSubInfoId");
		if(RetMapUtils.affirmNullString(liveSubInfoId)){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
		record.setLiveSubInfoId(liveSubInfoId);
		record.setSubUserId(auth.getUserId());
		record.setVerifyFlag(3);
		record.setSubEndTime(new Date());
		Map<String, Object>retValue=new HashMap<>();
		retValue.put("code", 8200);
		UserSubcHouseLiveForm rd=userScheduleService.selectByPrimaryKey(liveSubInfoId);
		boolean retSql=userScheduleService.cancelOrRefuseUserSubcHouseLive(record);
		if(retSql&&rd.getVerifyFlag()==1){
			int intervalTime=getIntervalTime(new Date(),rd.getSubBeginTime());
			int count=userScheduleService.selectHouseDailyLiveCountByHouseId(rd.getHouseId(), intervalTime);
			if(count==0){
				Map<String,Object>pushMap=new HashMap<>();
				/**
				 *liveSubInfoId
				 * houseId
				 * pubUserId
				 * subUserId
				 * subBeginTime
				 * subEndTime
				 * verifyFlag
				 **/
				pushMap.put("houseId", rd.getHouseId());
				pushMap.put("pubUserId", rd.getPubUserId());
				pushMap.put("subUserId", rd.getSubUserId());
				pushMap.put("subBeginTime", rd.getSubBeginTime());
				pushMap.put("verifyFlag", 3);
				pushService.subUser2PubUser(pushMap);
			}
		}
		retValue.put("data", retSql);
		return retValue;
	}
	
	/**
	 *从视图(RecdHistory,SubcHistory)中获取用户观看记录以及预约记录 
	 * 
	 ***/
	@RequestMapping("/userRecord")
	@ResponseBody
	public Object getUserHistoryReocord(HttpServletRequest request){
		AuthInfo auth=AuthInfo.get(request);
		int countIndex=0;
		int pageIndex=1;
		int total=-1;
		Map<String, Object>retValue=null;
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		if(!RetMapUtils.affirmNullString(request.getParameter("pageIndex"))){
			pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		}else{
			pageIndex=-1;
		}
		if(!RetMapUtils.affirmNullString(request.getParameter("countIndex"))){
			countIndex=Integer.parseInt(request.getParameter("countIndex"));
		}else{
			countIndex=-1;
		}
		total=userScheduleService.getUserRecordCountByUserId(auth.getUserId());
		if(pageIndex>-1)
			retValue=RetMapUtils.affirmIllageDigit((pageIndex-1)*10, 0, total-1, "请求参数pageIndex超出范围", 8430);
		if(countIndex>-1)
			retValue=RetMapUtils.affirmIllageDigit(countIndex, 0, total-1, "请求参数countIndex超出范围", 8430);
		//retValue=RetMapUtils.affirmIllageDigit((pageIndex-1)*10, 0, total, "请求参数pageIndex超出范围", 8430);
		if(retValue!=null)return retValue;
		retValue=new HashMap<>();
		Map<String, Object>dataMap=new HashMap<>();
		List<UserRecordForm> records=null;
		if(pageIndex>-1){
			//按页码索引
			records=userScheduleService.getUserRecordByUserId(auth.getUserId(),(pageIndex-1)*10);
		}else if(countIndex>-1){
			//按条数索引
			records=userScheduleService.getUserRecordByUserId(auth.getUserId(),countIndex);
		}else{
			//if(countIndex==-1&&pageIndex==-1)//记录全查
			records=userScheduleService.getUserAllRecordByUserId(auth.getUserId());
		} 
		if(records!=null&&records.size()>0)
		for(UserRecordForm record:records){
			record.setHousePic(houseService.getPicListByHouseId(record.getRecordHouseId()));
			if(!RetMapUtils.affirmNull(record.getHousePic())&&record.getHousePic().size()>0)
				record.setHouseShowPic(record.getHousePic().get(0));
			else
				record.setHouseShowPic("./image/default.jpg");
		}
		dataMap.put("list", records);
		if(total>-1){
			dataMap.put("total",total);
		}
		retValue.put("code", 8200);
		retValue.put("data", dataMap);
		return retValue;	
	}
	
	/***
	 * 清除用户已取消或者是被拒绝的预约消息
	 * 还包括一些直播记录
	 * */
	@RequestMapping("/clearUserRecord")
	@ResponseBody
	public Object clearUserSubcHouseLive(HttpServletRequest request){
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		String recordId=request.getParameter("recordId");
		if(RetMapUtils.affirmNullString(recordId)){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		Map<String, Object>retValue=new HashMap<>();
		retValue.put("code", 8200);
		retValue.put("data", userScheduleService.deleteUserRecordById(auth.getUserId(), recordId));
		return retValue;
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
}
