package com.miniProject.service;

import java.util.List;

import com.miniProject.model.UserRecordForm;
import com.miniProject.model.UserScheduleVerifyForm;
import com.miniProject.model.UserSubcHouseLiveForm;

public interface UserScheduleServiceI {
	public List<UserScheduleVerifyForm> getUserScheduleVerifyByUserId(String userId,int pageIndex);
	public List<UserScheduleVerifyForm> getUserAllScheduleVerifyByUserId(String userId);
	
	public List<UserScheduleVerifyForm> getUserScheduleToBeVerifiedByUserId(String userId,int pageIndex);
	public List<UserScheduleVerifyForm> getUserAllScheduleToBeVerifiedByUserId(String userId);
	public int getUserScheduleToBeVerifiedCountByUserId(String userId);
	public int getUserScheduleVerifyCountByUserId(String userId);
	public boolean cancelOrRefuseUserSubcHouseLive(UserSubcHouseLiveForm record);
	
	/**
	 *从记录表中查询用户历史记录 
	 **/
	public List<UserRecordForm> getUserRecordByUserId(String userId,int pageIndex);
	public List<UserRecordForm> getUserAllRecordByUserId(String userId);
	public int getUserRecordCountByUserId(String userId);
	public boolean deleteUserRecordById(String userId,String recordId);
	
	public UserSubcHouseLiveForm selectByPrimaryKey(String liveSubInfoId);
	
	public int selectHouseDailyLiveCountByHouseId(String houseid,int intervalTime);
}
