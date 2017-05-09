package com.miniProject.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniProject.dao.UserHisRecordFormMapper;
import com.miniProject.dao.UserScheduleVerifyFormMapper;
import com.miniProject.dao.UserSubcHouseLiveFormMapper;
import com.miniProject.model.UserRecordForm;
import com.miniProject.model.UserScheduleVerifyForm;
import com.miniProject.model.UserSubcHouseLiveForm;

@Service("userScheduleService")
public class UserScheduleServiceImp implements UserScheduleServiceI {
	@Autowired
	private UserScheduleVerifyFormMapper userSchVerFormMapper;
	@Autowired
	private UserSubcHouseLiveFormMapper userSubcHouseLiveFormMapper;
	@Autowired
	private UserHisRecordFormMapper userRcordFormMapper;
	/***
	 *获取已确认的预约直播(分页查询)
	 **/
	@Override
	public List<UserScheduleVerifyForm> getUserScheduleVerifyByUserId(String userId,int pageIndex){
		return userSchVerFormMapper.getUserScheduleByUserId(userId,1,pageIndex);
	}
	/***
	 *获取所有已确认的预约直播（不分页）
	 *@param userId 用户id
	 **/
	public List<UserScheduleVerifyForm> getUserAllScheduleVerifyByUserId(String userId){
		return userSchVerFormMapper.getUserAllScheduleByUserId(userId, 1);
	}
	/**
	 *获取已确认的预约直播数目(总数目) 
	 **/
	@Override
	public int getUserScheduleVerifyCountByUserId(String userId){
		return userSchVerFormMapper.getUserScheduleCountByUserId(userId,1);
	}
	/***
	 *获取待确认的预约直播(分页查询)
	 **/
	@Override
	public List<UserScheduleVerifyForm> getUserScheduleToBeVerifiedByUserId(String userId, int pageIndex) {
		return userSchVerFormMapper.getUserScheduleByUserId(userId,0,pageIndex);
	}
	/***
	 *获取所有待确认的预约直播(不分页)
	 **/
	public List<UserScheduleVerifyForm> getUserAllScheduleToBeVerifiedByUserId(String userId) {
		return userSchVerFormMapper.getUserAllScheduleByUserId(userId,0);
	}
	/***
	 *获取待确认的预约直播数目(总数目)
	 **/
	@Override
	public int getUserScheduleToBeVerifiedCountByUserId(String userId) {
		return userSchVerFormMapper.getUserScheduleCountByUserId(userId,0);
	}
	/***
	 *订阅用户取消预约
	 *或者房源发布方拒绝直播
	 * */
	@Override
	public boolean cancelOrRefuseUserSubcHouseLive(UserSubcHouseLiveForm record) {
		int num=userSubcHouseLiveFormMapper.cancelHouseLiveBySelfInUserSchedule(record);
		return (num>0?true:false);
	}
	/***
	 *获取用户历史记录 (分页)
	 **/
	@Override
	public List<UserRecordForm> getUserRecordByUserId(String userId, int pageIndex) {
		return userRcordFormMapper.getUserScheduleByUserId(userId, pageIndex);
	}
	/***
	 *获取用户所有历史记录(不分页) 
	 **/
	@Override
	public List<UserRecordForm> getUserAllRecordByUserId(String userId) {
		return userRcordFormMapper.getUserAllScheduleByUserId(userId);
	}
	/**
	 *获取记录总数 
	 **/
	@Override
	public int getUserRecordCountByUserId(String userId) {
		return userRcordFormMapper.getUserRecordCountByUserId(userId);
	}
	/**
	 *删除记录 
	 **/
	@Override
	public boolean deleteUserRecordById(String userId, String recordId) {
		return (userRcordFormMapper.deleteUserRecordByPublicKey(recordId, userId)>0?true:false);
	}
	@Override
	public UserSubcHouseLiveForm selectByPrimaryKey(String liveSubInfoId) {
		// TODO Auto-generated method stub
		return userSubcHouseLiveFormMapper.selectByPrimaryKey(liveSubInfoId);
	}
	public int selectHouseDailyLiveCountByHouseId(String houseid,int intervalTime){
		List<UserSubcHouseLiveForm>list=userSubcHouseLiveFormMapper.selectHouseDailyLiveCountByHouseId(houseid, intervalTime);
		return (list!=null?list.size():0);
	}
	
	

}
