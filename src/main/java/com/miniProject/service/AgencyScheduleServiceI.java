package com.miniProject.service;

import java.util.Date;
import java.util.List;

import com.miniProject.model.AgencyScheduleVerifyForm;
import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.model.subAgency;

public interface AgencyScheduleServiceI {
	public List<AgencyScheduleVerifyForm> getAgencyScheduleVerifyByUserId(String userId);
	public List<AgencyScheduleVerifyForm> getAgencyWaitScheduleVerifyByUserId(String userId);
	public int getAgencyScheduleVerifyCountByUserId(String userId);
	public boolean cancelorRejectHouseLive(UserSubcHouseLiveForm record);
	public boolean verifyAgencySubcHouseLive(UserSubcHouseLiveForm record);

	public  List<subAgency> getAgencyScheduleUser(String id);
	public  List<subAgency> getAgencyWaitScheduleUser(String id);
	/**
	 *通过直播预约人id , datetime 获取订阅人ID
	 *@param record UserSubcHouseLiveForm
	 *包含以下参数
	 *pubUserId 发布者id
	 *subBeginTime 预约时间
	 *verifyFlag 订阅标志位 0表示预约未确认，1表示预约已确认，2表示预约被拒绝，3,表示预约被取消
	 *
	 *@return List UserSubcHouseLiveForm
	 **/
    public  List<UserSubcHouseLiveForm> getSubInfoByliveUserAndTime(UserSubcHouseLiveForm record);
    /**
	 *通过房源id,直播预约人id , datetime 获取订阅人ID
	 *@param record UserSubcHouseLiveForm
	 *包含以下参数
	 *houseId 房源id
	 *pubUserId 发布者id
	 *subBeginTime 预约时间
	 *verifyFlag 订阅标志位 0表示预约未确认，1表示预约已确认，2表示预约被拒绝，3,表示预约被取消
	 *
	 *@return List UserSubcHouseLiveForm
	 **/
    public  List<UserSubcHouseLiveForm> getSubInfoByliveUserAndTimeAndHouse(UserSubcHouseLiveForm record);
    public  List<UserSubcHouseLiveForm> getOwnSubcHouseLiveList(String houseid,int intervalTime);
    public  int getOwnSubcHouseLiveCount(String houseid,int intervalTime);
    public int addNewHouseLiveByAgency(UserSubcHouseLiveForm record);
    public List<UserSubcHouseLiveForm> cancelSubInfoByHouseId(String houseId);
    public void getTimeIntervalSubInfoByHouseId(String houseId);
    /***
     *从tbl_live_house_info
     **/
    List<UserSubcHouseLiveForm> getAgencySelfScheduleByUserId(String pubUserId,int intervalTime);
    List<UserSubcHouseLiveForm> getlSubInfoByTimeInterval();
}
