package com.miniProject.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.miniProject.model.CommunityInfo;
import com.miniProject.model.UserFavorHouseForm;
import com.miniProject.model.UserHouseForm;
import com.miniProject.model.UserHousePicForm;
import com.miniProject.model.UserSubcHouseLiveForm;
import com.sun.corba.se.spi.oa.ObjectAdapterBase;

public interface HouseServiceI {
	public UserHouseForm getByHouseId(String houseid);
	public	List<String> getPicListByHouseId(String houseid);
	public UserFavorHouseForm getFavorHouseByid(String houseid,String userid);
	public boolean insertFavorHouseInfo(UserFavorHouseForm userFavorHouseForm);
	public boolean deleteFavorHouseInfoById(String houseid,String userid);
	public boolean insertSubcHouseLiveInfo(UserSubcHouseLiveForm userSubcHouseLiveForm);
	public boolean updateSubcHouseLive(UserSubcHouseLiveForm record);
	public boolean setHouseLiving(String userId,String houseId);
	public boolean cancelHouseLiving(String userId,String houseId);
	public Date getVerifyBeginTimeByHouseId(String houseid);
	public int getUserDailyInfoCount(String houseid,String userid,int intervalTime);
	int selectHouseLiveByBeginTime(String houseid,Date beginTime);
	public List<UserSubcHouseLiveForm> getOwnVerifyTimeListById(String houseid,String userid,int intervalTime);
	public List<Date> getOwnOtherVerifyTimeListById(String houseid,String userid,int intervalTime);
	public List<UserSubcHouseLiveForm> getOtherVerifyTimeListById(String houseid,String userid,int intervalTime);
	public List<UserSubcHouseLiveForm> getOwnSubcHouseLiveListById(String houseid,String userid,int intervalTime);
	public List<Date> getLandlordVerifyTimeListById(String houseid,int intervalTime);
	public boolean refuseSubcHouseLive(UserSubcHouseLiveForm record);
	public boolean verifySubcHouseLive(UserSubcHouseLiveForm record);
	public boolean updateHouseInfo(UserHouseForm record);
	public boolean  cancelHouseLiveBySelf(UserSubcHouseLiveForm record);
	
	public  Map<String, Object> getLocation(String name);
	
	public CommunityInfo locating(String name);
	
	public List<String> match(String name);
	
	public List<String> matchBegin(String name);
	
	//修改房源直播状态
	public int modifyHouseLiveFlag(String houseId, boolean liveFlag);
}
