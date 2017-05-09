package com.miniProject.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.miniProject.dao.UserFavorHouseFormMapper;
import com.miniProject.dao.UserHisRecordFormMapper;
import com.miniProject.dao.UserHouseFormMapper;
import com.miniProject.dao.UserHousePicFormMapper;
import com.miniProject.dao.UserInfoMapper;
import com.miniProject.dao.UserSubcHouseLiveFormMapper;
import com.miniProject.model.UserFavorHouseForm;
import com.miniProject.model.UserHouseForm;
import com.miniProject.model.UserInfo;
import com.miniProject.util.Config;
import com.miniProject.util.RetMapUtils;

@Service("personalCenterService")
public class PersonalCenterServiceImpl implements PersonalCenterServiceI{
	private static final Logger logger = LoggerFactory.getLogger(PersonalCenterServiceImpl.class);

	@Autowired
	private UserInfoMapper userInfoMapper;	
	@Autowired
	private UserHouseFormMapper userHouseMapper;
	@Autowired
	private UserFavorHouseFormMapper userFavorHouseMapper;	
	@Autowired
	private UserHousePicFormMapper housePicMapper;
	@Autowired
	private UserSubcHouseLiveFormMapper userSubLiveMapper;
	@Autowired
	private PushServiceI pushService;
	@Autowired
	private UserHisRecordFormMapper userHisRecordMapper;
	@Autowired
	private PushHistoryServiceI pushHistoryService;
	
	@Override
	public Map<String, Object> getPersonalCenter(String userId, int userType) {

		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
		if (userInfo == null){
			return RetMapUtils.createRetMap(8404, "查询记录不存在");
		}
			
		Map<String, Object> data = new HashMap<>();
		data.put("portraitFileName", userInfo.getPortraitFileName());
		data.put("userName", userInfo.getUserName());
		
		if (0 == userType){
			//用户身份是租客，显示收藏数量
			data.put("houseNumber", this.getFavorHouseCountByUserId(userId));
		}
		else{
			//用户身份是中介或房东，显示房源数量
			data.put("houseNumber", this.getPubHouseCountByUserId(userId));
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 8200);
		result.put("data", data);
		
		return result;
	}

	@Override
	public Map<String, Object> modifyUserName(String userId, String userName) {

		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
		if (userInfo == null){
			return RetMapUtils.createRetMap(8404, "查询记录不存在");
		}
		
		userInfo.setUserName(userName);
		userInfoMapper.updateByPrimaryKey(userInfo);
		
		//FIXME : contact字段非必须
		//修改昵称时同步修改房源信息表中的contact字段
		userHouseMapper.updateContactByPrimaryKey(userId, userName + "," + userInfo.getPhoneNumber());
		
		return RetMapUtils.createRetMap(8200, "修改昵称成功");
	}

	@Autowired
    private ServletContext context;
	
	@Override
	public Map<String, Object> uploadPortraitFile(MultipartFile file, String userId) {
		
		if (file == null){
			return RetMapUtils.createRetMap(8506, "上传文件为空");
		}
		
		//头像文件上传目录 
//		final String path = context.getRealPath("") + File.separator + "static" + File.separator + "portraitFiles";
		final String path = Config.UPLOAD_PATH + "portraitFiles";
		//文件名为userId
		File targetFile = new File(path, userId + ".jpg");

		if(!targetFile.exists()){
			targetFile.mkdirs();
		}

		try {
			file.transferTo(targetFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();	
			return RetMapUtils.createRetMap(8507, "上传文件失败");
		}
		
		//头像文件写入数据库
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
		if (userInfo == null){
			return RetMapUtils.createRetMap(8404, "查询记录不存在");
		}
		
		//数据库中保存的文件路径
		userInfo.setPortraitFileName("/static/portraitFiles/" + userId + ".jpg");
		userInfoMapper.updateByPrimaryKey(userInfo);
		
		return RetMapUtils.createRetMap(8200, "修改头像成功");
	}

	@Override
	public int getFavorHouseCountByUserId(String userId) {
		return userFavorHouseMapper.getFavorHouseCountByUserId(userId);
	}

	@Override
	public int getPubHouseCountByUserId(String userId) {
		return userHouseMapper.getPubHouseCountByUserId(userId);
	}

	@Override
	public Map<String, Object> getFavorHouseByUserId(String userId, int pageIndex) {
		//根据userId以及查询页面，获取房源id列表
		List<UserFavorHouseForm> favorHouseList = userFavorHouseMapper.getFavorHouseByUserId(userId, pageIndex);
		if (favorHouseList.isEmpty()){
			logger.warn("empty favorate house list");
			return RetMapUtils.createRetMap(8200, "收藏列表为空");
		}
		
		List houseIdList = new ArrayList();
		for (UserFavorHouseForm favorHouse : favorHouseList){
			houseIdList.add(favorHouse.getHouseId());
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("date", getHouseByHouseIdList(houseIdList));
		result.put("code", 8200);
		
		return result;
	}

	@Override
	public Map<String, Object> getConfig(String userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("code", 8200);
		result.put("receiveFlag", pushService.getReceiveMsgFlag(userId));
		
		return result;
	}

	@Override
	public Map<String, Object> modifyConfig(String userId, boolean receiveFlag) {
		pushService.setReceiveMsg(userId, receiveFlag);
		return RetMapUtils.createRetMap(8200, "修改消息提醒设置成功");
	}

	@Override
	public Map<String, Object> clearHistory(String userId) {
		//清除直播历史记录
		userHisRecordMapper.deleteAllRecordsByUserId(userId);
		//清除推送历史记录
		pushHistoryService.deleteHistoryMsgByUserId(userId);
		
		//清除预约表记录（）
		userSubLiveMapper.deleteRecordsByUserIdAndFlag(userId);
		
		return RetMapUtils.createRetMap(8200, "清除历史消息成功");
	}

	@Override
	public List getHouseByHouseIdList(List<String> houseIdList) {
		List data = new ArrayList();
		for (String houseId : houseIdList){
			Map<String, Object> houseMsg = new HashMap<String, Object>();
			
			//根据houseId查询房屋信息,固定显示
			UserHouseForm houseInfo = userHouseMapper.selectByPrimaryKey(houseId);
			houseMsg.put("houseId", houseId);
			houseMsg.put("title", houseInfo.getTitle());				//房源标题：湘云雅苑 两室一厅 精装
			//houseMsg.put("location", houseInfo.getLocation());		//滨江
			houseMsg.put("blcokName", houseInfo.getLocation());			//街道名：长河 （前端代码无法更新）
			houseMsg.put("villageName", houseInfo.getVillageName());	//小区名：湘云雅苑
			houseMsg.put("houseType", houseInfo.getHouseType());		//房屋类型：三室一厅
			houseMsg.put("sharingWay", houseInfo.getSharingWay());		//合租方式
			houseMsg.put("decoration", houseInfo.getDecoration());
			houseMsg.put("price", houseInfo.getPrice());
			
			//获取房源图片，一张
			houseMsg.put("picName", housePicMapper.getPicByHouseId(houseId));
			
			//根据直播状态进行一系列判断
			if (houseInfo.getHouseState() == true){
				//房源已出租
				houseMsg.put("houseState", true);
				
				//已出租的房源权重为-1
				houseMsg.put("weight", -1);
				
				if (houseInfo.getVideoState() != null){
					//有录像
					houseMsg.put("videoUrl", houseInfo.getVideoState());
				}
				else {
					houseMsg.put("videoUrl", null);
				}
			}
			else{
				//房源未出租
				houseMsg.put("houseState", false);
				if (houseInfo.getLiveState() == true){
					//正在直播
					houseMsg.put("liveState", true);
					
					//正在直播的房源权重为50
					houseMsg.put("weight", 50);
				}
				else {
					//未直播
					houseMsg.put("liveState", false);
					
					//获取最近的直播确认时间
					Date subBeginTime = userSubLiveMapper.getNearastSubBeginTime(houseId);
					if (subBeginTime == null){
						//该房源没有被预约
						houseMsg.put("verifyState", false);
						
						//没有被预约的房源权重为0
						houseMsg.put("weight", 0);
					}
					else{
						//该房源被预约
						houseMsg.put("verifyState", true);
						
						//被预约的按一定权重计算
						//预约时间超过当前时间的权重为9
						//预约时间在当前时间之后，没半小时权重减1，到0为止
						int weight = (int)((System.currentTimeMillis() > subBeginTime.getTime()) ? 49 : 
							((49 - (subBeginTime.getTime() - System.currentTimeMillis()) / (30 * 60 * 1000)) > 1 ? (49 - (subBeginTime.getTime() - System.currentTimeMillis()) / (30 * 60 * 1000)) : 1));
						houseMsg.put("weight", weight);
						
						//该房源最近的预约时间
						houseMsg.put("subBeginTime", subBeginTime.getTime());
					}
				}
			}
			
			data.add(houseMsg);
		}

		logger.info("query house list successfully");
		return data;
	}

	@Override
	public Map<String, Object> getAllFavorHouseByUserId(String userId) {
		//根据userId以及查询页面，获取房源id列表
		List<UserFavorHouseForm> favorHouseList = userFavorHouseMapper.getAllFavorHouseByUserId(userId);
		if (favorHouseList.isEmpty()){
			logger.warn("empty favorate house list");
			return RetMapUtils.createRetMap(8200, "收藏列表为空");
		}
		
		List houseIdList = new ArrayList();
		for (UserFavorHouseForm favorHouse : favorHouseList){
			houseIdList.add(favorHouse.getHouseId());
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("date", getHouseByHouseIdList(houseIdList));
		result.put("code", 8200);
		
		return result;
	}
}