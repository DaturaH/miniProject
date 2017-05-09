package com.miniProject.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.miniProject.model.UserFavorHouseForm;

public interface PersonalCenterServiceI {
	
	//查询“我的”
	Map<String, Object> getPersonalCenter(String userId, int userType);
	//修改用户昵称
	Map<String, Object> modifyUserName(String userId, String userName);
	//上传用户头像
	Map<String, Object> uploadPortraitFile(MultipartFile file, String userId);
	
	//查询租户收藏的房源数量
	int getFavorHouseCountByUserId(String userId);
	
	//查询中介发布的房源数量
	int getPubHouseCountByUserId(String userId);
	
	//根据页面数查询收藏房源信息(“我的”页面调用接口)
	Map<String, Object> getFavorHouseByUserId(String userId, int pageIndex);
	Map<String, Object> getAllFavorHouseByUserId(String userId);
	
	//根据传入的houseIdList获取房源信息（后台其他页面调用接口）
	List getHouseByHouseIdList(List<String> houseIdList);
	
	//设置页，查询用户配置，即是否接收推送
	Map<String, Object> getConfig(String userId);
	//修改是否接收推送配置，true为接收
	Map<String, Object> modifyConfig(String userId, boolean receiveFlag);
	//清除直播历史和消息盒子记录
	Map<String, Object> clearHistory(String userId);
}