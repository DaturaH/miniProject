package com.miniProject.controller;


import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.miniProject.dao.CommunityInfoMapper;
import com.miniProject.dao.HouseWantedInfoMapper;
import com.miniProject.dao.UserHouseFormMapper;
import com.miniProject.dao.UserHousePicFormMapper;
import com.miniProject.dao.UserInfoMapper;
import com.miniProject.model.BSUserSubcHouseLive;
import com.miniProject.model.CommunityInfo;
import com.miniProject.model.HouseDetailForm;

import com.miniProject.model.HouseWantedInfo;

import org.apache.log4j.Logger;
import org.objectweb.asm.commons.Remapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.miniProject.model.UserFavorHouseForm;
import com.miniProject.model.UserHouseForm;

import com.miniProject.model.UserHousePicForm;

import com.miniProject.model.UserInfo;

import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.service.AgencyScheduleServiceI;
import com.miniProject.service.HouseServiceI;
import com.miniProject.service.PushServiceI;
import com.miniProject.util.AuthInfo;
import com.miniProject.util.Config;
import com.miniProject.util.ExceptionResolver;
import com.miniProject.util.RetMapUtils;
import com.sun.org.apache.bcel.internal.generic.LLOAD;

/***
 *@author hzchendou
 *做标记，后期需要完善
 *
 ***/
@Controller
@RequestMapping("/house")
public class HouseController {
	
	private static Logger logger = Logger.getLogger(HouseController.class);

	@Resource
	private HouseServiceI houseService;

	@Autowired
	private UserInfoMapper userInfoMapper;
	

	@Resource
	private PushServiceI pushService;

	@Autowired
    private ServletContext context;
	
	@Autowired
	private UserHousePicFormMapper userHousePicFormMapper;
	
	
	@Autowired
	private UserHouseFormMapper houseMapper;
	
	@Autowired
	private AgencyScheduleServiceI agencyScheduleService;
	
	@Autowired
	private MongoOperations mongoOps;
	
	
	public UserHouseForm baseInfo(String houseid){
		return houseService.getByHouseId(houseid);
	}
	/***
	 *实际返回信息是以UserHousePicForm为单位的
	 *
	 **/

	public List<String> picList(String houseId){
		return houseService.getPicListByHouseId(houseId);
	}
	/***
	 * 依据userid和houseid判断是否收藏该房源
	 **/

	public boolean isFavorHouse(String userid,String houseid){
		UserFavorHouseForm userFavorHouseForm=houseService.getFavorHouseByid(houseid, userid);
		return (userFavorHouseForm!=null);
	}
	/***
	 *距离当前最近一次直播时间 (未直播)
	 **/
	public Date getClosetHouseLiveBeginTime(String houseid){
		return houseService.getVerifyBeginTimeByHouseId(houseid);
	}



	/////////////////////////////////////////租客查看房源信息使用////////////////////////////////////////////////////////////

	/***
	 * 依据userid和houseid收藏该房源
	 * @param userid 用户id (后台获取)
	 * @param houseid 房源信息id
	 **/
	@RequestMapping("/setFavor")
	@ResponseBody
	public Object setFavorHouse(@RequestBody final HouseInfo houseInfo,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(houseInfo)||RetMapUtils.affirmNullString(houseInfo.houseid)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			retMap.put("code", 8200);
			if(isFavorHouse(auth.getUserId(), houseInfo.houseid)){
				return RetMapUtils.createRetMap(8405, "非法请求");
			}
			UserFavorHouseForm userFavorHouseForm=new UserFavorHouseForm();
			userFavorHouseForm.setHouseId(houseInfo.houseid);
			userFavorHouseForm.setUserId(auth.getUserId());
			userFavorHouseForm.setInfoId(UUID.randomUUID().toString());
			retMap.put("date", houseService.insertFavorHouseInfo(userFavorHouseForm));
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
	}
	/***
	 *依据userid和houseid取消收藏
	 *@param houseid 房源信息id
	 **/
	@RequestMapping("/cancelFavor")
	@ResponseBody
	public Object cancelFavorHouse(@RequestBody final HouseInfo houseInfo,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(houseInfo)||RetMapUtils.affirmNullString(houseInfo.houseid)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			retMap.put("code", 8200);
			retMap.put("data", houseService.deleteFavorHouseInfoById(houseInfo.houseid, auth.getUserId()));
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
	}
	/***
	 *依据userid和houseid判断是否收藏该房源
	 **/
	@RequestMapping("/isFavorHouse")
	@ResponseBody
	public Object isFavorHouse(@RequestBody final HouseInfo houseInfo,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		if(RetMapUtils.affirmNull(houseInfo)||RetMapUtils.affirmNullString(houseInfo.houseid)){
			return RetMapUtils.createRetMap(8401, "缺少请求参数");
		}
		UserFavorHouseForm userFavorHouseForm=houseService.getFavorHouseByid(houseInfo.houseid, auth.getUserId());
		retMap.put("code", 8200);
		retMap.put("data", userFavorHouseForm!=null);
		return retMap;
	}
	/***
	 *房源详情信息，包括房源的基本信息，用户的收藏信息，房源的直播时间
	 *@param houseid String 房源信息id
	 **/
	@RequestMapping("/detail")
	@ResponseBody
	public Object details(@RequestBody final HouseInfo houseInfo,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();

		
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(houseInfo)||RetMapUtils.affirmNullString(houseInfo.houseid)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			
			logger.info("#########   houseID " + houseInfo.houseid);
			logger.info("#########   userID " + auth.getUserId());
			
			HouseDetailForm houseDetailForm=new HouseDetailForm();
			houseDetailForm.setHouseForm(baseInfo(houseInfo.houseid));
			if(!RetMapUtils.affirmNull(houseDetailForm.getHouseForm())){
				houseDetailForm.setPicList(picList(houseInfo.houseid));
				houseDetailForm.setFavorHouse(isFavorHouse(auth.getUserId(),houseInfo.houseid));
				houseDetailForm.setBeginTime(getClosetHouseLiveBeginTime(houseInfo.houseid));
				UserInfo userInfo=userInfoMapper.selectByPrimaryKey(houseDetailForm.getHouseForm().getUserId());
				houseDetailForm.setUserPic(userInfo.getPortraitFileName());
				houseDetailForm.setType(userInfo.getUserType());
			}
			retMap.put("code", 8200);
			retMap.put("data", houseDetailForm);
			logger.info("houseId:"+houseDetailForm.getHouseForm().getHouseId());
			logger.info("userId:"+houseDetailForm.getHouseForm().getUserId());
			logger.info("price:"+houseDetailForm.getHouseForm().getPrice());
		}catch(Exception ex){
			retMap=ExceptionResolver.catchExceptionToRet(ex);
			if(retMap!=null)return retMap;
				ex.printStackTrace();
			return RetMapUtils.createRetMap(8888, ex.getMessage());
		}
		
		return retMap;
	}
	/**
	 *设置房源直播中，标记位
	 *@param houseId 
	 **/
	@RequestMapping("/setUpHouseLive")
	public Object sethouseLiving(HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		String houseId;
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		if(auth.getUserType()==0)
			return RetMapUtils.createRetMap(8402, "没有权限");
		houseId=request.getParameter("houseId");
		if(RetMapUtils.affirmNullString(houseId)||"undefined".equalsIgnoreCase(houseId)){
			return RetMapUtils.createRetMap(8400, "请求参数不正确");
		}
		retMap.put("code", 8200);
		retMap.put("data", houseService.setHouseLiving(auth.getUserId(), houseId));
		return retMap;
	}
	/**
	 *设置房源直播中，标记位
	 *@param houseId 
	 **/
	@RequestMapping("/cancelHouseLive")
	public Object cancelHouseLiving(HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		String houseId;
		AuthInfo auth=AuthInfo.get(request);
		if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
			return RetMapUtils.createRetMap(8402, "请登录");
		}
		if(auth.getUserType()==0)
			return RetMapUtils.createRetMap(8402, "没有权限");
		houseId=request.getParameter("houseId");
		if(RetMapUtils.affirmNullString(houseId)||"undefined".equalsIgnoreCase(houseId)){
			return RetMapUtils.createRetMap(8400, "请求参数不正确");
		}
		retMap.put("code", 8200);
		retMap.put("data", houseService.cancelHouseLiving(auth.getUserId(), houseId));
		return retMap;
	}
	
	/***
	 *用户直播排期表
	 *@param houseId 房源信息id
	 *@param intervalTime 间隔时间 
	 *间隔时间
	 *0表示今天
	 *1表示明天
	 *.....
	 *以此类推 
	 **/
	@RequestMapping("/houseLiveTimeList")
	@ResponseBody
	public Object houseLiveVerifyTimeList(HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		String houseId;
		int intervalTime=0;
		try{
			houseId=request.getParameter("houseId");
			String pm=request.getParameter("intervalTime");
			if(RetMapUtils.affirmNullString(pm))
				intervalTime=0;
			else
				intervalTime=Integer.parseInt(pm);
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNullString(houseId)||"undefined".equalsIgnoreCase(houseId)){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			if(intervalTime<0||intervalTime>6){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			BSUserSubcHouseLive userSubcHouseLiveState =new BSUserSubcHouseLive();

			
			userSubcHouseLiveState.setMeVerifyTime(toDateList(houseService.getOwnVerifyTimeListById(houseId, auth.getUserId(),intervalTime)));
			
			userSubcHouseLiveState.setMeSubcTime(toDateList(houseService.getOwnSubcHouseLiveListById(houseId, auth.getUserId(),intervalTime)));
			
			userSubcHouseLiveState.setOtherVerifyTime(toDateList(houseService.getOtherVerifyTimeListById(houseId, auth.getUserId(),intervalTime)));
			
			userSubcHouseLiveState.setLandlordRefuseTime(houseService.getLandlordVerifyTimeListById(houseId, intervalTime));
			
			userSubcHouseLiveState.setUserRefuseTime(houseService.getOwnOtherVerifyTimeListById(houseId, auth.getUserId(), intervalTime));
			retMap.put("code", 8200);
			retMap.put("data", userSubcHouseLiveState);
		}catch(Exception e){
			if(e instanceof NumberFormatException){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
	}

	/***
	 *订阅房源直播
	 *@problems:
	 *record中的pub_user_id 和house_id 以及 sub_user_id的正确性没有进行校验
	 *@resolve
	 *后期将各个表进行关联，防止无效数据插入
	 **/
	@RequestMapping("/subHouseLive")
	@ResponseBody
	public Object subcHouseLive(@RequestBody final SubcHouseInfo subcHouseInfo,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(subcHouseInfo)||RetMapUtils.affirmNullString(subcHouseInfo.houseId)||RetMapUtils.affirmNull(subcHouseInfo.beginTime)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			//数据有效性校验
			/***
			 * house_id
			 **/
			UserHouseForm houseForm=houseService.getByHouseId(subcHouseInfo.houseId);
			if(RetMapUtils.affirmNull(houseForm)||RetMapUtils.affirmNullString(houseForm.getUserId())){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			/***
			 *校验时间正确性 
			 **/
			if(RetMapUtils.expirationTime(subcHouseInfo.beginTime)){
				return RetMapUtils.createRetMap(8400, "预约时间不正确");
			}
			int intervalTime=getIntervalTime(new Date(),subcHouseInfo.beginTime);
			if(intervalTime<0||intervalTime>6){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			/**
			 *同一时间段的不可预约多次，这一点没有验证 
			 **/
			int count=houseService.getUserDailyInfoCount(subcHouseInfo.houseId, auth.getUserId(), intervalTime);
			if(count >2){
				return RetMapUtils.createRetMap(8700, "预约次数超过上限（3次）");
			}
			UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
			record.setLiveSubInfoId(UUID.randomUUID().toString());
			record.setSubUserId(auth.getUserId());
			record.setPubUserId(houseForm.getUserId());
			record.setHouseId(subcHouseInfo.houseId);
			record.setSubBeginTime(subcHouseInfo.beginTime);
			record.setVerifyFlag(0);
			retMap.put("code", 8200);
			boolean retSql=houseService.insertSubcHouseLiveInfo(record);
			if(retSql)
			{
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
				pushMap.put("houseId", subcHouseInfo.houseId);
				pushMap.put("pubUserId", houseForm.getUserId());
				pushMap.put("subUserId", auth.getUserId());
				pushMap.put("subBeginTime", subcHouseInfo.beginTime);
				pushMap.put("verifyFlag", 0);
				pushService.subUser2PubUser(pushMap);
			}
			retMap.put("data",retSql );
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
	}
	
	
	/***
	 *用户加入房源直播预约 
	 **/
	@RequestMapping("/joinHouseLive")
	@ResponseBody
	public Object joinOtherHouseLive(@RequestBody final SubcHouseInfo subcHouseInfo,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(subcHouseInfo)||RetMapUtils.affirmNullString(subcHouseInfo.houseId)||RetMapUtils.affirmNull(subcHouseInfo.beginTime)){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			if(RetMapUtils.expirationTime(subcHouseInfo.beginTime)){
				return RetMapUtils.createRetMap(8400, "加入直播时间不正确");
			}
			//数据有效性校验
			/***
			 * house_id
			 **/
			UserHouseForm houseForm=houseService.getByHouseId(subcHouseInfo.houseId);
			if(RetMapUtils.affirmNull(houseForm)||RetMapUtils.affirmNullString(houseForm.getUserId())){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			/***
			 *校验时间正确性 
			 **/
			int intervalTime=getIntervalTime(new Date(),subcHouseInfo.beginTime);
			if(intervalTime<0||intervalTime>6){
				return RetMapUtils.createRetMap(8400, "请求参数不正确");
			}
			/*int count=houseService.getUserDailyInfoCount(subcHouseInfo.houseId, auth.getUserId(), subcHouseInfo.intervalTime);
			if(count >2){
				return RetMapUtils.createRetMap(8400, "预约次数超过上限（3次）");
			}*/
			houseService.selectHouseLiveByBeginTime(subcHouseInfo.houseId, subcHouseInfo.beginTime);
			UserSubcHouseLiveForm record=new UserSubcHouseLiveForm();
			record.setLiveSubInfoId(UUID.randomUUID().toString());
			record.setSubUserId(auth.getUserId());
			record.setPubUserId(houseForm.getUserId());
			record.setHouseId(subcHouseInfo.houseId);
			record.setSubBeginTime(subcHouseInfo.beginTime);
			record.setVerifyFlag(1);
			retMap.put("code", 8200);
			retMap.put("data", houseService.insertSubcHouseLiveInfo(record));
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
	}
	
	
	
	
	/***
	 *用户取消直播预约
	 *需要校验字段：
	 *String userid//后台获取
	 *String houseid//前台发送
	 *String liveSubInfoId//前台发送
	 *@problems:
	 *  1.用户取消预约时是否需要进行消息推送
	 *  2.用户逆向修改verify_flag状态
	 **/
	@RequestMapping("/canelHouseLiveBySelf")
	@ResponseBody
	public Object cancelHouseLiveBySelf(UserSubcHouseLiveForm record,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(record)||RetMapUtils.affirmNullString(record.getHouseId())||RetMapUtils.affirmNull(record.getSubBeginTime())){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			if(record.getSubBeginTime().getTime()<System.currentTimeMillis()){
				return RetMapUtils.createRetMap(8400, "请求参数错误");
			}
			record.setSubUserId(auth.getUserId());
			record.setVerifyFlag(3);
			retMap.put("code", 8200);
			retMap.put("data", houseService.cancelHouseLiveBySelf(record));
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
		/*record.setVerifyFlag(3);
		return houseService.updateSubcHouseLive(record);*/
	}
	
	
	////////////////////////////////////////房源发布端使用///////////////////////////////////////////////////////////////////
	/***
	 * 更新房源信息
	 * 校验字段
	 * 全字段校验
//	@RequestMapping("/editHouseInfo")
//	@ResponseBody
//	**/
//	public Object updateHouseInfo(UserHouseForm record){
//		Map<String, Object>retMap=new HashMap<>();
//		try{
//			if(RetMapUtils.affirmNull(record)){
//				return RetMapUtils.createRetMap(8401, "缺少请求参数");
//			}
//			retMap.put("code", 8200);
//			retMap.put("data", houseService.updateHouseInfo(record));
//		}catch(Exception e){
//			return RetMapUtils.createRetMap(8888, e.getMessage());
//		}
//		return retMap;
//		//return houseService.updateHouseInfo(record);
//	}
	/***
	 * 房源发布端拒绝该时段的直播预约
	 * 需要提供
	 * houseid、(前台发送)
	 * pubUserId、(后台获取)
	 * beginTime(前台发送)
	 **/
	@RequestMapping("/refuseHouseLive")
	@ResponseBody
	public Object refuseSubcHouseLive(@RequestBody  UserSubcHouseLiveForm record,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(record)||RetMapUtils.affirmNull(record.getSubBeginTime())||RetMapUtils.affirmNullString(record.getHouseId())){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			record.setPubUserId(auth.getUserId());
			record.setVerifyFlag(2);
			retMap.put("code", 8200);
			retMap.put("data", houseService.refuseSubcHouseLive(record));
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getMessage());
		}
		return retMap;
		/*record.setVerifyFlag(2);
		return houseService.refuseSubcHouseLive(record);*/
	}
	/***
	 * 房源发布端确认该时段的直播预约
	 * 需要提供 houseid、pubUserId、beginTime
	 **/
	@RequestMapping("/verifyHouseLive")
	@ResponseBody
	public Object verifySubcHouseLive(@RequestBody UserSubcHouseLiveForm record,HttpServletRequest request){
		Map<String, Object>retMap=new HashMap<>();
		try{
			AuthInfo auth=AuthInfo.get(request);
			if(RetMapUtils.affirmNull(auth)||RetMapUtils.affirmNullString(auth.getUserId())){
				return RetMapUtils.createRetMap(8402, "请登录");
			}
			if(RetMapUtils.affirmNull(record)||RetMapUtils.affirmNull(record.getSubBeginTime())||RetMapUtils.affirmNull(record.getHouseId())){
				return RetMapUtils.createRetMap(8401, "缺少请求参数");
			}
			record.setPubUserId(auth.getUserId());
			record.setVerifyFlag(1);
			retMap.put("code", 8200);
			retMap.put("data", houseService.verifySubcHouseLive(record));
		}catch(Exception e){
			return RetMapUtils.createRetMap(8888, e.getClass().getSimpleName()+"===>"+e.getMessage());
		}
		return retMap;
	}
	
	@Autowired
	private HouseWantedInfoMapper houseWantedInfoMapper;
	
	/**
	 * 意向录入
	 */
	@RequestMapping("/houseWantedInfo")
	@ResponseBody
	public Object houseWantedInfo(HttpServletRequest request, HouseWantedInfo houseWantedInfo) {
		AuthInfo authInfo = AuthInfo.get(request);
		if (authInfo == null)
			return null;
		houseWantedInfo.setUserId(authInfo.getUserId());
		Map<String, Object> retMap = new HashMap<>();
		if (houseWantedInfoMapper.insert(houseWantedInfo) == 1)
			retMap.put("code", 8200);
		else
			retMap.put("code", 8606);
		return retMap;

	}
	
	
	

	
	
	/**
	 * 发布房源
	 */
	@RequestMapping("/postHouseInfo")
	@ResponseBody
	public Object postHouseInfo(HttpServletRequest request,@RequestBody UserHouseForm record){
		Map<String, Object> retMap = new HashMap<>();
		AuthInfo authInfo = AuthInfo.get(request);
		//权限不足
		if(authInfo == null || authInfo.getUserType() == 0) {
			retMap.put("code", 8402);
			return retMap;
		}
		String houseId = UUID.randomUUID().toString();
		record.setUserId(authInfo.getUserId());
		record.setHouseId(houseId);
		record.setHouseState(false);
		record.setLiveState(false);
		
		Map<String, Object> data = new HashMap<>();
		
		if(houseMapper.insert(record) ==1) {
			//插入mongodb
			Point point = new Point(record.getLongitude(),record.getLatitude());
			record.setPoint(point.asArray());
    		mongoOps.insert(record);
    		
			retMap.put("code", 8200); 
			data.put("houseId", houseId);
			retMap.put("data", data);
			
		} else {
			
			retMap.put("code", 8605);
		}
		return retMap;
	}
	
	@RequestMapping("/editHouseInfo")
	@ResponseBody
	public Object updateHouseInfo(HttpServletRequest request, @RequestBody UserHouseForm record){
		Map<String, Object> retMap = new HashMap<>();
		UserHouseForm userHouseForm = houseMapper.selectByPrimaryKey(record.getHouseId());
		AuthInfo authInfo = AuthInfo.get(request);
		//权限不足
		if(authInfo == null || userHouseForm == null || !userHouseForm.getUserId().equals(authInfo.getUserId())) {
			retMap.put("code", 8402);
			logger.warn("editHouseInfo permissions denied.");
			return retMap;
		}
		record.setUserId(userHouseForm.getUserId());
	
		if(houseMapper.updateByPrimaryKeySelective(record) == 1) {
			//更新mongodb
			UserHouseForm houseForm = houseMapper.selectByPrimaryKey(record.getHouseId());
			Point point = new Point(houseForm.getLongitude(),houseForm.getLatitude());
			houseForm.setPoint(point.asArray());
    		mongoOps.save(houseForm);
    		logger.info("editHouseInfo update mongo.");
			retMap.put("code", 8200);
		}
		else {
			retMap.put("code", 8608);
			return retMap;
		}
		List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
		try {
			//下线
			if(record.getHouseState() == true) {
				//取消预约
				List<UserSubcHouseLiveForm> list = agencyScheduleService.cancelSubInfoByHouseId(record.getHouseId());
				for(UserSubcHouseLiveForm subcHouseLiveForm:list) {
					Map<String , Object> subResult = new HashMap<>();
					subResult.put("liveSubInfoId" ,subcHouseLiveForm.getLiveSubInfoId() );
					subResult.put("houseId" , subcHouseLiveForm.getHouseId());
					subResult.put("pubUserId" , subcHouseLiveForm.getPubUserId());
					subResult.put("subUserId" , subcHouseLiveForm.getSubUserId());
					subResult.put("subBeginTime" , subcHouseLiveForm.getSubBeginTime());
					subResult.put("verifyFlag" , 5);
					subList.add(subResult);
				}
				//推送通知用户
				pushService.pubUser2SubUser(subList);
			}
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return retMap;
	}
	
	
	
	@RequestMapping("/getHouseInfo")
	@ResponseBody
	public Object getHouseInfo(String houseId){
		Map<String, Object> retMap = new HashMap<>();
		UserHouseForm userHouseForm = houseMapper.selectByPrimaryKey(houseId);
		
		List<String> picList = houseService.getPicListByHouseId(houseId);
		
		Map<String, Object> data = new HashMap<>();
		data.put("houseInfo", userHouseForm);
		data.put("picList", picList);
		
		retMap.put("data", data);
		retMap.put("code", 8200);
		
		return retMap;
	}
	
	//获取房源图片id及路径
	@RequestMapping("/getHousePics")
	@ResponseBody
	public Object getHousePics(@RequestBody Map<String, String> request){
		String houseId = request.get("houseId");
		Map<String, Object> retMap = new HashMap<>();

		List<UserHousePicForm> picList = userHousePicFormMapper.getPicFormsByHouseId(houseId);
		
		Map<String, Object> data = new HashMap<>();
		
		retMap.put("data", picList);
		retMap.put("code", 8200);
		
		return retMap;
	}
	
	
	
	@RequestMapping("/deleteHousePic")
	@ResponseBody
	public Object updateHouseInfo(HttpServletRequest request, @RequestBody Map<String, String> req){
		String picId = req.get("picId");
		
		AuthInfo authInfo = AuthInfo.get(request);
		Map<String, Object> retMap = new HashMap<>();

		if(picId == null || picId.equals("")) {
			retMap.put("code", 8401);
			retMap.put("msg", "empty picId");
			return retMap;
		}
		UserHousePicForm userHousePicForm =  userHousePicFormMapper.selectByPrimaryKey(picId);

		if(userHousePicForm == null) {
			retMap.put("code", 8402);
			return retMap;
		}
		
		UserHouseForm userHouseForm = houseMapper.selectByPrimaryKey(userHousePicForm.getHouseId());
		//权限不足
		if(authInfo == null || !userHouseForm.getUserId().equals(authInfo.getUserId())) {
			retMap.put("code", 8402);
			return retMap;
		}
		
		int row = userHousePicFormMapper.deleteByPrimaryKey(picId);
		if( row == 1) {
			if(userHousePicForm.getPictureName().startsWith("/static")) {
				String path = Config.UPLOAD_PATH + "housePic";
				File targetFile = new File(path,  picId+".jpg");
				targetFile.delete();
			}
			
			retMap.put("code", 8200);
		}
		else {
			retMap.put("code", 8444);
		}
		
		return retMap;
	}
	

	/**
	 * 定位小区区域
	 */
	@RequestMapping("/locating")
	@ResponseBody
	public Map<String, Object> locating(String name)  {
		
		Map<String, Object> res = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		res.put("code", 8200);
		res.put("data", data);
		CommunityInfo communityInfo = houseService.locating(name);
		if(communityInfo == null) {
			res.put("code", 8608);
			res.remove("data");
		} else {
			
			Map<String, Object> info = houseService.getLocation(name);
			if(info != null) {
				data.put("blockname", info.get("blockname"));
				data.put("location",  info.get("areaname"));
			}
			else {
				res.put("code", 8608);
				res.remove("data");
				return res;
			}
			
			data.put("longitude", communityInfo.getLng());
			data.put("latitude",communityInfo.getLat());
		}
		
		return res;
	}


	/**
	 * 获取名字匹配的小区列表
	 */
	@RequestMapping("/match")
	@ResponseBody
	public Object houseMatch(String name)  {
		String part = name;
		Map<String, Object> res = new HashMap<>();
		Map<String, List<String>> data = new HashMap<>();
		res.put("code", 8200);
		res.put("data", data);

		if(part == null || part.equals("")) {
			return res;
		}
		if(part.length() < 2) {
			res.put("data", houseService.matchBegin(part));
		} else {
			res.put("data", houseService.match(part));
		}

		return res;
	}






	/**
	 * 上传多个文件，每个文件的name值为files
	 * 可同时上传表单属性
	 */
	@RequestMapping("/uploadPics")
	@ResponseBody
	public Object uploadPics(@RequestParam("files") MultipartFile[] files, String houseId, HttpServletRequest request) throws UnsupportedEncodingException {
		
		Map<String, Object> retMap = new HashMap<>();
		
		AuthInfo authInfo = AuthInfo.get(request);
		//权限不足
		if(authInfo == null || authInfo.getUserType() == 0) {
			retMap.put("code", 8402);
			return retMap;
		}
		
		UserHouseForm userHouseForm = houseMapper.selectByPrimaryKey(houseId);
		//权限不足
		if(!userHouseForm.getUserId().equals(authInfo.getUserId())) {
			retMap.put("code", 8402);
			return retMap;
		}

//		String path = context.getRealPath("") + File.separator + "static" + File.separator + "housePic";
		String path = Config.UPLOAD_PATH + "housePic";

		for(MultipartFile file: files) {
			String filename = UUID.randomUUID().toString();
			File targetFile = new File(path, filename +".jpg");

			if(!targetFile.exists()){
				targetFile.mkdirs();
			}

			try {
				file.transferTo(targetFile);
				
				UserHousePicForm userHousePicForm = new UserHousePicForm();
				userHousePicForm.setHouseId(houseId);
				userHousePicForm.setPictureName("/static/housePic/" + filename +".jpg");
				userHousePicForm.setPictureId(filename);
				userHousePicFormMapper.insert(userHousePicForm);
			} catch (IllegalStateException | IOException e) {
				logger.warn(e.getMessage(), e);
				retMap.put("code", 8607);
				return retMap;
			}
		}
		retMap.put("code", 8200);
		return retMap;
	}

	private static class HouseInfo{
		public String houseid;
	}
	private static class SubcHouseInfo{
		public String houseId;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		public Date beginTime;
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
	private static List<Date> toDateList(List<UserSubcHouseLiveForm>records){
		List<Date>list=new ArrayList<>();
		for(UserSubcHouseLiveForm record:records){
			list.add(record.getSubBeginTime());
		}
		return list;
	}
}
