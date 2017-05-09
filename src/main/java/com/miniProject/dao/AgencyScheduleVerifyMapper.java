package com.miniProject.dao;

import java.util.List;

import com.miniProject.model.AgencyScheduleVerifyForm;
import com.miniProject.model.UserSubcHouseLiveForm;
import com.miniProject.model.subAgency;

public interface AgencyScheduleVerifyMapper {    
	
	
    List<AgencyScheduleVerifyForm>getAgencyScheduleByUserId(String userId,int verifyFlag);
    
    List<UserSubcHouseLiveForm> getAgencySelfScheduleByUserId(String pubUserId,int intervalTime);

    int getAgencyScheduleCountByUserId(String userId);
      
    // 房源发布方取消或拒绝预订直播(1->2)(0->2)
    int cancelorRejectHouseLiveBySelf(UserSubcHouseLiveForm record);
    
    // 房源发布方确认预订直播(0->1)
    int verifyHouseLive(UserSubcHouseLiveForm record);
    
    //选择唯一房源时间列表（根据时间，房源group by ; verifyflag筛选）
    List<subAgency> getDiffHouseId(String userId);
    
    //选择等待确认唯一房源时间列表（根据时间，房源group by ; verifyflag筛选）
    List<subAgency> getWaitDiffHouseId(String userId);
    
    //通过userId ,subBeginTime 和 verifyFlag 获取订阅人信息
    List<UserSubcHouseLiveForm> getSubInfoByliveUserAndTime(UserSubcHouseLiveForm record);
    
    //通过houseId,pubUserId ,subBegin 和 verifyFlag 获取订阅人信息
    List<UserSubcHouseLiveForm> getSubInfoByliveUserAndTimeAndHouse(UserSubcHouseLiveForm record);
    
    //通过HouseId , subBeginTime , verifyFlag ＝ 0 获取当天所有的直播列表
    List<UserSubcHouseLiveForm> getOwnSubcHouseLiveList(String houseid,int intervalTime);

    //通过HouseId , subBeginTime , verifyFlag ＝ 0 获取当天所有的直播列表数量    
    int getOwnSubcHouseLiveCount(String houseid, int intervalTime);
    
    //插入houseId,pub_user_id,sub_user_id,subBeginTime,verifyFlag
    int addNewHouseLiveByAgency(UserSubcHouseLiveForm record);

    //通过HouseId 获取订阅信息
    List<UserSubcHouseLiveForm> getSubInfoByHouseId(String houseId);
    
    //通过HouseId 取消订阅用户
    int cancelSubInfoByHouseId(String houseId);
    
    //通过HouseId 获取一定时间区间内的订阅用户
    List<UserSubcHouseLiveForm> getTimeIntervalSubInfoByHouseId(String houseId);
    
    
    //定时推送，通过时间区间选定pub_user_id
    List<UserSubcHouseLiveForm> getlSubInfoByTimeInterval();
}